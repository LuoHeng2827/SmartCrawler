package com.luoheng.crawler.smart.resource;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-17 16:49
 **/
public class FileResource implements Resource<File> {
    private String id;
    private String path;
    private File file;

    public FileResource(String id, String path) {
        this.id = id;
        this.path = path;
    }

    /*public enum ResourceTypeEnum{
            MYSQL,
            REDIS,
            FILE
        }*/
    @Override
    public File getResource() throws FileNotFoundException{
        if (file == null){
            synchronized (this){
                if (file == null){
                    file = new File(path);
                    if (!file.exists())
                        throw new FileNotFoundException("the path '" + path + "' not found");
                }
            }
        }
        return file;
    }

    @Override
    public void close() {
    }

    public String getId() {
        return id;
    }
}
