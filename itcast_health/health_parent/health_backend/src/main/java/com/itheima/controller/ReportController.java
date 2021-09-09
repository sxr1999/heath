package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-23 11:55
 **/
@RequestMapping("/report")
@RestController
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetmealService setmealService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        //使用模拟数据测试对象是否能够转为echarts的数据
        Map<String,Object> map = new HashMap<String, Object>();
        List<String> months = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();//获得日历对象，模拟时间就是当前时间
        //计算过去一年的12个月
        calendar.add(Calendar.MONTH,-12);
        Date time = calendar.getTime();
        for (int i = 0; i < 12; i++) {
            calendar.add(calendar.MONTH,1);//当前时间往后推一个月
            Date date = calendar.getTime();
            months.add(new SimpleDateFormat("yyyy.MM").format(date));
        }

        map.put("months",months);


        List<Integer> memberCount = memberService.findMemberCountByMonths(months);

        map.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }


    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        Map<String,Object> map = new HashMap<String, Object>();
        try{
            List<String> setmealNames = new ArrayList<String>();

            List<Map<String,Object>> setmealCount = setmealService.findSetmealCount();
            map.put("setmealCount",setmealCount);
            for (Map<String, Object> map1 : setmealCount) {
                String  name = (String) map1.get("name");
                setmealNames.add(name);
            }
            map.put("setmealNames",setmealNames);

            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @Reference
    private ReportService reportService;

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
       try{
           Map<String, Object> map = reportService.getBusinessReportData();
           return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
       }catch (Exception e){
           return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
       }
    }


    //导出运营数据
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try{
            Map<String, Object> result = reportService.getBusinessReportData();
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");


            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            //基于excel模板文件在内存中创建一个excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            XSSFSheet sheet = excel.getSheetAt(0);
            //获得第三行
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }
            //使用输出流经行下载,基于浏览器作为客户端下载
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
            excel.write(out);

            out.flush();;
            excel.close();
            return null;

        }catch (Exception e){
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    @RequestMapping("/exportBusinessReport4PDF")
    public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response){
        try{
            Map<String, Object> result = reportService.getBusinessReportData();
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");


            String jrxmlPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jrxml";
            String jasperPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jasper";

            JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath,result, new JRBeanCollectionDataSource(hotSetmeal));

            //使用输出流经行下载,基于浏览器作为客户端下载
            OutputStream out = response.getOutputStream();
            response.setContentType("application/pdf");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");//指定以附件形式进行下载

            JasperExportManager.exportReportToPdfStream(jasperPrint,out);
            out.flush();;
            out.close();
            return null;

        }catch (Exception e){
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

}
