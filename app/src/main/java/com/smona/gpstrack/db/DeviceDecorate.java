package com.smona.gpstrack.db;

import com.smona.gpstrack.db.table.Device;

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
public class DeviceDecorate<T extends Device> extends BaseDaoDecorate {

    @Override
    AbstractDao<Device, Void> getDao() {
        return DaoManager.getInstance().getDaoSession().getDeviceDao();
    }

    @Override
    WhereCondition getWhereCondition(String type, String condition) {
        if (CONDITION_LISTALL.equals(type)) {
            return DeviceDao.Properties.Id.eq(condition);
        } else {
            return DeviceDao.Properties.Id.eq(condition);
        }
    }

    public List<Device> searchDevice(String name) {
        return dao.queryBuilder().where(DeviceDao.Properties.Name.like("%" + name+ "%")).list();
    }
}
