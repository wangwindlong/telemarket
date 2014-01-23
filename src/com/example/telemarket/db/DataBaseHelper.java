package com.example.telemarket.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.telemarket.AppConfig;

import java.util.Iterator;
import java.util.Map;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static DataBaseHelper dbInstance = null;
    private static String DATA_FILE = AppConfig.DATA_FILE_PATH + "/" + AppConfig.DATABASE_FILENAME;
    private static SQLiteDatabase sqliteDb = null;
    private Context mContext;
    private static int VERSION = 3;

    /**
     * 显示调用父类的方法
     *
     * @param context
     */
    public DataBaseHelper(Context context) {
        //context上下文       name数据库名称      factory游标工厂   version数据库文件的版本号
        super(context, DATA_FILE, null, VERSION);
        //如果数据库被创建 那么数据库文件会被保存在当前应用所在的<包>/databases
        this.mContext = context;
    }

    /**
     * 单例模式 返回实例
     *
     * @param context
     * @return
     */
    public synchronized static DataBaseHelper getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new DataBaseHelper(context);
        }
        return dbInstance;
    }

    public DataBaseHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
        this.VERSION = version;
    }

    public void close() {
        if (sqliteDb != null) {
            sqliteDb.close();
        }
    }

//    /*创建简单数据表*/
//    public int createTable(Class<?> entityClass) {
//        StringBuffer sqlSb = new StringBuffer();
//        sqlSb.append("CREATE TABLE IF NOT EXISTS ");
//        sqlSb.append("[" + entityClass.getSimpleName() + "]");
//        sqlSb.append("(");
//        HashMap data = BeanTools.getAllFiled(entityClass);
//        String[] fieldName = (String[]) data.get("fieldName");
//        Class[] fieldType = (Class[]) data.get("fieldType");
//        try {
//            for (int i = 0; i < fieldName.length; i++) {
//                if (i != 0) {
//                    sqlSb.append(",");
//                }
//                if (fieldName[i].equals("_id")) {
//                    // 主键
//                    sqlSb.append("[" + fieldName[i] + "]" + " "
//                            + fieldType[i].getSimpleName()
//                            + " PRIMARY KEY AUTOINCREMENT");
//                } else {
//                    sqlSb.append("[" + fieldName[i] + "]" + " "
//                            + fieldType[i].getSimpleName());
//                }
//            }
//        } catch (Exception e) {
//
//        }
//        sqlSb.append(")");
//        sqliteDb.execSQL(sqlSb.toString());
//        return 0;
//    }

    /**
     * 创建简单数据表*
     */
    public int createTable(String tableName, Map<String, String> map) {
        StringBuffer sqlSb = new StringBuffer();
        sqlSb.append("CREATE TABLE IF NOT EXISTS ");
        sqlSb.append("[" + tableName + "]");
        sqlSb.append("(");
        Iterator iter = map.entrySet().iterator();
        int i = 0;
        while (iter.hasNext()) {
            if (i != 0) {
                sqlSb.append(",");
            }
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            if (i == 0) {
                sqlSb.append("[" + key + "]" + " " + val);
            } else {
                sqlSb.append("[" + key + "]" + " " + val);
            }
            i++;
        }
        sqlSb.append(")");
        String createTableSql = sqlSb.toString();
        sqliteDb.execSQL(sqlSb.toString());
        return 0;
    }

    /**
     * 删除数据库
     *
     * @param context
     * @return
     */
    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DATA_FILE);
    }

    /**
     * 删除指定记录
     */
    public int delete(String tableName, String whereClause) {
        return sqliteDb.delete(tableName, whereClause, null);
    }

    public void exec(String sql) {
        sqliteDb.execSQL(sql);
    }

    /**
     * 添加记录*
     */
    public long insert(String tableName, ContentValues cv) {
        long id = sqliteDb.insert(tableName, null, cv);
        return id;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    //该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        // db.execSQL("");
    }

    /**
     * 查询*
     */
    public Cursor read(String sqlstr) {
        Cursor cur = sqliteDb.rawQuery(sqlstr, null);
        return cur;
    }

    public Cursor readOne(String sqlstr) {
        Cursor cur = sqliteDb.rawQuery(sqlstr + " limit 1 offset 0", null);
        return cur;
    }

    /**
     * 判断某张表是否存在
     *
     * @param tableName 表名
     * @return
     */
    public boolean tableIsExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"
                    + tableName.trim() + "' ";
            cursor = sqliteDb.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
        } finally {
            if (null != cursor) {
                cursor.close();
                cursor = null;
            }
        }
        return result;
    }

    /**
     * 更新记录
     *
     * @param tableName   表名
     * @param cv          值
     * @param whereClause 条件字段
     * @param whereClause 条件参数
     * @return
     */
    public int update(String tableName, ContentValues cv, String whereClause) {
        return sqliteDb.update(tableName, cv, whereClause, null);
    }

    public interface DataType {
        public final String DATETIME = "DATETIME";
        public final String IMAGE = "IMAGE";
        public final String INT = "INT";
        public final String INTEGER = "INTEGER";
        public final String TEXT = "TEXT";
        public final String VARCHAR = "VARCHAR";
    }

    /**
     * 当数据库文件的版本号发生改变时调用
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void closeAll(Cursor cursor, SQLiteDatabase sqliteDatabase, DataBaseHelper helper) {
        try {
            if (cursor != null) {
                cursor.close();
            }
            if (sqliteDatabase != null) {
                sqliteDatabase.close();
            }
            if (helper != null) {
                helper = null;
            }
        } catch (Exception e) {
        }
    }

    //判断SD卡是否存在
    private static boolean existSDcard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

}
