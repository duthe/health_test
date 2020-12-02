package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.OrderService;
import com.itheima.health.service.SetMealService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetMealService setMealService;


    /**
     * 体检预约
     * @param paramMap
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map<String, String> paramMap) {
//      校验验证码
        String telephone = paramMap.get("telephone");
        String key = RedisMessageConstant.SENDTYPE_ORDER + ":" + telephone;
        Jedis jedis = jedisPool.getResource();
        //获取redis中存储的验证码
        String code = jedis.get(key);

        if (code == null) {
            //redis中没有获取到验证码 则表示验证码过期或者是用户未获取验证码
            return new Result(false, "验证码已过期，请重新获取验证码");
        }

        //校验验证码是否正确
        if (!code.equals(paramMap.get("validateCode"))) {
            //不正确
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //校验成功 删除redis中的验证码 防止重复验证
        jedis.del(key);
        jedis.close();
        //设置预约方式
        paramMap.put("orderType", "微信预约");
        Integer id = orderService.submit(paramMap);
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("id", id + "");
        return new Result(true, MessageConstant.ORDER_SUCCESS, resultMap);
    }

    /**
     * 根据预约id查询预约信息
     * @param id 预约订单id
     * @return 包含预约人姓名 套餐名称 预约日期 预约类型
     */
    @RequestMapping("/findDetailById")
    public Result findDetailById(int id){
       Map<String, Object> resultMap = orderService.findDetailById(id);
       return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, resultMap);
    }

    /**
     * 导出预约订单信息
     */
    @GetMapping("/exportSetMealInfo")
    public void exportSetMealInfo(int id, HttpServletResponse response) throws Exception {
//       设置响应头
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachement;filename=setmealInfo.pdf");

        //查询预约信息
        Map<String, Object> orderInfoMap = orderService.findDetailById(id);
//        获取订单的套餐ID
        Integer setMealId = (Integer) orderInfoMap.get("id");
//        获取订单的套餐详情
        Setmeal setmeal = setMealService.findSetMealDetailById(setMealId);
//        创建文件对象

        Document document = new Document();
//        文件写到输出流
        PdfWriter.getInstance(document,response.getOutputStream());
//        打开文档
        document.open();
//        设置支持中文
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese);
//        添加段落 设置预约信息
        document.add(new Paragraph("预约订单信息",font));
        document.add(new Paragraph("体检人: " + (String)orderInfoMap.get("member"),font));
        document.add(new Paragraph("体检套餐: " + (String)orderInfoMap.get("setmeal"),font));
        document.add(new Paragraph("体检日期: " + (String) orderInfoMap.get("orderDate"),font));
        document.add(new Paragraph("预约类型: " + (String)orderInfoMap.get("orderType"),font));

//      套餐详情
        Table table = new Table(3); // 3列  表头
        table.addCell(buildCell("项目名称",font));
        table.addCell(buildCell("项目内容",font));
        table.addCell(buildCell("项目解读",font));

        // 检查组
        List<CheckGroup> checkGroups = setmeal.getCheckGroups();
        if(null != checkGroups){
            for (CheckGroup checkGroup : checkGroups) {
                // 项目名称列
                table.addCell(buildCell(checkGroup.getName(),font));
                // 项目内容, 把所有的检查项拼接
                List<CheckItem> checkItems = checkGroup.getCheckItems();
                String checkItemStr = "";
                if(null != checkItems){
                    StringJoiner stringJoiner = new StringJoiner(" ");
                    for (CheckItem checkItem : checkItems) {
                        stringJoiner.add(checkItem.getName());
                    }
                    // 检查项的拼接完成
                    checkItemStr = stringJoiner.toString();
                }
                table.addCell(buildCell(checkItemStr,font));
                // 项目解读
                table.addCell(buildCell(checkGroup.getRemark(),font));
            }
        }
        // 添加表格
        document.add(table);
        document.close();

    }



    public Cell buildCell(String content, Font font)
            throws BadElementException {
        Phrase phrase = new Phrase(content, font);
        return new Cell(phrase);
    }
}
