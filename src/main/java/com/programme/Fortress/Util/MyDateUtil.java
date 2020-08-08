package com.programme.Fortress.Util;

import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

@Configuration
public class MyDateUtil {

    /**
     * 获取当前时间
     * @param standard 日期格式
     * @return
     */
    public static String getMaxDate(String standard){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(standard));
    }

    /**
     * 获取指定时间的历史时间
     * @param selectDate 指定时间
     * @param standard 日期格式
     * @param day 查询天数
     * @return
     */
    public static String getHistoryDate(String selectDate,String standard,int day){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(standard);
        LocalDateTime parse = LocalDateTime.from(LocalDate.parse(selectDate, timeFormatter).atStartOfDay());
        return parse.plusDays(day).format(DateTimeFormatter.ofPattern(standard));
    }

    /**
     * 获取与当前时间的时间差(秒)
     * @param time 查询时间
     * @param standard 日期格式
     * @return
     * @throws ParseException
     */
    public static long getTimeInterval(String time,String standard) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(standard);
        Date nowDate = new Date();
        Date date=format.parse(time);
        long dateInterval=date.getTime()-nowDate.getTime();
        return dateInterval/(1000);
    }

    /**
     * 获取日期差
     * @param dateStart 开始时间
     * @param dateEnd   结束时间
     * @return
     */
    public static long getDayDifference(String dateStart,String dateEnd){
        long daysDiff = ChronoUnit.DAYS.between(LocalDate.parse(dateStart, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalDate.parse(dateEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return daysDiff;
    }

}
