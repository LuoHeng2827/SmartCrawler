package com.luoheng.crawler.util.mysql;

public class DBConfig {
    private String url;
    private String user;
    private String passwords;

    public DBConfig(String url, String user, String passwords){
        this.url = url;
        this.user = user;
        this.passwords = passwords;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUser(){
        return user;
    }

    public void setUser(String user){
        this.user = user;
    }

    public String getPasswords(){
        return passwords;
    }

    public void setPasswords(String passwords){
        this.passwords = passwords;
    }
}
