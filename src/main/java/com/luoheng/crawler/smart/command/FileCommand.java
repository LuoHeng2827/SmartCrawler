package com.luoheng.crawler.smart.command;

import com.luoheng.crawler.smart.resource.FileResource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-09 11:30
 **/
public class FileCommand extends Command<FileCommand> {
    private FileResource fileResource;
    public FileCommand(FileResource fileResource){
        this.fileResource = fileResource;
    }

    public String[] lines() throws IOException {
        File file = fileResource.getResource();
        List<String> lineList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = reader.readLine()) != null) {
            lineList.add(line);
        }
        reader.close();
        return lineList.toArray(new String[0]);
    }

}
