package com.luoheng.crawler.scheduler;

import com.luoheng.crawler.model.Task;

public interface Scheduler {
    void push(Task task);

    Task pop();
}
