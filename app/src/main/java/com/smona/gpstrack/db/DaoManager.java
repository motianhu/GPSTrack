package com.smona.gpstrack.db;

import com.smona.gpstrack.util.AppContext;
import org.greenrobot.greendao.query.QueryBuilder;

/**
 * DB
 */
public class DaoManager {
    //创建数据库的名字
    private static final String DB_NAME = "gpstrack.db";
    //多线程中要被共享的使用volatile关键字修饰  GreenDao管理类
    private volatile static DaoManager mInstance;
    //它里边实际上是保存数据库的对象
    private static DaoMaster mDaoMaster;
    //创建数据库的工具
    private DaoMaster.DevOpenHelper mHelper;
    //管理gen里生成的所有的Dao对象里边带有基本的增删改查的方法
    private static DaoSession mDaoSession;

    /**
     * 单例模式获得操作数据库对象
     *
     * @return
     */
    public static DaoManager getInstance() {
        if (mInstance == null) {
            synchronized (DaoManager.class) {
                if (mInstance == null) {
                    mInstance = new DaoManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 判断是否有存在数据库，如果没有则创建
     *
     * @return
     */
    private DaoMaster getDaoMaster() {
        if (mDaoMaster == null) {
            mHelper = new DaoMaster.DevOpenHelper(AppContext.getAppContext(), DB_NAME, null);
            mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        }
        return mDaoMaster;
    }


    /**
     * 是否打印 数据库的日志信息 默认不开启
     */
    public void setDebug(Boolean isDebug) {
        if (isDebug) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        } else {
            QueryBuilder.LOG_SQL = false;
            QueryBuilder.LOG_VALUES = false;
        }

    }

    /**
     * 完成对数据库的添加、删除、修改、查询操作，
     *
     * @return
     */
    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            if (mDaoMaster == null) {
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }

    public void closeHelper() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

    public void closeDaoSession() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }
}
