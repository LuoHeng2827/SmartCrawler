package com.luoheng.crawler.bean;

import com.luoheng.crawler.model.Bean;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-24 15:20
 **/
public interface DataSet {
    Bean popData();

    void pushData();
}
