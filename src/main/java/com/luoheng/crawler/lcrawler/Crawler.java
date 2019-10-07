package com.luoheng.crawler.lcrawler;


import com.luoheng.crawler.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class Crawler extends Thread {
    private static final long DEFAULT_CRAWL_INTERVAL = 1000L;
    private static final long DEFAULT_MAX_PAUSE_TIME = 180000L;
    private long pauseTime;
    protected int number = 0;
    /**
     * 爬虫的当前任务
     */
    private Task currentTask;
    /**
     * 爬虫结束标识符
     */
    private boolean over;
    /**
     * 爬取间隔
     */
    private long crawlInterval;
    /**
     * 负责该爬虫的工厂
     */
    private CrawlerFactory factory;
    private boolean pause;
    private Logger logger = LoggerFactory.getLogger(Crawler.class);


    public Crawler(CrawlerFactory factory) {
        init();
        this.factory = factory;
    }

    public Crawler(CrawlerFactory factory, String name) {
        this(factory);
        setName(name);
    }


    public Crawler(CrawlerFactory factory, String name, long crawlInterval) {
        this(factory, name);
        this.crawlInterval = crawlInterval;
    }

    private void init() {
        over = false;
        pause = false;
        crawlInterval = DEFAULT_CRAWL_INTERVAL;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public abstract Task getTaskData();

    public abstract void crawl(Task task);

    public boolean isOver() {
        return over;
    }

    public void overSelf() {
        over = true;
    }

    public void setCrawlInterval(long crawlInterval) {
        if (crawlInterval > 0)
            this.crawlInterval = crawlInterval;
    }

    public void pauseSelf() {
        pause = true;
    }

    public void resumeSelf() {
        pause = false;
        pauseTime = 0;
    }

    public boolean isPause() {
        return pause;
    }

    @Override
    public void run() {
        long totalPauseTime = 0L;
        while (!over) {
            if (pause) {
                ThreadUtil.waitMillis(pauseTime);
                totalPauseTime += pauseTime;
                if (totalPauseTime >= DEFAULT_MAX_PAUSE_TIME) {
                    //todo 发送警告
                }
                continue;
            }
            currentTask = getTaskData();
            if (currentTask == null)
                ThreadUtil.waitMillis(100);
            crawl(currentTask);
            getFactory().addCompletedTaskCount();
            ThreadUtil.waitMillis(crawlInterval);
        }
    }

    public CrawlerFactory getFactory() {
        return factory;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    @Override
    public void interrupt() {
        over = true;
    }
}
