package com.luoheng.crawler.lcrawler;


import com.luoheng.crawler.model.Bean;
import com.luoheng.crawler.model.Page;
import com.luoheng.crawler.model.Task;
import com.luoheng.crawler.monitor.WarnSender;
import com.luoheng.crawler.processor.Processor;
import com.luoheng.crawler.util.ExceptionUtil;
import com.luoheng.crawler.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public abstract class Crawler extends Thread {
    public static final int ASSIGNMENT_STRATEGY_NORMAL= 1;
    public static final int ASSIGNMENT_STRATEGY_LAZY= 2;

    private static final long DEFAULT_CRAWL_INTERVAL = 1000L;
    private static final long DEFAULT_MAX_PAUSE_TIME = 180000L;
    public static final long DEFAULT_MAX_PAUSE_TIME_STEP = 200L;
    private static final long DEFAULT_MAX_WAIT_TASK_TIME = 6000L;
    private static final long DEFAULT_WAIT_TASK_TIME_STEP = 200L;
    private WarnSender warnSender;
    private long maxPauseTime = DEFAULT_MAX_PAUSE_TIME;
    private long pauseTimeStep = DEFAULT_MAX_PAUSE_TIME_STEP;
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

    /**
     * 等待任务超时时间
     */
    private long maxWaitTaskTime = DEFAULT_MAX_WAIT_TASK_TIME;
    private long waitTaskTimeStep = DEFAULT_WAIT_TASK_TIME_STEP;
    private boolean pause;
    private List<Processor> processorList = new ArrayList<>();
    private int assignmentStrategy;
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
        maxPauseTime = 0;
    }

    public boolean isPause() {
        return pause;
    }

    @Override
    public void run() {
        while (!over) {
            try {
                if (!checkWaitTime()){
                    continue;
                }
                currentTask = waitTask();
                if (currentTask == null)
                    continue;
                assignTask(currentTask);
                getFactory().addCompletedTaskCount();
                ThreadUtil.waitMillis(crawlInterval);
            }catch (Exception e){
                if (warnSender != null){
                    warnSender.send(String.format("SmartCrawler overall situation exception: %s", ExceptionUtil.getTotal(e)));
                }
            }
        }
    }

    private Task waitTask(){
        long totalWaitTaskTime = 0L;
        currentTask = getTaskData();
        while (currentTask == null){
            totalWaitTaskTime += waitTaskTimeStep;
            ThreadUtil.waitMillis(waitTaskTimeStep);
            if (totalWaitTaskTime > maxWaitTaskTime){
                String errorMsg = "SmartCrawler wait task time is more than set up value";
                warnSender.send(errorMsg);
                logger.info(errorMsg);
                return null;
            }
            currentTask = getTaskData();
        }
        return currentTask;
    }

    private boolean checkWaitTime(){
        long totalPauseTime = 0L;
        while (pause) {
            ThreadUtil.waitMillis(pauseTimeStep);
            totalPauseTime += maxPauseTime;
            if (totalPauseTime >= maxPauseTime) {
                if (warnSender != null){
                    String errorMsg = "SmartCrawler pause time is more than set up value";
                    warnSender.send(errorMsg);
                    logger.info(errorMsg);
                }
                return false;
            }
        }
        return true;
    }

    private void doProcessorTask(Processor processor, Task task){
        Page page = processor.requestPage(task);
        Bean bean = processor.process(page);
        processor.handleResult(bean);
    }

    private void assignTask(Task task){
        switch (assignmentStrategy){
            case ASSIGNMENT_STRATEGY_NORMAL:
                for (Processor processor : processorList){
                    if (processor.isAccept(task)){
                        doProcessorTask(processor, task);
                    }
                }
                break;
            case ASSIGNMENT_STRATEGY_LAZY:
                for (Processor processor : processorList){
                    if (processor.isAccept(task)){
                        doProcessorTask(processor, task);
                        break;
                    }
                }
                break;
            default:
                break;
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

    public WarnSender getWarnSender() {
        return warnSender;
    }

    public Crawler setWarnSender(WarnSender warnSender) {
        this.warnSender = warnSender;
        return this;
    }
}
