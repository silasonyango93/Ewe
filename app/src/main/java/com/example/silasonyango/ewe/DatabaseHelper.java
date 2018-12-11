package com.example.silasonyango.ewe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SilasOnyango on 2/28/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Klabu.db";
    public static final String TABLE_NAME="Users";
    public static final String COL_0="dbId";
    public static final String COL_1="id";
    public static final String COL_2="name";
    public static final String COL_3="email";
  public static final String COL_4="mKey";
    public static final String COL_5="Address";

    public static final String TABLE_CHAT="Chat";
    public static final String COL_8="dbId";
    public static final String COL_9="Status";
    public static final String COL_10="RowKey";
    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(dbId INTEGER PRIMARY KEY AUTOINCREMENT,id VARCHAR(200),name VARCHAR(200),email VARCHAR(200),mKey VARCHAR(200),Address VARCHAR(200))");
        db.execSQL("CREATE TABLE " + TABLE_CHAT + "(dbId INTEGER PRIMARY KEY AUTOINCREMENT,Status VARCHAR(200),RowKey VARCHAR(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_CHAT);
        onCreate(db);

    }

    public boolean insertData(String id,String name,String email,String mKey,String address)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, email);
        contentValues.put(COL_4, mKey);
        contentValues.put(COL_5, address);

        long result=db.insert(TABLE_NAME,null,contentValues);

        if(result==-1)
            return false;
        else
            return true;

    }

    public Cursor getAllData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM " +TABLE_NAME,null);
        return res;
    }

    public Cursor getSpecificData(String name)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM " +TABLE_NAME+ " WHERE " +COL_2+ " LIKE '%" +name+ "%';",null);
        return res;
    }

    public boolean updateData(String id,String name,String marks,String grade)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3, marks);
        //contentValues.put(COL_4, grade);


        db.update(TABLE_NAME,contentValues, "id = ?",new String[] {id});

        return true;

    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME, " id = ?",new String[] {id});
    }

    public Cursor average()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT AVG(MARKS) FROM " + TABLE_NAME, null);
        return res;
    }

    public Cursor getUserId(String Key)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT id,name FROM " +TABLE_NAME,null);
        return res;
    }



    public Cursor getAllCredentials()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM " +TABLE_NAME,null);
        return res;
    }

    public Integer deleteCredentials(String mKey)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME, " holder = ?",new String[] {mKey});
    }


    public boolean updateCredentials(String id,String name,String email,String mKey,String address)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, email);
        contentValues.put(COL_5, address);

        //contentValues.put(COL_5, address);


        db.update(TABLE_NAME,contentValues, "mKey = ?",new String[] {mKey});

        return true;

    }

    public boolean insertChatWallStatus(String Status,String RowKey)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_9,Status);
        contentValues.put(COL_10, RowKey);


        long result=db.insert(TABLE_CHAT,null,contentValues);

        if(result==-1)
            return false;
        else
            return true;

    }

    public boolean updateChatWallStatus(String Status,String RowKey)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_9,Status);

        //contentValues.put(COL_5, address);


        db.update(TABLE_CHAT,contentValues, "RowKey = ?",new String[] {RowKey});

        return true;

    }

    public Cursor checkChatStatus()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM " +TABLE_CHAT,null);
        return res;
    }



}
