package cn.zju.id21632120.dbutil;

import android.provider.BaseColumns;

/**
 * 数据库辅助类
 * Created by Wangli on 2017/6/11.
 */

public class StatusContract {

    public static final String DB_NAME = "timeline.db";
    public static final String TABLE = "status";
    public static final String DEFAULT_SORT = Column.CREATED_AT + " DESC";
    public static final int DB_VERSION = 1;

    public static final String NEW_STATUES = "cn.zju.id21632120.action.NEW_STATUES";

    public class Column {
        public static  final String ID = BaseColumns._ID;
        public static  final String USER = "user";
        public static  final String MESSAGE = "message";
        public static  final String CREATED_AT = "created_at";
    }

}
