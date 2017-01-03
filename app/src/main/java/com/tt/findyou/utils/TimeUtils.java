package com.tt.findyou.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TT on 2017/1/3.
 */
public class TimeUtils {
    private static SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime =System.currentTimeMillis();
        long time = (curTime - timeStamp) / 1000 ;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            throw new RuntimeException("参数时间大于当前时间");
        }
    }

    public static String convertTimeToFormat(Date date) {
        return convertTimeToFormat(date.getTime());
    }
    public static String convertTimeToFormat(String str,DateFormat format) {
        try {
            return convertTimeToFormat(format.parse(str).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.println("时间格式化错误！");
        }
        return "时间格式化错误！";
    }
    public static String convertTimeToFormat(String str) {
        return convertTimeToFormat(str,defaultFormat);
    }


    public static int parseAge(String str){
        Calendar curr = Calendar.getInstance();
        int currYear = curr.get(Calendar.YEAR);
        int currMonth = curr.get(Calendar.MONTH);
        int currDate = curr.get(Calendar.DATE);
        try {
            curr.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(str));
            int birthYear = curr.get(Calendar.YEAR);
            int birthMonth = curr.get(Calendar.MONTH);
            int birthDate = curr.get(Calendar.DAY_OF_MONTH);

            int age = currYear-birthYear;
            if(currMonth < birthMonth && (currMonth == birthMonth && currDate < birthDate))
                age--;
            return age;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
