package com.luoheng.crawler.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author ：LuoHeng
 * @date ：Created in 2019/10/19 12:28
 * @description：
 */
public class Page {
    private byte[] rawData;

    private String charset;

    private Logger logger = LoggerFactory.getLogger(Page.class);

    public Html toHtml(){
        Html html;
        try {
            html = new Html(new String(rawData, charset));
        }catch (UnsupportedEncodingException e){
            logger.warn("toHtml exception:{},use default charset", e.getMessage());
             html = new Html(new String(rawData, Charset.defaultCharset()));
        }
        return html;
    }

    public String toText(){
        String text;
        try {
            text = new String(rawData, charset);
        }catch (UnsupportedEncodingException e){
            logger.warn("toText exception:{},use default charset", e.getMessage());
            text = new String(rawData, Charset.defaultCharset());
        }
        return text;
    }

    public JsonObject toJson(){
        Gson gson = new Gson();
        return gson.fromJson(toText(), JsonObject.class);
    }

    public <T> T toObject(Class<T> classOfT){
        Gson gson = new Gson();
        return gson.fromJson(toText(), classOfT);
    }

    public boolean toFile(String filePath, String fileName) throws IOException {
        File file = new File(filePath, fileName);
        if (!file.exists()){
            if (!file.createNewFile()){
                logger.warn("failed to create multipartForm: {}", file.getAbsolutePath());
                return false;
            }
        }
        else{
            if (!file.delete()){
                logger.info("failed to delete old multipartForm: {}", file.getAbsolutePath());
                return false;
            }
            if (!file.createNewFile()){
                logger.warn("failed to create multipartForm: {}", file.getAbsolutePath());
                return false;
            }
        }
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        outputStream.write(rawData);
        outputStream.close();
        return true;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
