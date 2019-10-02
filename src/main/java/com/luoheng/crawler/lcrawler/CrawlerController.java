package com.luoheng.crawler.lcrawler;

import com.luoheng.crawler.util.ThreadUtil;
import com.luoheng.crawler.util.redis.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 *
 */
public class CrawlerController extends Thread {
    /**
     * 默认的监视函数执行间隔
     */
    private static final long DEFAULT_MONITOR_INTERVAL = 500;

    private static final long DEFAULT_HISTORY_INTERVAL = 1000;
    /**
     * 监视函数执行间隔
     */
    private long monitorInterval;
    /**
     * 判断爬取工作是否完成
     */
    private boolean complete;
    private Timer historyTimer;
    private long historyCountInterval;
    private Map<CrawlerFactory,List<Long>> historyCount = new HashMap<>();
    /**
     * 用来保存爬虫工作链，Key为爬虫工厂，Value为爬虫数组
     */
    private Map<CrawlerFactory,Vector<Crawler>> allCrawlers = new LinkedHashMap<>();
    private Map<CrawlerFactory,Map<String,Integer>> relatedRedisKey = new LinkedHashMap<>();
    private Logger logger = LoggerFactory.getLogger(CrawlerController.class);

    public CrawlerController() {
        init();
    }

    public CrawlerController(String name) {
        this();
        setName(name);
    }

    public CrawlerController monitorInterval(long monitorInterval) {
        this.monitorInterval = monitorInterval;
        return this;
    }

    private void init() {
        monitorInterval = DEFAULT_MONITOR_INTERVAL;
        complete = false;
        historyTimer = new Timer();
        historyTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Map.Entry<CrawlerFactory,List<Long>> entry : historyCount.entrySet()) {
                    entry.getValue().add(entry.getKey().getCompletedTaskCount());
                }
            }
        }, 0, 1000);
    }

    /**
     * 添加节点，形成爬虫工作链
     *
     * @param factory 管理爬虫生命周期的工厂类
     * @param count   要创建爬虫的数量
     * @return 返回自身，用于链式调用
     */
    @SuppressWarnings ("unchecked")
    public CrawlerController add(CrawlerFactory factory, int count) {
        Vector<Crawler> crawlerVector = factory.newVector(count);
        allCrawlers.put(factory, crawlerVector);
        return this;
    }

    public CrawlerController add(CrawlerFactory factory, int count, Map<String,Integer> redisKey) {
        add(factory, count);
        relatedRedisKey.put(factory, redisKey);
        return this;
    }

    /**
     * 通知给定爬虫工厂的下一个爬虫工厂关闭
     *
     * @param factory
     */
    public void notifyNextOver(CrawlerFactory factory) {
        boolean isNext = false;
        for (Map.Entry<CrawlerFactory,Vector<Crawler>> entry : allCrawlers.entrySet()) {
            if (isNext) {
                entry.getKey().notifyOver();
                break;
            }
            if (entry.getKey().equals(factory)) {
                isNext = true;
            }
        }
    }

    /*private void monitorRedisKey() {
        for (Map.Entry<CrawlerFactory,Map<String,Integer>> entryI : relatedRedisKey.entrySet()) {
            CrawlerFactory factory = entryI.getKey();
            Map<String,Integer> redisKeyMap = entryI.getValue();
            for (Map.Entry<String,Integer> entryJ : redisKeyMap.entrySet()) {
                String redisKey = entryJ.getKey();
                int maxCount = entryJ.getValue();
                if (JedisUtil.llen(redisKey) >= maxCount) {
                    if (!factory.isStop())
                        factory.pause();
                }else if (JedisUtil.llen(redisKey) <= maxCount * 0.8) {
                    if (factory.isStop())
                        factory.resume();
                }
            }
        }
    }*/

    /**
     * 监视爬虫工厂是否关闭，如果关闭，则通知下一个爬虫工厂关闭
     */
    private void monitorCrawlerFactory() {
        for (Map.Entry<CrawlerFactory,Vector<Crawler>> entry : allCrawlers.entrySet()) {
            entry.getKey().inspect();
            if (entry.getKey().isOver())
                notifyNextOver(entry.getKey());
        }
    }

    /**
     * 监视爬虫数量，若线程因为不可逆性的原因关闭，则重新创建爬虫不足数量
     */
    private void monitorCrawlerCount() {
        complete = true;
        for (Map.Entry<CrawlerFactory,Vector<Crawler>> entry : allCrawlers.entrySet()) {
            Vector<Crawler> crawlerVector = entry.getValue();
            CrawlerFactory factory = entry.getKey();
            if (factory.isOver())
                continue;
            complete = false;
            for (int i = crawlerVector.size() - 1; i >= 0; i--) {
                Crawler crawler = crawlerVector.get(i);
                if (!crawler.isAlive()) {
                    if (!crawler.isOver()) {
                        Crawler newCrawler = factory.newInstance();
                        newCrawler.setNumber(i);
                        crawlerVector.remove(i);
                        crawlerVector.add(i, newCrawler);
                        newCrawler.start();
                    }
                }
            }
        }
    }

    public void monitor() {
        monitorCrawlerFactory();
        monitorCrawlerCount();
        //monitorRedisKey();
    }

    /**
     * 启动爬虫线程
     */
    private void startCrawler() {
        for (Map.Entry<CrawlerFactory,Vector<Crawler>> entry : allCrawlers.entrySet()) {
            Vector<Crawler> crawlerVector = entry.getValue();
            for (Crawler crawler : crawlerVector) {
                crawler.start();
            }
        }
    }

    @Override
    public void run() {
        startCrawler();
        while (!complete) {
            monitor();
            ThreadUtil.waitMillis(monitorInterval);
        }
        logger.info("{} has finished", getName());
    }

    public boolean isComplete() {
        return complete;
    }

    public void printStatus() {
        for (Map.Entry<CrawlerFactory,Vector<Crawler>> entry : allCrawlers.entrySet()) {
            entry.getKey().printStatus();
        }
    }
}
