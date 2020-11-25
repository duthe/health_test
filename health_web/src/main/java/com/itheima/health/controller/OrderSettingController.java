package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 上传预约模板
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile) throws Exception {
        //poi解析
        List<String[]> stringList = POIUtils.readExcel(excelFile);
        SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
        //数组集合转换为 实体类对象集合
        List<OrderSetting> orderSettingList = stringList.stream().map(strings -> {
            OrderSetting os = new OrderSetting();
            try {
                os.setOrderDate(sdf.parse(strings[0]));
                os.setNumber(Integer.valueOf(strings[1]));
            } catch (ParseException e) {
                //TODO: 异常处理

            }
            return os;

        }).collect(Collectors.toList());
        //批量添加
        orderSettingService.batchAdd(orderSettingList);
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }


    /**
     * 根据月份获取预约信息
     * @param month
     * @return
     */
    @RequestMapping("/findOrderSettingByMonth")
    public Result findOrderSettingByMonth(String month) {
        /**
         *   返回数据模型
         *   [
         *     { date: 1, number: 120, reservations: 1 },
         *     { date: 3, number: 120, reservations: 1 },
         *    ]
         */
        List<Map<String, Integer>> resultList = orderSettingService.findOrderSettingByMonth(month);
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, resultList);
    }

    /**
     * 根据日期设置当天预约信息
     * @param orderSetting
     * @return
     */
    @RequestMapping("/editNumberByOrderDate")
    public Result editNumberByOrderDate(@RequestBody OrderSetting orderSetting) {
        orderSettingService.updateOrderSetting(orderSetting);
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);

    }


}
