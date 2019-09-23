package com.smona.gpstrack.db;

import com.smona.gpstrack.db.table.Alarm;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.WhereCondition;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 8:04 AM
 */
//操作此类接口方法，必须在子线程
public class AlarmDecorate extends BaseDaoDecorate<Alarm> {

    @Override
    AbstractDao<Alarm, Void> getDao() {
        return DaoManager.getInstance().getDaoSession().getAlarmDao();
    }

    @Override
    WhereCondition getWhereCondition(String condition) {
        return AlarmDao.Properties.Id.eq(condition);
    }
}
