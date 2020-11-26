package com.itheima.health.job;



import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import com.itheima.health.utils.QiNiuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class GenerateHtmlJob {

   //订阅套餐服务接口
    @Reference
    private SetMealService setMealService;

    //注入freemarkerConfiguration
    @Autowired
    private Configuration freemarkerConfiguration;

    //注入jedis连接池
    @Autowired
    private JedisPool jedisPool;

    //log4j日志类
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    //静态页面存放目录
    @Value("${out_put_path}")
    private String path;

    /**
     * spring创建该类对象后执行的初始化方法
     * 相当于配置文件的init-method
     */
    @PostConstruct
    public void freemarkerInit() {
        //设置模板路径
        freemarkerConfiguration.setClassForTemplateLoading(this.getClass(), "/ftl");
        //设置编码
        freemarkerConfiguration.setDefaultEncoding("utf-8");

    }


    /**
     * 静态化任务
     * 启动5秒后执行 然后每隔半小时执行一次
     */
    @Scheduled(initialDelay = 5000, fixedDelay = 1800000)
    public void doGenerateHtml(){
        logger.info("静态化任务开始执行...");
        Jedis jedis = jedisPool.getResource();
        //redis中获取需要静态化的列表 格式 "id|type|timestamp"
        Set<String> idSet = jedis.smembers("setMeal:static:html");
        logger.debug("需要静态化操作的套餐id个数:{}", idSet.size());
        idSet.forEach(id->{
            String[] idArr = id.split("\\|");
            Integer setMealId = Integer.valueOf(idArr[0]);
            String type = idArr[1];
            String fileName = "setmeal_" + setMealId + ".html";
            if ("1".equals(type)) {
                //增 删 改套餐 type=1 生成静态页面
                generateDetailHtml(setMealId, fileName);

            } else if ("0".equals(type)){
                //删除套餐 type=1  删除静态页面
                logger.debug("删除静态页面{}", fileName);
                new File(path + File.separator + fileName).delete();

            }
            //删除redis中的id值
            jedis.srem("setMeal:static:html",id);
        });

        // 有需要静态化的套餐 就需要重新生成套餐列表
        if (idSet.size() > 0) {
            generateSetMealListHtml();
        }

        jedis.close();

    }

    /**
     * 生成套餐列表html
     */
    public void generateSetMealListHtml() {
        logger.info("开始生成套餐列表");
        List<Setmeal> setMealList = setMealService.findAll();
        //设置套餐图片完整路径
        setMealList.forEach(setMeal -> {
            setMeal.setImg(QiNiuUtils.DOMAIN + setMeal.getImg());
        });
        String templateName = "mobile_setmeal.ftl";
        String fileName = "mobile_setmeal.html";
        //设置数据模型
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("setmealList", setMealList);
        generateHtml(fileName, templateName, dataMap);
    }


    /**
     * 生成套餐详情页面静态html
     * @param setMealId 套餐id
     * @param fileName  生成的文件名称
     */
    public void generateDetailHtml(Integer setMealId, String fileName) {
        logger.debug("开始生成套餐详情{}", fileName);
        //套餐详情页面模板文件
        String templateName = "mobile_setmeal_detail.ftl";
        Setmeal setMeal = setMealService.findSetMealDetailById(setMealId);
        //设置套餐图片完整路径
        setMeal.setImg(QiNiuUtils.DOMAIN + setMeal.getImg());
        //设置数据模型
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("setmeal", setMeal);
        generateHtml(fileName, templateName, dataMap);
    }


    /**
     * 生成静态html
     * @param fileName  生成的文件名称
     * @param templateName 模板名称
     * @param dataMap   数据模型
     */
    public void generateHtml(String fileName, String templateName,  Map<String, Object> dataMap) {
        try (
                //缓冲流 转换流  字符流设置编码
                BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + File.separator + fileName), StandardCharsets.UTF_8))
            )
        {
            Template template = freemarkerConfiguration.getTemplate(templateName);
            //填充数据到模板并生成文件
            template.process(dataMap, bos);
            bos.flush();

        } catch (Exception e) {
            logger.error("生成静态页面失败,文件{}", fileName, e);
        }
        logger.info("生成文件{}成功",fileName);
    }
}
