package cn.viworks.vgenerator.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date getSystemDate() {
        return new Date();
    }
	
    public static String format(Date date, String formator) {
		if(date == null){
			return "";
		}
		return new SimpleDateFormat(formator).format(date);
    }

    public static Date getDate(String dt, String formator) {
        Date date = null;
        try{
            date = new SimpleDateFormat(formator).parse(dt);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return date;
    }
}
