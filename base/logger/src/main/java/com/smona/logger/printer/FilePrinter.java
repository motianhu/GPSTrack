package com.smona.logger.printer;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.smona.logger.common.LogConfig;
import com.smona.logger.common.LogConstants;
import com.smona.logger.common.LogItem;
import com.smona.logger.common.LogLevel;
import com.smona.logger.common.LogUtils;

public class FilePrinter implements Printer {
    private static final String TAG = "FilePrinter";
    private static LogConfig sLogConfig;
    private static Context sContext;

    private static Worker sWorker;
    private static LogWriter sLogWriter;

    private String mCurrentFileName;
    private int mCurrentFilePart = 0;//超过大小，自动增加

    private String mSeedFileName;
    private String mLogDir;
    private List<LogItem> logCacheList;//日志缓存

    public int mLogFileKeepDays = LogConstants.LOG_FILE_KEEP_DAYS;
    public int mLogfileSizeThreshold = LogConstants.LOG_FILE_SIZE_THRESHOLD;

    @Override
    public void println(int logLevel, String tag, String msg) {
        operateWorker(new LogItem(logLevel, tag, msg));
    }

    /**
     * 将缓存日志写入到文件中，解决app被杀时，缓存日志丢失问题
     */
    public void flushCacheLogToFile() {
        synchronized (sWorker) {
            if (logCacheList != null && logCacheList.size() > 0) {
                if (checkTargetFilePrepared()) {
                    sLogWriter.writeCacheToFile(logCacheList);
                }
            }

        }
    }

    @Override
    public void printCrash(String tag, String message) {
        LogItem logItem = new LogItem(LogLevel.ERROR, tag, message);
        logItem.setCrashInfo(true);
        operateWorker(logItem);
    }

    //添加日志条目到缓存中,符合写的条件后，写到日志文件里面
    private void operateWorker(LogItem logItem) {

        //添加日志条目到缓存中
        sWorker.addLogItem(logItem);

        if (sWorker.isWorking() && (sWorker.checkWritePrepared() || sWorker.isCrashInfo)) {
            sWorker.notityWrite();
        }
    }

    @Override
    public LogConfig getLogConfig() {
        return sLogConfig;
    }

    @Override
    public void setLogConfig(LogConfig logConfig) {
        this.sLogConfig = logConfig;
    }

    public static class Builder {
        FilePrinter filePrinter;

        /**
         * 传入模块名，根据模块名生成文件名  如传入modulexx 则生成文件名类似  2017_08_23_modulexx_0.txt
         */
        public Builder(String seedFileName, Context context) {
            filePrinter.sContext = context;
            filePrinter = new FilePrinter();
            seedFileName(seedFileName);
        }

        /**
         * 根据传入的种子文件名生成新的文件
         */
        private Builder seedFileName(String seedFileName) {
            //获取当天最新文件名
            String latestFileName = LogUtils.getLatestFileName(LogConstants.DEFAULT_LOG_DIR, seedFileName);

            Log.d(TAG, "seedFileName latestFileName = " + latestFileName);

            if (!TextUtils.isEmpty(latestFileName)) { //当天有日志文件，提取filepart
                filePrinter.mCurrentFileName = latestFileName;
                int start = latestFileName.lastIndexOf("_") + 1;
                int end = latestFileName.lastIndexOf(".");
                if (end > start) {
                    try {
                        int part = Integer.parseInt(latestFileName.substring(start, end));
                        filePrinter.mCurrentFilePart = part;
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            } else {
                filePrinter.mCurrentFilePart = 0;
                filePrinter.mCurrentFileName = LogUtils.generateFileName(seedFileName);
            }

            filePrinter.mSeedFileName = seedFileName;

            //删除旧文件
            filePrinter.delOldFiles();

            Log.i(TAG, "mCurrentFileName = " + filePrinter.mCurrentFileName);
            return this;
        }

        public Builder logConfig(LogConfig logConfig) {
            filePrinter.sLogConfig = logConfig;
            return this;
        }

        private Builder logDir(String logDir) {
            if (!TextUtils.isEmpty(logDir)) {
                filePrinter.mLogDir = logDir;
            }
            return this;
        }

        public FilePrinter build() {
            sLogWriter = new LogWriter();
            sWorker.start();
            return filePrinter;
        }
    }

    /**
     * 异步处理文件输出
     */
    class Worker implements Runnable {
        long lastWriteTime;//上次写文件的时间
        volatile boolean isWorking;//是否正在工作
        List<LogItem> tempList;
        volatile boolean shouldStop;
        FilePrinter filePrinter;

        boolean isCrashInfo = false;

        public Worker(FilePrinter filePrinter) {
            this.filePrinter = filePrinter;
            logCacheList = new ArrayList<>();
            tempList = new ArrayList<>();
        }

        @Override
        public void run() {

            while (isWorking) {

                //条件满足，写入文件
                synchronized (sWorker) {
                    tempList.addAll(logCacheList);
                    logCacheList.clear();


                    filePrinter.doPrintln(tempList);//处理打印

                    isCrashInfo = false;

                    try {
                        sWorker.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


        }

        public synchronized void notityWrite() {
            sWorker.notify();
        }

        /**
         * 检查是否满足将缓存写到文件中
         */
        private boolean checkWritePrepared() {
            if (lastWriteTime == 0) {
                lastWriteTime = System.currentTimeMillis();
                return false;
            }

            if (logCacheList.size() >= LogConstants.LOG_WRITE_NUM_THRESHOLD || ((lastWriteTime + LogConstants.LOG_WRITE_TIME_INTERVAL <= System.currentTimeMillis()) && logCacheList.size() > 0)) {
                lastWriteTime = System.currentTimeMillis();
                return true;
            }
            return false;
        }

        /**
         * 添加日志条目
         */
        public synchronized void addLogItem(LogItem logItem) {
            logCacheList.add(logItem);
            if (logItem.isCrashInfo()) {
                isCrashInfo = true;
            }
        }

        /**
         * 新开线程执行
         */
        public synchronized void start() {
            if (isWorking) {
                return;
            }

            new Thread(this).start();
            isWorking = true;
        }

        public boolean isWorking() {
            return isWorking;
        }
    }

    /**
     * 负责具体操作文件  如打开 关闭 写入
     */
    static class LogWriter implements Serializable {
        File targetFile;
        BufferedWriter bufferedWriter;

        //判断是否已经打开
        public boolean isOpened() {
            return bufferedWriter != null;
        }

        public File getCurrentFile() {
            return targetFile;
        }

        //打开要写入的目标文件
        public boolean open(String logDir, String fileName) {
            File dirFile = new File(logDir);
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    Log.e(TAG, "open dirs fail!");
                    return false;
                }
            }
            targetFile = new File(dirFile, fileName);

            if (!targetFile.exists()) {
                try {
                    targetFile.createNewFile();
                } catch (IOException e) {
                    Log.e(TAG, "create new file fail：" + targetFile.getName() + "," + e.getMessage());
                    return false;
                }
            }

            try {
                if (bufferedWriter != null) {
                    close();
                }
                bufferedWriter = new BufferedWriter(new FileWriter(targetFile, true));

                //日志头 创建新文件时打印日志头
                writeLog(LogUtils.getLogHeadInfo(sContext));

            } catch (IOException e) {
                Log.e(TAG, "create writer fail" + e.toString());
                return false;
            }
            return true;
        }

        //写入条目
        public void writeLog(String logStr) {
            try {
                bufferedWriter.write(logStr);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }

        /**
         * 将缓存写入到文件
         */
        public synchronized void writeCacheToFile(List<LogItem> logCache) {
            try {
                for (LogItem logItem : logCache) {
                    bufferedWriter.write(logItem.toString());
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();
                logCache.clear();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }

        //关闭资源
        public boolean close() {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    return false;
                } finally {
                    bufferedWriter = null;
                    targetFile = null;
                }
            }
            return true;
        }
    }

    private FilePrinter() {
        mLogDir = LogConstants.DEFAULT_LOG_DIR;
        sWorker = new Worker(this);
    }

    /**
     * 不存在多线程问题
     * 处理输出到文件
     */
    private void doPrintln(List<LogItem> logList) {
        if (checkTargetFilePrepared()) {
            sLogWriter.writeCacheToFile(logList);
        } else {
            Log.d(TAG, "write file fail");
        }
    }

    /**
     * 检查目标文件是否存在 如果不存在则创建新文件
     * 产生新文件策略：
     * 1.根据日期生成文件名，检查文件名是否和当前文件名一致，如果不一致，表明到了新的一天，需要产生新文件；
     * 2.在上一步基础上，如果一致，检查当前文件大小是否超过限制，如果超过限制，则需要产生新文件；一致则不需要产生新文件
     */
    private boolean checkTargetFilePrepared() {

        Log.i(TAG, "checkTargetFilePrepared enter mSeedFileName = " + mSeedFileName);
        //检查是否写入到新的文件中；
        String newFileName = LogUtils.generateFileName(mSeedFileName, mCurrentFilePart);
        boolean needGenNewFile = false;

        if (!newFileName.equals(mCurrentFileName)) {//当前操作文件名和目标文件名不一致
            //需要将currentFilePart归零后，重新产生文件,否则新一天的文件名会和前一天的filePart关联起来
            mCurrentFilePart = 0;
            newFileName = LogUtils.generateFileName(mSeedFileName, mCurrentFilePart);
            needGenNewFile = true;
        } else {
            //检查文件大小是否超过阈值
            File currentFile = sLogWriter.getCurrentFile();

            if (currentFile != null && currentFile.length() >= mLogfileSizeThreshold) {//文件大小超过限制
                int start = currentFile.getName().lastIndexOf("_") + 1;
                int end = currentFile.getName().lastIndexOf(".");
                if (end > start) {
                    try {
                        int part = Integer.parseInt(currentFile.getName().substring(start, end));
                        newFileName = LogUtils.generateFileName(mSeedFileName, ++part);
                        mCurrentFilePart = part;
                        Log.d(TAG, "create new file when file exceed=" + newFileName);
                        needGenNewFile = true;
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

            } else {
                Log.d(TAG, "file size is not exceed");
            }
        }

        if (needGenNewFile) {//需要写入到新文件中
            //删除旧文件
            delOldFiles();

            if (sLogWriter.isOpened()) {
                sLogWriter.close();
            }
            mCurrentFileName = newFileName;

            Log.i(TAG, "checkTargetFilePrepared mCurrentFileName = " + mCurrentFileName);
            return sLogWriter.open(mLogDir, mCurrentFileName);
        } else {//不需要写入到新文件
            if (!sLogWriter.isOpened()) {
                return sLogWriter.open(mLogDir, mCurrentFileName);
            }

            Log.i(TAG, "leave mCurrentFileName = " + mCurrentFileName);
            return true;
        }

    }

    /**
     * 删除旧文件
     */
    private void delOldFiles() {
        File dir = new File(LogConstants.DEFAULT_LOG_DIR);

        if (!dir.exists() || !dir.isDirectory()) {
            Log.e(TAG, dir.getAbsolutePath() + "dir is not exist");
            return;
        }

        //得到n天前的日期串
        final String baseDelName = LogUtils.getNthDayBeforeStr(mLogFileKeepDays);

        Log.d(TAG, "del file name：" + baseDelName);

        String reg = "(.*?)(\\d{4}_\\d{2}_\\d{2}).*";
        final Pattern pattern = Pattern.compile(reg);

        //找到要删除的目标文件
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String fileName = file.getName();

                Matcher matcher = pattern.matcher(fileName);
                if (matcher.matches()) {
                    String group = matcher.group(2);
                    Log.d(TAG, "group=" + group);

                    return baseDelName.compareTo(group) >= 0;
                }


                return false;
            }
        });

        if (null == files || 0 == files.length) {
            return;
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }

        Log.d(TAG, "delOldFiles leave");

    }

}
