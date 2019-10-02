package com.luoheng.crawler.smart.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-13 12:33
 **/
public class UtilsCommand extends Command {

    public String regExtract(String reg, String rawStr){
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(rawStr);
        if (matcher.find())
            return matcher.group();
        return "";
    }

    public String regRepalce(String reg, String rawStr){
        return rawStr.replaceAll(reg, rawStr);
    }

    public String startWithCharacters(String characters, String rawStr){
        int index = rawStr.indexOf(characters);
        if (index == -1)
            return "";
        return rawStr.substring(index + characters.length());
    }

    public String endWithCharacters(String characters, String rawStr){
        int index = rawStr.lastIndexOf(characters);
        if (index == -1)
            return "";
        return rawStr.substring(0, index);
    }

    public Date string2Date(String dateStr, String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    public static void main(String[] args) throws Exception{
        UtilsCommand utilsCommand = new UtilsCommand();
        String dateStr = "2019-09-23 17:15:00";
        String format = "yyyy-MM-dd hh:mm:ss";
        Date date = (Date)utilsCommand.execute(utilsCommand, "string2Date", dateStr, format);
        System.out.println(date.toString());
    }
}
