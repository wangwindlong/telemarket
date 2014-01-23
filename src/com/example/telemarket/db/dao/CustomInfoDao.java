package com.example.telemarket.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.telemarket.bean.CustomInfo;
import com.example.telemarket.db.DataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-21
 * Time: 上午9:16
 * To change this template use File | Settings | File Templates.
 */
public class CustomInfoDao {
    Context context;

    public CustomInfoDao(Context context) {
        this.context = context;
    }

    public List<CustomInfo> getCustomInfo() {
        DataBaseHelper helper = null;
        SQLiteDatabase sqliteDatabase = null;
        Cursor cursor = null;
        List<CustomInfo> customInfoList = new ArrayList<CustomInfo>();
        try {
            helper = DataBaseHelper.getInstance(context);
            sqliteDatabase = helper.getReadableDatabase();
            String[] columnArray = new String[]{"c_name", "c_position", "c_id", "c_company","c_industry","c_group","c_telephone"};
            cursor = sqliteDatabase.query("custom_group", columnArray, null, null, null, null, null);
            customInfoList = getCustom(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            helper.closeAll(cursor, sqliteDatabase, helper);
        }
        return customInfoList;
    }

    public List<Map<String, String>> getCustomGroup() {
        DataBaseHelper helper = null;
        SQLiteDatabase sqliteDatabase = null;
        Cursor cursor = null;
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();

        try {
            helper = DataBaseHelper.getInstance(context);
            sqliteDatabase = helper.getReadableDatabase();
            cursor = sqliteDatabase.query("cgroup_config", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                Map<String, String> result = new HashMap<String, String>();
                result.put("cgroup_type", cursor.getString(cursor.getColumnIndex("cgroup_type")));
                result.put("cgroup_name", cursor.getString(cursor.getColumnIndex("cgroup_name")));
                result.put("cgroup_id", cursor.getString(cursor.getColumnIndex("cgroup_id")));
                results.add(result);
            }
        } catch (Exception e) {
        } finally {
            helper.closeAll(cursor, sqliteDatabase, helper);
        }
        return results;
    }

    public int updateSql(String sql) {
        DataBaseHelper dbHelper = null;
        SQLiteDatabase sqliteDatabase = null;
        try {
            dbHelper = DataBaseHelper.getInstance(context);
            sqliteDatabase = dbHelper.getWritableDatabase();
            sqliteDatabase.beginTransaction();
            // 调用insert方法，就可以将数据插入到数据库当中   第一个参数:表名称
            // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
            // 第三个参数：ContentValues对象
            sqliteDatabase.execSQL(sql);
            sqliteDatabase.setTransactionSuccessful();
            sqliteDatabase.endTransaction();
        } catch (Exception e) {
            return -2;
        } finally {
            dbHelper.closeAll(null, sqliteDatabase, dbHelper);
        }
        return 1;
    }

    public void batchUpdate(List<String> sql_list) {
        DataBaseHelper dbHelper = null;
        SQLiteDatabase sqliteDatabase = null;
        try {
            dbHelper = DataBaseHelper.getInstance(context);
            sqliteDatabase = dbHelper.getWritableDatabase();
            sqliteDatabase.beginTransaction();
            for (String sql : sql_list) {
                // 调用insert方法，就可以将数据插入到数据库当中   第一个参数:表名称
                // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
                // 第三个参数：ContentValues对象
                sqliteDatabase.execSQL(sql);
            }
            sqliteDatabase.setTransactionSuccessful();
            sqliteDatabase.endTransaction();
        } catch (Exception e) {
        } finally {
            dbHelper.closeAll(null, sqliteDatabase, dbHelper);
        }
    }

    public int updateDailyRecord(ContentValues values, String condition, String[] condValues) {
        int result = -2;
        SQLiteDatabase sqliteDatabase = null;
        DataBaseHelper helper = null;
        Cursor cursor = null;
        try {
            helper = DataBaseHelper.getInstance(context);
            sqliteDatabase = helper.getWritableDatabase();
//            sqliteDatabase.execSQL("BEGIN EXCLUSIVE transaction");
            // 第一个参数String：表名   第二个参数ContentValues：ContentValues对象
            // 第三个参数String：where字句，相当于sql语句where后面的语句，？号是占位符
            // 第四个参数String[]：占位符的值
            result = sqliteDatabase.update("daily_record", values, condition, condValues);
//            sqliteDatabase.execSQL("commit transaction");
        } catch (Exception e) {
        } finally {
            helper.closeAll(cursor, sqliteDatabase, helper);
        }
        return result;
    }

    private List<CustomInfo> getCustom(Cursor cursor) {
        List<CustomInfo> customInfoList = new ArrayList<CustomInfo>();
        while (cursor.moveToNext()) {
            CustomInfo info = new CustomInfo();
//            {"c_name", "c_position", "c_id", "c_company","c_industry","c_group", "c_telephone"}
            info.setId(cursor.getString(cursor.getColumnIndex("c_id")));
            info.setName(cursor.getString(cursor.getColumnIndex("c_name")));
            info.setPosition(cursor.getString(cursor.getColumnIndex("c_position")));
            info.setBusiness(cursor.getString(cursor.getColumnIndex("c_industry")));
            info.setCompany(cursor.getString(cursor.getColumnIndex("c_company")));
            info.setTelephone(cursor.getString(cursor.getColumnIndex("c_telephone")));
//            info.setEasyName(cursor.getString(cursor.getColumnIndex("c_easyname")));
            info.setGuid(cursor.getString(cursor.getColumnIndex("c_group")));  //客户所属分组 group uid
//            info.setCustomType(cursor.getString(cursor.getColumnIndex("c_type")));
//            info.setMailBox(cursor.getString(cursor.getColumnIndex("c_email")));
//            info.setProvince(cursor.getString(cursor.getColumnIndex("c_province")));
//            info.setResieveMsg(cursor.getString(cursor.getColumnIndex("c_sendmsg")));
            customInfoList.add(info);
        }
        return customInfoList;
    }
}
