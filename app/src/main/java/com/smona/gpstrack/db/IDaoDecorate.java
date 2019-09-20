package com.smona.gpstrack.db;

import java.util.List;

/**
 * DAO基类
 * @param <T>
 */
public interface IDaoDecorate<T> {
    void add(T bean);
    void delete(T bean);
    T query(String condition);
    void update(T bean);

    void addAll(List<T> beanList);
    void deleteAll();
    List<T> listAll();

    void delete(List<T> beanList);
    long count();
}
