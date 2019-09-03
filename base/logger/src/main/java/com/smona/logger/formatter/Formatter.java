package com.smona.logger.formatter;

public interface Formatter<T> {

    /**
     * 将data 格式化成字符串
     */
    public String format(T data,boolean printOneline);

}
