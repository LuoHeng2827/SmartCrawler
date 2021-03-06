package com.luoheng.crawler.util;

public class ExceptionUtil {
    public static String getTotal(Exception e){
        StringBuilder builder=new StringBuilder();
        if(e.getCause()!=null)
            builder.append(e.getCause().getMessage());
        for(StackTraceElement element:e.getStackTrace()){
            builder.append(e.getClass().getName()) ;
            builder.append(": ");
            builder.append(e.getMessage());
            builder.append("\n\t");
            builder.append("at ");
            builder.append(element.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
    public static void main(String[] args){
        try{
            throw new Exception("hello");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(getTotal(e));
        }
    }
}
