package com.smona.gpstrack.db;

import com.smona.gpstrack.db.table.Location;

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
public class LocationDecorate extends BaseDaoDecorate<Location> {

    @Override
    AbstractDao<Location, Void> getDao() {
        return DaoManager.getInstance().getDaoSession().getLocationDao();
    }

    @Override
    WhereCondition getWhereCondition(String condition) {
        return LocationDao.Properties.DeviceId.eq(condition);
    }
}
