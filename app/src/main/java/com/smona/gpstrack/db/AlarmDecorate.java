package com.smona.gpstrack.db;

import com.smona.gpstrack.db.table.Alarm;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

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
    WhereCondition getWhereCondition(String type, String condition) {
        if (CONDITION_LISTALL.equals(type)) {
            return AlarmDao.Properties.DevicePlatformId.eq(condition);
        } else if (CONDITION_LIMIT.equals(type)) {
            return AlarmDao.Properties.Status.eq(condition);
        } else {
            return AlarmDao.Properties.Id.eq(condition);
        }
    }

    public int listAll(String condition1, String condition2) {
        if (dao != null) {
            return dao.queryBuilder().where(AlarmDao.Properties.DevicePlatformId.eq(condition1)).where(AlarmDao.Properties.Status.eq(condition2)).list().size();
        } else {
            return 0;
        }
    }
}
