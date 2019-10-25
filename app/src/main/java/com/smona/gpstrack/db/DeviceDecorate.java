package com.smona.gpstrack.db;

import android.text.TextUtils;
import android.widget.TextView;

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

    public List<Device> listDevice(String status) {
        if(TextUtils.isEmpty(status)) {
            return listAll();
        } else {
            return dao.queryBuilder().where(DeviceDao.Properties.Status.eq(status)).list();
        }
    }
}
