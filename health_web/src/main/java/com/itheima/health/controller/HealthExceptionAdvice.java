package com.itheima.health.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理类
 */
@RestControllerAdvice
public class HealthExceptionAdvice {

    /**
     *  info:  打印日志，记录流程性的内容
     *  debug: 记录一些重要的数据 id, orderId, userId
     *  error: 记录异常的堆栈信息，代替e.printStackTrace();
     *  工作中不能有System.out.println(), e.printStackTrace();
     */
    private Logger logger = LoggerFactory.getLogger(HealthExceptionAdvice.class);


    /**
     * 处理自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException e) {
        return new Result(false, e.getMessage());
    }


    /**
     * 处理所有未知异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        logger.error("未知异常", e);
        return new Result(false, "发生未知异常,请稍后重试！");
    }







}