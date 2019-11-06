package com.smona.gpstrack.db;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 8:27 AM
 */
public abstract class BaseDaoDecorate<T> implements IDaoDecorate<T> {

    protected final static String CONDITION_QUERY = "query";
    protected final static String CONDITION_LIMIT = "limit";
    protected final static String CONDITION_LISTALL = "listAll";

    protected AbstractDao<T, Void> dao = getDao();

    abstract AbstractDao<T, Void> getDao();

    abstract WhereCondition getWhereCondition(String type, String condition);

    @Override
    public void add(T bean) {
        if (dao != null && bean != null) {
            dao.insert(bean);
        }
    }

    @Override
    public void delete(T bean) {
        if (dao != null && bean != null) {
            dao.delete(bean);
        }
    }

    @Override
    public T query(String condition) {
        return dao.queryBuilder().where(getWhereCondition(CONDITION_QUERY, condition)).unique();
    }

    @Override
    public void update(T bean) {
        if (dao != null && bean != null) {
            dao.update(bean);
        }
    }

    @Override
    public void addAll(List<T> beanList) {
        if (dao != null && beanList != null) {
            dao.insertOrReplaceInTx(beanList);
        }
    }

    @Override
    public void deleteAll() {
        if (dao != null) {
            dao.deleteAll();
        }
    }


    @Override
    public List<T> listAll(String condition) {
        if (dao != null) {
            return dao.queryBuilder().where(getWhereCondition(CONDITION_LISTALL, condition)).list();
        } else {
            return null;
        }
    }

    @Override
    public List<T> listAll() {
        if (dao != null) {
            return dao.loadAll();
        } else {
            return null;
        }
    }

    @Override
    public void delete(List<T> beanList) {
        if (dao != null) {
            dao.deleteInTx(beanList);
        }
    }

    @Override
    public long count() {
        if (dao != null) {
            return dao.count();
        } else {
            return 0;
        }
    }
}
