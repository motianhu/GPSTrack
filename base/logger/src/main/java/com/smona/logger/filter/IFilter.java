package com.smona.logger.filter;

public interface IFilter {

    /**
     * 日志过滤器  根据tag来过滤
     * 返回true表示需要输出，false表示不输出
     */
    public boolean accept(String tag);

}
