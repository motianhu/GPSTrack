package com.smona.gpstrack.db;

import com.smona.gpstrack.db.table.Fence;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.WhereCondition;

/**
 * DB中的电子围栏操作类
 * @param <T>
 */
public class FenceDecorate<T extends Fence> extends BaseDaoDecorate {
    @Override
    AbstractDao getDao() {
        return DaoManager.getInstance().getDaoSession().getFenceDao();
    }

    @Override
    WhereCondition getWhereCondition(String type, String condition) {
        if (CONDITION_LISTALL.equals(type)) {
            return FenceDao.Properties.Id.eq(condition);
        } else {
            return FenceDao.Properties.Id.eq(condition);
        }
    }
}
