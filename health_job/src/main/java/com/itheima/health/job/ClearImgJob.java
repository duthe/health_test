package com.itheima.health.job;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.SetMealService;
import com.itheima.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClearImgJob {

    @Reference
    private SetMealService setMealService;

    private Logger Log = LoggerFactory.getLogger(ClearImgJob.class);


    /**
     * 定时清理七牛云上垃圾图片任务
     *上线时使用 @Scheduled(cron = "0 0 2 * * ? *")每天凌晨2点执行一次
     *
     * 方便测试 启动5秒后执行然后每间隔86400秒执行一次
     */
    @Scheduled(initialDelay = 5000, fixedDelay = 86400000)
    public void clearImg() {
        Log.info("开始清理图片....");
        //获取七牛云中所有图片集合
        List<String> qnFileList = QiNiuUtils.listFile();
        Log.debug("七牛云有{}张图片",qnFileList!=null?qnFileList.size():0);
        //获取数据库中图片集合
        List<String> dbImgList = setMealService.findImgs();
        Log.debug("数据库中有{}张图片",dbImgList!=null?dbImgList.size():0);
        //七牛云中图片减去数据库中图片得到垃圾图片集合
        qnFileList.removeAll(dbImgList);
        QiNiuUtils.removeFiles(qnFileList.toArray(new String[]{}));
        Log.info("清理{}张垃圾图片成功", qnFileList.size());

    }

}
