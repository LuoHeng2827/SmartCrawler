package com.luoheng.crawler.lcrawler;

public class Task {
    private String data;
    private int failedTime;

    public Task(String data, int failedTime) {
        this.data = data;
        this.failedTime = failedTime;
    }

    public Task(String data) {
        this.data = data;
        failedTime = 0;
    }

    public void failed(){
        failedTime++;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getFailedTime() {
        return failedTime;
    }

    public void setFailedTime(int failedTime) {
        this.failedTime = failedTime;
    }
}
