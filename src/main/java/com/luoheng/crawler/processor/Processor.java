package com.luoheng.crawler.processor;

import com.luoheng.crawler.model.Bean;
import com.luoheng.crawler.model.Page;
import com.luoheng.crawler.model.Task;

/**
 * @author ：LuoHeng
 * @date ：Created in 2019/10/19 12:24
 * @description：
 */
public interface Processor {
    boolean isAccept(Task task);

    Page requestPage(Task task);

    Bean process(Page page);

    void handleResult(Bean bean);
}
