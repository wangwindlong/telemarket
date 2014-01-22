package com.example.tool;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.telemarket.broadcast.PhoneBroadcast;

/**
 * 数据库帮助类
 *
 */
public class SqliteUtil extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "broadcast.db";
	private static final String TABLE_NAME = "config";
	private SQLiteDatabase db = null;
	
	
	public SqliteUtil(Context context) {
		super(context, DB_NAME, null, 1);
	}

	public int getOldState(){
		int state = 0;
		db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME , null);
		if( cursor.moveToFirst() ){
			state = cursor.getInt( cursor.getColumnIndex("flag") );
		}
		db.close();
		return state;
	}
	
	public void updateState(int state ){
		db = getWritableDatabase();
		db.beginTransaction();
		db.execSQL("update " + TABLE_NAME + " set flag=" + state);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
	
	/**
	 * 第一次创建数据库(即DB_NAME这个数据库在机器上面不存在)的时候调用
	 * <br>系统会帮你创建DB_NAME这个数据库文件，你只需用建表就行了。
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		db.execSQL("create table " + TABLE_NAME + "(flag Integer)");
		db.execSQL("insert into " + TABLE_NAME + " values(" + PhoneBroadcast.CALL_TYPE_IDEL + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
