package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 报表控制层
 */
@RestController
@RequestMapping(value = "/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;


    ///专门报表的服务
    @Reference
    private ReportService reportService;

    @RequestMapping(value = "/getMemberReport",method = RequestMethod.GET)
    public Result getMemberReport(){
        //调用服务得到Map
        Map map = null;
        try {
            map = memberService.getMemberReport();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    /**
     * 套餐预约占比饼形图
     */
    @RequestMapping(value = "/getSetmealReport",method = RequestMethod.GET)
    public Result getSetmealReport(){

        try {
            Map rsMap = new HashMap();
            //调用服务获取setmealNames setmealCount
            List<Map<String,Object>> setmealCount =setmealService.getSetmealReport();
            List setmealNames = new ArrayList();//套餐名称
            if(setmealCount!= null && setmealCount.size()>0){
                for (Map<String, Object> map : setmealCount) {
                    //{value:222,name:xxxx}
                    String name = (String)map.get("name");//套餐名称.
                    setmealNames.add(name);
                }
            }
            rsMap.put("setmealNames",setmealNames);//['套餐一','套餐二']
            rsMap.put("setmealCount",setmealCount);//List<Map<String,Object>>  [{value:222,name:xxxx},{value:222,name:xxxx}]
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,rsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * getBusinessReportData 获取运营数据统计报表（会员数据 预约数据 热门套餐数据）
     */
    @RequestMapping(value = "/getBusinessReportData",method = RequestMethod.GET)
    public Result getBusinessReportData(){

        try {
            Map rsMap = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,rsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * :导出运营数据统计
     */
    /*@RequestMapping(value = "/exportBusinessReport",method = RequestMethod.GET)
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //获取运营统计的数据
            Map rsMap = reportService.getBusinessReportData();
            //获取模板excel对象  template/report_template.xlsx
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //HSSFWorkbook//2003  XSSFWorkbook//2007
            //统计获取map中数据
            String reportDate = (String)rsMap.get("reportDate");
            Integer todayNewMember = (Integer) rsMap.get("todayNewMember");
            Integer totalMember = (Integer) rsMap.get("totalMember");
            Integer thisWeekNewMember = (Integer) rsMap.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) rsMap.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) rsMap.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) rsMap.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) rsMap.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) rsMap.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) rsMap.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) rsMap.get("thisMonthVisitsNumber");


            //获取sheet 获取第三行第6列单元格赋值
            XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);//获取第一个sheet页
            XSSFRow row = sheetAt.getRow(2);//获取第二行
            row.getCell(5).setCellValue(reportDate);
            //.........
            row = sheetAt.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheetAt.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheetAt.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheetAt.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheetAt.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            //热门套餐
            List<Map> hotSetmeal = (List<Map>)rsMap.get("hotSetmeal");
            if(hotSetmeal != null && hotSetmeal.size()>0){
                int setmealNum = 12;
                for (Map map : hotSetmeal) {
                    //map每一行套餐数据
                    XSSFRow setmealRow = sheetAt.getRow(setmealNum);
                    setmealRow.getCell(4).setCellValue((String)map.get("name"));
                    setmealRow.getCell(5).setCellValue((String)map.get("setmeal_count").toString());
                    setmealRow.getCell(6).setCellValue((String)map.get("proportion").toString());
                    setmealRow.getCell(7).setCellValue((String)map.get("remark"));
                    setmealNum++;
                }
            }
            //通过输出流返回浏览器
            ServletOutputStream outputStream = response.getOutputStream();///outputStream输出流
            //告知浏览器 excel文件类型  文件名 attachment:以附件形式下载文件  filename文件名
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//2007
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            //application/vnd.ms-excel
            xssfWorkbook.write(outputStream);
            //释放资源
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }*/


    /**
     * jxl针对poi提供的模板技术(扩展)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/exportBusinessReport",method = RequestMethod.GET)
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //获取运营统计的数据
            Map rsMap = reportService.getBusinessReportData();
            //获取模板excel对象  template/report_template.xlsx
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            XLSTransformer transformer = new XLSTransformer();
            transformer.transformWorkbook(xssfWorkbook, rsMap);//将excel对象传入 excel模块中需要的输入传入
            //通过输出流返回浏览器
            ServletOutputStream outputStream = response.getOutputStream();///outputStream输出流
            //告知浏览器 excel文件类型  文件名 attachment:以附件形式下载文件  filename文件名
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//2007
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            //application/vnd.ms-excel
            xssfWorkbook.write(outputStream);
            //释放资源
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

}
