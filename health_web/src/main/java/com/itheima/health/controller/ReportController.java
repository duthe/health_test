package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetMealService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetMealService setMealService;

    @Reference
    private ReportService reportService;


    /**
     * 获取过去一年时间内每个月的会员总数据量
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
//        当前时间减一年
        calendar.add(Calendar.YEAR, -1);
        List<String> months = new ArrayList<>(12);
        List<Integer> memberCounts = new ArrayList<>(12);
         for (int i = 0; i < 12; i++) {
//             当前月份加1月
             calendar.add(Calendar.MONTH, 1);
//             拼接日
             String month = sdf.format(calendar.getTime()) + "-31";
//             获取当前日期之前的会员数量
             Integer memberCount = memberService.findMemberCountBeforeRegTime(month);
             months.add(month);
             memberCounts.add(memberCount);
        }
//         构建前端需要信息格式
         Map<String, Object> resultMap = new HashMap<>(2);
         resultMap.put("months", months);
         resultMap.put("memberCount", memberCounts);
         return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, resultMap);
    }


    /**
     * 获取套餐占比
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetMealReport() {
//      获取套餐占比 包含套餐名字 和预约占比
        List<Map<String, Object>> setMealReportList = setMealService.getSetMealReport();
//        构建套餐名字集合
        List<String> setmealNames = setMealReportList.stream().map(setMealReport -> (String)setMealReport.get("name")).collect(Collectors.toList());
//        构建前端需要数据格式
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("setmealNames",setmealNames);
        resultMap.put("setmealCount", setMealReportList);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, resultMap);
    }

    /**
     * 获取运营统计数据
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        Map<String, Object> resultMap = reportService.getBusinessReportData();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,resultMap);
    }

    /**
     * 导出运营统计数据到excel
     */
    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        //获取运营统计数据
        Map<String, Object> resultMap = reportService.getBusinessReportData();
        //获取模板文件路径
        String template = request.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
//        创建工作簿 读取模板
        try {
            XSSFWorkbook sheets = new XSSFWorkbook(template);
//           获取第一个工作表
            XSSFSheet sheet = sheets.getSheetAt(0);
//            填充数据到模板
            sheet.getRow(2).getCell(5).setCellValue((String) resultMap.get("reportDate"));
            // 填充会员
            sheet.getRow(4).getCell(5).setCellValue((Integer) resultMap.get("todayNewMember"));
            sheet.getRow(4).getCell(7).setCellValue((Integer) resultMap.get("totalMember"));
            sheet.getRow(5).getCell(5).setCellValue((Integer) resultMap.get("thisWeekNewMember"));
            sheet.getRow(5).getCell(7).setCellValue((Integer) resultMap.get("thisMonthNewMember"));
            // 填充预约到诊
            sheet.getRow(7).getCell(5).setCellValue((Integer) resultMap.get("todayOrderNumber"));
            sheet.getRow(7).getCell(7).setCellValue((Integer) resultMap.get("todayVisitsNumber"));
            sheet.getRow(8).getCell(5).setCellValue((Integer) resultMap.get("thisWeekOrderNumber"));
            sheet.getRow(8).getCell(7).setCellValue((Integer) resultMap.get("thisWeekVisitsNumber"));
            sheet.getRow(9).getCell(5).setCellValue((Integer) resultMap.get("thisMonthOrderNumber"));
            sheet.getRow(9).getCell(7).setCellValue((Integer) resultMap.get("thisMonthVisitsNumber"));
            // 填充热门套餐
            List<Map<String, Object>> hotSetMeal = (List<Map<String, Object>>) resultMap.get("hotSetmeal");
            int rowIndex = 12;
            for (Map<String, Object> map : hotSetMeal) {
                Row row = sheet.getRow(rowIndex);
                row.getCell(4).setCellValue(((String) map.get("name")));
                row.getCell(5).setCellValue((Long)map.get("setmeal_count"));
                BigDecimal bigDecimal = (BigDecimal)map.get("proportion");
                row.getCell(6).setCellValue(bigDecimal.doubleValue());
                row.getCell(7).setCellValue((String) map.get("remark"));
                rowIndex++;
            }


            // 内容体设置, 告诉浏览器，下载的文件是excel文档
            response.setContentType("application/vnd.ms-excel");
            String filename = "运营数据统计.xlsx";

//            filename = URLEncoder.encode(filename,"UTF-8");

            // 原始数据字节流
            byte[] bytes = filename.getBytes();
//            转换为iso-8859-1的字符串
            filename = new String(bytes, "ISO-8859-1");

            // 响应头信息设置
            response.setHeader("Content-Disposition","attachment;filename=" + filename);
            // 写到输出流
            sheets.write(response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
