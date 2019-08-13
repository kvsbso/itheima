package com.itheima.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//获取中间月份工具
public class MonthUtils {
	/**
	 * 根据时间范围获得月份集
	 *
	 * @return
	 */
	public static List<String> getRangeSet(String beginDate, String endDate) {
		List<String> rangeSet = null;
		SimpleDateFormat sdf = null;
		Date begin_date = null;
		Date end_date = null;
		rangeSet = new java.util.ArrayList<String>();
		sdf = new SimpleDateFormat("yyyy-MM");
		try {
			begin_date = sdf.parse(beginDate);//定义起始日期
			end_date = sdf.parse(endDate);//定义结束日期
		} catch (Exception e) {
			System.out.println("时间转化异常，请检查你的时间格式是否为yyyy-MM或yyyy-MM-dd");
		}
		Calendar dd = Calendar.getInstance();//定义日期实例
		if (begin_date.getTime() < end_date.getTime()) {
			dd.setTime(begin_date);//设置日期起始时间
			while (!dd.getTime().after(end_date)) {//判断是否到结束日期
				rangeSet.add(sdf.format(dd.getTime()));
				dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
			}
			return rangeSet;
		} else {
			dd.setTime(end_date);//设置日期起始时间
			while (!dd.getTime().after(begin_date)) {//判断是否到结束日期
				rangeSet.add(sdf.format(dd.getTime()));
				dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
			}
			return rangeSet;
		}
	}

}
