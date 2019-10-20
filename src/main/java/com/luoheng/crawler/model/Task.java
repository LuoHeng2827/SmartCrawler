package com.luoheng.crawler.model;

public class Task {
    private Site site;
    private int failedTime;
    private Bean meta;

    public Task(Site site, int failedTime, Bean meta){
        this(site, failedTime);
        this.meta = meta;
    }
    public Task(Site site, int failedTime) {
        this(site);
        this.failedTime = failedTime;
    }

    public Task(Site site) {
        this.site = site;
        failedTime = 0;
    }

    public void failed(){
        failedTime++;
    }

    public Site getSite() {
        return site;
    }

    public Task setSite(Site site) {
        this.site = site;
        return this;
    }

    public int getFailedTime() {
        return failedTime;
    }

    public Task setFailedTime(int failedTime) {
        this.failedTime = failedTime;
        return this;
    }
}
