package com.luoheng.crawler.lcrawler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

public abstract class CrawlerFactory<T extends Crawler>  {
    private Vector<T> crawlerVector;
    private boolean stop;
    private boolean over;
    private CrawlerController controller;
    private String name;
    private int crawlerCount;
    private AtomicLong completedTaskCount = new AtomicLong(0);
    private Logger logger = LoggerFactory.getLogger(CrawlerFactory.class);

    public CrawlerFactory(CrawlerController controller) {
        init();
        this.controller = controller;
    }

    public CrawlerFactory(CrawlerController controller,String name) {
        this(controller);
        this.name = name;
    }

    private void init() {
        stop = false;
        over = false;
        name = "CrawlerFactory";
    }

    public void addCompletedTaskCount(){
        completedTaskCount.incrementAndGet();
    }

    public long getCompletedTaskCount() {
        return completedTaskCount.get();
    }

    /**
     * 通知工厂结束
     * 若工厂关闭或关闭
     */
    public void notifyOver() {
        if (isOver() || isStop())
            return;
        over = true;
        logger.info("{} is stopping...", name);
        for (T crawler: crawlerVector) {
            crawler.interrupt();
        }
    }

    /**
     * 让管理的爬虫暂停。让isStop()为true
     * @see #isStop()
     */
    public void pause() {
        logger.info("{} is pausing...", name);
        stop = true;
        for (T crawler: crawlerVector) {
            crawler.pauseSelf();
        }
    }

    /**
     * 让管理的爬虫恢复暂停。让isStop()为false
     * @see #isStop()
     */
    public void resume() {
        logger.info("{} is resuming...", name);
        for (T crawler: crawlerVector) {
            crawler.resumeSelf();
        }
    }

    /**
     * 检查状态，如果工厂已经结束，则返回。
     * 如果工厂没有关闭，通知爬虫线程关闭，如果爬虫线程全部关闭，令over为true
     */
    public void inspect() {
        if (isOver())
            return;
        if (isStop()) {
            if (shouldOver()) {
                this.over = true;
                logger.info("{} is overSelf", name);
            }
        }
    }

    public CrawlerController getController() {
        return controller;
    }

    /**
     * 判断管理的爬虫是否停止
     */
    public boolean isStop() {
        return stop;
    }

    /**
     * 判断管理的爬虫是否结束
     */
    public boolean isOver() {
        return over;
    }


    /**
     * 判断管理的爬虫是否应该结束，爬虫是否结束任务需要根据实际情况来决定
     * @return
     */
    public abstract boolean shouldOver();

    public void printStatus() {
        logger.info("name: {}", name);
        int aliveCount = 0;
        for (int i = 0;i < crawlerVector.size();i++) {
            if (crawlerVector.get(i).isAlive())
                aliveCount++;
        }
        logger.info("name: {}", name);
        logger.info("total crawler count: {}", crawlerCount);
        logger.info("alive crawler count: {}", aliveCount);
        logger.info("alive crawler's number is {}", aliveCount);
    }

    /**
     * 创建爬虫数组
     * @param count 爬虫数量
     * @return 返回爬虫数组
     */
    public Vector<T> newVector(int count) {
        crawlerCount = count;
        for (int i = 0;i < count;i++) {
            T crawler = newInstance();
            crawler.setNumber(i);
            crawlerVector.add(crawler);
        }
        return crawlerVector;
    }

    /**
     * 生成爬虫实例
     * @return 返回爬虫实例
     */
    abstract T newInstance();

}
