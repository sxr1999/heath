package com.itheima;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-25 11:29
 **/
public class JasperReportsTest {
    @Test
    public void test1() throws JRException {
       String jrxmlPath = "E:\\JavaLanguage\\IdeaFile\\itcast_health\\JasperReports\\src\\main\\resources\\demo.jrxml";
       String jasperPath = "E:\\JavaLanguage\\IdeaFile\\itcast_health\\JasperReports\\src\\main\\resources\\demo.jasper";

        JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

        //构造数据
        Map paramters = new HashMap();
        paramters.put("reportDate","2019-10-10");
        paramters.put("company","itcast");
        List<Map> list = new ArrayList();
        Map map1 = new HashMap();
        map1.put("name","xiaoming");
        map1.put("address","beijing");
        map1.put("email","xiaoming@itcast.cn");
        Map map2 = new HashMap();
        map2.put("name","xiaoli");
        map2.put("address","nanjing");
        map2.put("email","xiaoli@itcast.cn");
        list.add(map1);
        list.add(map2);

        //填充数据
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperPath,
                        paramters,
                        new JRBeanCollectionDataSource(list));

        //输出文件
        String pdfPath = "D:\\test.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint,pdfPath);
    }

    @Test
    public void test2() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql:///health","root","1999");

        String jrxmlPath = "E:\\JavaLanguage\\IdeaFile\\itcast_health\\JasperReports\\src\\main\\resources\\demo1.jrxml";
        String jasperPath = "E:\\JavaLanguage\\IdeaFile\\itcast_health\\JasperReports\\src\\main\\resources\\demo1.jasper";

        //模板编译
        JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

        //准备数据
        Map map = new HashMap();
        map.put("company","传智播客");

        //填充数据
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, map, connection);

        //输出文件
        String pdfpath = "d:\\test.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint,pdfpath);

    }

    @Test
    public void test3() throws Exception {

        String jrxmlPath = "E:\\JavaLanguage\\IdeaFile\\itcast_health\\JasperReports\\src\\main\\resources\\demo2.jrxml";
        String jasperPath = "E:\\JavaLanguage\\IdeaFile\\itcast_health\\JasperReports\\src\\main\\resources\\demo2.jasper";

        JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

        Map map = new HashMap();
        map.put("company","传智播客");

        List<Map> list = new ArrayList();
        Map map1 = new HashMap();
        map1.put("name","入职体检套餐");
        map1.put("code","RZTJ");
        map1.put("age","18-60");
        map1.put("sex","男");

        Map map2 = new HashMap();
        map2.put("name","婚前体检套餐");
        map2.put("code","HQTJ");
        map2.put("age","18-60");
        map2.put("sex","不限");

        list.add(map1);
        list.add(map2);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, map, new JRBeanCollectionDataSource(list));

        String pdfpath = "d:\\test1.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint,pdfpath);

    }
}
