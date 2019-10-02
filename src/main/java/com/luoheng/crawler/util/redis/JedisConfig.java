package com.luoheng.crawler.util.redis;

public class JedisConfig{
    private String host;
    private int port;
    private String passwords;

    public JedisConfig(String host, int port, String passwords){
        this.host=host;
        this.port=port;
        this.passwords=passwords;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }
}
