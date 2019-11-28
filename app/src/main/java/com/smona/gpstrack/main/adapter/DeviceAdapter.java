package com.smona.gpstrack.main.adapter;

import android.support.v4.app.Fragment;

import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.main.holder.DeviceHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 1:04 PM
 */
public class DeviceAdapter extends XBaseAdapter<Device, DeviceHolder> {

    private Fragment fragment;
    public DeviceAdapter(Fragment fragment, int layoutResId) {
        super(layoutResId);
        this.fragment = fragment;
    }

    @Override
    protected void convert(DeviceHolder holder, Device item, int pos) {
        holder.bindViews(fragment, item);
    }

    public void removeDevice(String delDeviceId) {
        Device device;
        for (int i = 0; i < mDataList.size(); i++) {
            device = mDataList.get(i);
            if (device.getId().equalsIgnoreCase(delDeviceId)) {
                mDataList.remove(i);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void addDevice(Device addDevice) {
        mDataList.add(0, addDevice);
        notifyDataSetChanged();
    }

    public void updateDevice(Device updateDevice) {
        Device device;
        for (int i = 0; i < mDataList.size(); i++) {
            device = mDataList.get(i);
            if (device.getId().equalsIgnoreCase(updateDevice.getId())) {
                device.copyValue(updateDevice);
                notifyDataSetChanged();
                break;
            }
        }
    }

}
