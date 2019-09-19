package com.smona.gpstrack.common.bean;

import com.smona.gpstrack.common.DeviceProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 11:07 AM
 */
public class OSBaseBean {
    private String model = DeviceProfile.getModel();
    private String manufacturer = DeviceProfile.getManufacturer();
    private String os = DeviceProfile.getOS();
    private String osVersion = DeviceProfile.getOSVersion();
    private List<String> imeis = new ArrayList<>();

    public OSBaseBean() {
        imeis.add(DeviceProfile.getIMEI());
    }
}
