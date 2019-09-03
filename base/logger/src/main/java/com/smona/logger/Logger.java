package com.smona.logger;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.smona.logger.common.LogConfig;
import com.smona.logger.common.LogConstants;
import com.smona.logger.common.LogItem;
import com.smona.logger.common.LogLevel;
import com.smona.logger.common.LogUtils;
import com.smona.logger.printer.ConsolePrinter;
import com.smona.logger.printer.FilePrinter;
import com.smona.logger.printer.Printer;
import com.smona.logger.printer.PrinterSet;

public class Logger {
    public static LogConfig sLogConfig;//日志配置
    public static Context sContext;
    private static final String TAG = "Log";
    private static PrinterSet sPrinterSet;//输出渠道
    private static boolean  sIsInitialized = false;//检查是否初始化
    private static BlockingQueue<LogItem> sLogItems = null;
    private static volatile boolean needOnlineDebug = false;
    private static ScheduledExecutorService sScheduledThreadPool;

    /**
     * 初始化，默认配置，默认输出渠道为控制台
     */
    public static void init(Context context){
        init(context,null);
    }

    /**
     * 初始化，默认配置，默认输出渠道为控制台
     */
    public static void init(Context context,String tag) {
        //全局配置
        LogConfig globalConfig = new LogConfig.Builder()
                .tag(tag)//全局tag,不指定tag,则使用该tag
                .logLevel(LogLevel.ALL)//控制日志输出级别
                .st(true)//是否打印方法调用栈信息（StackTrace）
                .pi(false)//是否打印进程信息 ProcessInfo
                .ti(false)//是否打印线程信息  ThreadInfo
                .formatJson(false)//是否格式化json数据
                .printOneline(false)
                .build();

        //文件输出配置
        LogConfig filePrinterConfig = new LogConfig.Builder()
                .tag(tag)//全局tag,不指定tag,则使用该tag
                .logLevel(LogLevel.ALL)//控制日志输出级别
                .st(false)//是否打印方法调用栈信息（StackTrace）
                .pi(true)//是否打印进程信息 ProcessInfo
                .ti(true)//是否打印线程信息  ThreadInfo
                .printOneline(true)//打印成一行
                .formatJson(true)//是否格式化json数据
                .build();

        FilePrinter filePrinter = new FilePrinter.Builder(LogConstants.DEFAULT_LOG_TAG, context).logConfig(filePrinterConfig).build();//输出到文件

        initData(context, globalConfig, new ConsolePrinter(), filePrinter);

    }

    /**
     * 自定义初始化
     *
     * @param config  自定义Logcat配置
     * @param printer 自定义日志输出渠道
     */
    private static void initData(Context context, LogConfig config, Printer... printer) {
        if (sIsInitialized) {
            Logger.i(TAG,"initData has finish");
            return;
        }

        if(null == config){
            sLogConfig = new LogConfig.Builder().build();
        }else {
            sLogConfig = config;
        }

        Logger.sContext = context;
        Logger.sPrinterSet = new PrinterSet(printer);
        sIsInitialized = true;

        //是否捕获crash信息
        initCrashCofig();

        //初始化检测anr
        initDetectAnr();

        Logger.i(TAG, "Logger init finish");
    }

    /**
     * 每隔1s去检测ANR是否发生
     */
    private static void initDetectAnr() {
        sScheduledThreadPool = Executors.newScheduledThreadPool(1);
        final ActivityManager activityManager = (ActivityManager) sContext.getSystemService(Context.ACTIVITY_SERVICE);

        sScheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                    List<ActivityManager.ProcessErrorStateInfo> processList = activityManager.getProcessesInErrorState();
                    if (processList != null) {
                        Iterator<ActivityManager.ProcessErrorStateInfo> iterator = processList.iterator();
                        while (iterator.hasNext()) {
                            ActivityManager.ProcessErrorStateInfo processInfo = iterator.next();
                            //导出trace文件到目录下
                            String traceFileName = LogUtils.exportTraceFile();
                            //日志文件中打印anr信息
                            String anrInfo = LogUtils.getAnrInfo(processInfo, traceFileName, sContext);
                            sPrinterSet.printCrash(sLogConfig.mTag, anrInfo);
                            LogUtils.exportCrashFile(sLogConfig.mTag, anrInfo);

                            sScheduledThreadPool.shutdownNow();
                        }
                    }

            }

        }, 0, 1, TimeUnit.SECONDS);

    }

    /**
     * 初始化捕获crash信息
     */
    private static void initCrashCofig() {
        if (sLogConfig.mPrintCrash) {
            //捕获全局信息
            final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    //输出crash信息
                    sPrinterSet.printCrash(sLogConfig.mTag, LogUtils.getCrashInfo(sContext, t, e));
                    LogUtils.exportCrashFile(sLogConfig.mTag, LogUtils.getCrashInfo(sContext, t, e));
                    //调用默认处理
                    if (defaultUncaughtExceptionHandler != null) {
                        defaultUncaughtExceptionHandler.uncaughtException(t, e);
                    }
                }
            });

        } else {
            // 不捕获全局信息
            Logger.w(TAG, "do not catch the global info");
        }
    }

    private static boolean checkInitialized() {
        return sIsInitialized;
    }

    public static void v(String msg) {
        v(null, msg);
    }

    public static void v(String tag, String msg) {
        if(!checkInitialized()){
           return;
        }
        v(tag, msg, true);
    }

    public static void v(String tag, String msg, boolean printToRemote) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.VERBOSE, tag, msg, printToRemote);
    }

    public static void v(String msg, Throwable throwable) {
        v(null, msg, throwable);
    }

    public static void v(String tag, String msg, Throwable throwable) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.VERBOSE, tag, msg, throwable, true);
    }

    /**
     * 自定义打印日志信息
     *
     * @param tag              标签
     * @param msg              信息
     * @param printThreadInfo  是否打印线程信息
     * @param printProcessInfo 是否打印进程信息
     * @param printStackTrace  是否答应调用栈信息
     */
    public static void v(String tag, String msg, boolean printThreadInfo, boolean printProcessInfo, boolean printStackTrace) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.VERBOSE, tag, msg, printThreadInfo, printProcessInfo, printStackTrace, true);
    }

    public static void i(String msg) {
        i(null, msg);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, true);
    }

    public static void i(String tag, String msg, boolean printToRemote) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.INFO, tag, msg, printToRemote);
    }

    public static void i(String msg, Throwable throwable) {
        i(null, msg, throwable);
    }

    public static void i(String tag, String msg, Throwable throwable) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.INFO, tag, msg, throwable, true);
    }


    /**
     * 自定义打印日志信息
     *
     * @param tag              标签
     * @param msg              信息
     * @param printThreadInfo  是否打印线程信息
     * @param printProcessInfo 是否打印进程信息
     * @param printStackTrace  是否答应调用栈信息
     */
    public static void i(String tag, String msg, boolean printThreadInfo, boolean printProcessInfo, boolean printStackTrace) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.INFO, tag, msg, printThreadInfo, printProcessInfo, printStackTrace, true);
    }

    public static void d(String msg) {
        d(null, msg);
    }

    public static void d(String tag, String msg) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.DEBUG, tag, msg, true);
    }

    public static void d(String tag, String msg, boolean printToRemote) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.DEBUG, tag, msg, printToRemote);
    }

    public static void d(String msg, Throwable throwable) {
        d(null, msg, throwable);
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if(!checkInitialized()){
            return;
        }
        d(tag, msg, throwable, true);
    }

    public static void d(String tag, String msg, Throwable throwable, boolean printToRemote) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.DEBUG, tag, msg, throwable, printToRemote);
    }

    /**
     * 自定义打印日志信息
     *
     * @param tag              标签
     * @param msg              信息
     * @param printThreadInfo  是否打印线程信息
     * @param printProcessInfo 是否打印进程信息
     * @param printStackTrace  是否答应调用栈信息
     */
    public static void d(String tag, String msg, boolean printThreadInfo, boolean printProcessInfo, boolean printStackTrace) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.DEBUG, tag, msg, printThreadInfo, printProcessInfo, printStackTrace, true);
    }

    public static void w(String msg) {
        w(null, msg);
    }

    public static void w(String tag, String msg) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.WARN, tag, msg, true);
    }

    public static void w(String tag, String msg, boolean printToRemote) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.WARN, tag, msg, printToRemote);
    }

    public static void w(String msg, Throwable throwable) {
        w(null, msg, throwable);
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if(!checkInitialized()){
            return;
        }
        w(tag, msg, throwable, true);
    }

    public static void w(String tag, String msg, Throwable throwable, boolean printToRemote) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.WARN, tag, msg, throwable, printToRemote);
    }

    /**
     * 自定义打印日志信息
     *
     * @param tag              标签
     * @param msg              信息
     * @param printThreadInfo  是否打印线程信息
     * @param printProcessInfo 是否打印进程信息
     * @param printStackTrace  是否答应调用栈信息
     */
    public static void w(String tag, String msg, boolean printThreadInfo, boolean printProcessInfo, boolean printStackTrace) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.WARN, tag, msg, printThreadInfo, printProcessInfo, printStackTrace, true);
    }

    public static void e(String msg) {
        e(null, msg);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, true);
    }

    public static void e(String tag, String msg, boolean printToRemote) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.ERROR, tag, msg, printToRemote);
    }

    public static void e(String msg, Throwable throwable) {
        e(null, msg, throwable, true);
    }

    public static void e(String msg, Throwable throwable, boolean printToRemote) {
        e(null, msg, throwable, printToRemote);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        e(tag, msg, throwable, true);
    }

    public static void e(String tag, String msg, Throwable throwable, boolean printToRemote) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.ERROR, tag, msg, throwable, printToRemote);
    }

    /**
     * 自定义打印日志信息
     *
     * @param tag              标签
     * @param msg              信息
     * @param printThreadInfo  是否打印线程信息
     * @param printProcessInfo 是否打印进程信息
     * @param printStackTrace  是否答应调用栈信息
     */
    public static void e(String tag, String msg, boolean printThreadInfo, boolean printProcessInfo, boolean printStackTrace, boolean printToRemote) {
        if(!checkInitialized()){
            return;
        }
        handlePrint(LogLevel.ERROR, tag, msg, printThreadInfo, printProcessInfo, printStackTrace, printToRemote);
    }

    /**
     * 将缓存的日志立即写入到文件中，主要解决app被杀掉时日志丢失问题
     */
    public static void flushCacheLogToFile() {
        if(!checkInitialized()){
            return;
        }
        FilePrinter filePrinter = sPrinterSet.getFilePrinter();

        if (filePrinter != null) {
            filePrinter.flushCacheLogToFile();
        }
    }

    /**
     * 处理日志的输出
     */
    private static void handlePrint(int logLevel, String tag, String msg, boolean printToRemote) {
        //需要输出
        //tag = TextUtils.isEmpty(tag) ? sLogConfig.mTag : tag;

        if(null == tag){
            tag = LogUtils.getTAGPrefix(sContext);
        }else{
            tag = LogUtils.getTAGPrefix(sContext) + "::"+tag;
        }

        if (printToRemote) {
            putLogItem(logLevel, tag, msg);
        }

        sPrinterSet.handlePrintln(logLevel, tag, msg);
    }

    private static void handlePrint(int logLevel, String tag, String msg, boolean printTreadInfo, boolean printProcessInfo, boolean printStackTrace, boolean printToRemote) {
        tag = TextUtils.isEmpty(tag) ? sLogConfig.mTag : tag;

        if (printToRemote) {
            putLogItem(logLevel, tag, msg);
        }

        sPrinterSet.handlePrintln(logLevel, tag, msg, printTreadInfo, printProcessInfo, printStackTrace);
    }

    /**
     * 处理日志的输出
     */
    private static void handlePrint(int logLevel, String tag, String msg, Throwable tr, boolean printToRemote) {
        String trMsg = LogUtils.getThrowableInfo(tr);
        msg = msg + "\n" + trMsg;

        handlePrint(logLevel, tag, msg, printToRemote);
    }

    /**
     * 开启实时日志
     */
    public static void startOnlineDebug() {
        if (!needOnlineDebug) {
            needOnlineDebug = true;
            sLogItems = new LinkedBlockingQueue<>();
        }
    }

    /**
     * 停止实时日志
     */
    public static void stopOnlineDebug() {
        needOnlineDebug = false;
        if (sLogItems != null) {
            sLogItems.clear();
            sLogItems = null;
        }
    }

    private static void putLogItem(int loglevel, String tag, String msg) {
        //检查是否开启实施日志
        if (!needOnlineDebug || sLogItems == null) {
            return;
        }

        try {
            StackTraceElement stackTraceElement = LogUtils.getStackTrace();

            StringBuilder sb=new StringBuilder("(")
                    .append(stackTraceElement.getLineNumber())
                    .append(")");

            String simpleStackTrace = sb.toString();

//            sLogItems.put(new LogItem(loglevel,tag,msg,simpleStackTrace));
            sLogItems.offer(new LogItem(loglevel, msg, tag, simpleStackTrace));

        } catch (Exception e) {
            Logger.e(TAG,e.toString());
        }
    }

    public static LogItem getLogItem() {
        if (sLogItems == null || !needOnlineDebug) {
            return null;
        }

        try {
            if (sLogItems != null) {
                return sLogItems.take();
            }
            return null;
        } catch (InterruptedException e) {
            Logger.e(TAG,e.toString());
            return null;
        }
    }

    public static boolean setDebugLevel(int level){
        boolean success = false;
        FilePrinter filePrinter = sPrinterSet.getFilePrinter();

        if(null != filePrinter){
            filePrinter.getLogConfig().mLogLevel = level;
            sLogConfig.mLogLevel = level;
            success = true;
        }

        return  success;
    }

    public static boolean setDebugEnable(boolean enable){
        boolean success = false;
        FilePrinter filePrinter = sPrinterSet.getFilePrinter();

        if(null != filePrinter){
            if(true == enable){
                filePrinter.getLogConfig().mLogLevel = LogLevel.ALL;
                sLogConfig.mLogLevel = LogLevel.ALL;
            }else{
                filePrinter.getLogConfig().mLogLevel = LogLevel.OFF;
                sLogConfig.mLogLevel = LogLevel.OFF;
            }
            success = true;
        }

        return  success;
    }

    public static boolean setLogFileKeepDay(int day){
        boolean success = false;
        FilePrinter filePrinter = sPrinterSet.getFilePrinter();

        if(null != filePrinter){
            filePrinter.mLogFileKeepDays = day;
            success = true;
        }

        return  success;
    }

    public static boolean setLogFileSizeThreshold(int threshold){
        boolean success = false;
        FilePrinter filePrinter = sPrinterSet.getFilePrinter();

        if(null != filePrinter){
            filePrinter.mLogfileSizeThreshold = threshold;
            success = true;
        }

        return  success;
    }
}
