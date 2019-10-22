package com.smona.gpstrack.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

public class WorkHandlerManager {

    private DeferredHandler mHandler = new DeferredHandler();
    private final HandlerThread workerThread = new HandlerThread("db_thread");
    private final Handler sWorker;

    private WorkHandlerManager() {
        workerThread.start();
        sWorker = new Handler(workerThread.getLooper());
    }

    private static class WorkHandlerHolder {
        private static WorkHandlerManager paramCenter = new WorkHandlerManager();
    }

    public static WorkHandlerManager getInstance() {
        return WorkHandlerHolder.paramCenter;
    }


    public void runOnMainThread(Runnable r) {
        runOnMainThread(r, 0);
    }

    private void runOnMainThread(Runnable r, int type) {
        if (workerThread.getThreadId() == Process.myTid()) {
            // If we are on the worker thread, post onto the main handler
            mHandler.post(r);
        } else {
            r.run();
        }
    }

    public void runOnWorkerThread(Runnable r) {
        if (workerThread.getThreadId() == Process.myTid()) {
            r.run();
        } else {
            // If we are not on the worker thread, then post to the worker handler
            sWorker.post(r);
        }
    }
}
