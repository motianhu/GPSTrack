package com.smona.zxing.common.zxing.core;

import android.graphics.PointF;

public class ScanResult {
    public String result;
    public PointF[] resultPoints;

    public ScanResult(String result) {
        this.result = result;
    }

    public ScanResult(String result, PointF[] resultPoints) {
        this.result = result;
        this.resultPoints = resultPoints;
    }
}
