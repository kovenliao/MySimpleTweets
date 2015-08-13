package com.kl.apps.MySimpleTweets.data;

/**
 * Created by kovenliao on 8/13/15.
 */
public class QueryReq {
    private static final int DEFAULT_COUNT = 25;

    private int count;
    private int sinceId;

    public QueryReq() {
        this.count = DEFAULT_COUNT;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSinceId() {
        return sinceId;
    }

    public void setSinceId(int sinceId) {
        this.sinceId = sinceId;
    }

    public void setSinceIdByPage(int page) {
        this.sinceId = count * page + 1;
    }
}
