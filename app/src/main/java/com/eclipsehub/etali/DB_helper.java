package com.eclipsehub.etali;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DB_helper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Helper";
    private static final int DATABASE_VERSION = 2;

    public DB_helper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" Create table user (user_id INTEGER primary key autoincrement, name Text, phone Text, time DOUBLE)");
        db.execSQL(" Create table rec (id INTEGER primary key autoincrement,amount DOUBLE, reason Text, user_id INTEGER, time DOUBLE, FOREIGN KEY (user_id) REFERENCES Users(user_id))");
        db.execSQL(" Create table pay (id INTEGER primary key autoincrement,amount DOUBLE, reason Text, user_id INTEGER, time DOUBLE, FOREIGN KEY (user_id) REFERENCES Users(user_id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists rec");
        db.execSQL("drop table if exists pay");

    }

    public void add_user(String name,String phone){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("phone",phone);
        contentValues.put("time",System.currentTimeMillis());
        database.insert("user",null, contentValues);

    }

    public Cursor get_user(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM (SELECT *, 'user' AS Source FROM user) AS Combined ORDER BY time DESC;",null);
        return cursor;
    }

    public double cal_rec(String key){

        double total_rec = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM rec WHERE user_id = ?", new String[]{String.valueOf(key)});

        if (cursor!=null && cursor.getCount()>0){

            while ( cursor.moveToNext() ){
                double amount = cursor.getDouble(1);
                total_rec = total_rec+amount;

            }
        }


        return total_rec;
    }

    public double cal_pay(String key1){
        double totalincome = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM pay WHERE user_id = ?", new String[]{String.valueOf(key1)});

        if (cursor!=null && cursor.getCount()>0){

            while ( cursor.moveToNext() ){
                double amount = cursor.getDouble(1);
                totalincome = totalincome+amount;
            }
        }


        return totalincome;
    }

    public Cursor search ( String key1){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE name LIKE '"+key1+"%'",null);


        return cursor;
    }

    public void update_user (String id,String name,String address){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("phone",address);
        database.update("user", contentValues,  "user_id=?", new String[]{id});

    }

    public void delete_user(String id){

        SQLiteDatabase db =  this.getWritableDatabase();

        db.execSQL("delete from user where user_id like "+id);


    }

    public void add_rec (double amount,String reason,int user_id){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("amount",amount);
        contentValues.put("reason",reason);
        contentValues.put("time",System.currentTimeMillis());
        contentValues.put("user_id",user_id);
        database.insert("rec",null, contentValues);

    }

    public void add_pay (double amount,String reason,int user_id){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("amount",amount);
        contentValues.put("reason",reason);
        contentValues.put("time",System.currentTimeMillis());
        contentValues.put("user_id",user_id);
        database.insert("pay",null, contentValues);

    }

    public void up_rec ( String id,double amount,String reason,int user_id){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("amount",amount);
        contentValues.put("reason",reason);
        contentValues.put("user_id",user_id);
        database.update("rec", contentValues,  "id=?", new String[]{id});

    }

    public void delete_rec(String id){

        SQLiteDatabase db =  this.getWritableDatabase();

        db.execSQL("delete from rec where id like "+id);


    }



    public void up_pay ( String id,double amount,String reason,int user_id){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("amount",amount);
        contentValues.put("reason",reason);
        contentValues.put("user_id",user_id);
        database.update("pay", contentValues,  "id=?", new String[]{id});

    }

    public void delete_pay(String id){

        SQLiteDatabase db =  this.getWritableDatabase();

        db.execSQL("delete from pay where id like "+id);


    }


    public Cursor get_history_rec (String key2){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ( SELECT *, 'rec' AS Source FROM rec WHERE user_id = ?) AS Combined ORDER BY time DESC;", new String[]{String.valueOf(key2)});

        return cursor;
    }


    public Cursor get_history_pay (String key2){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM (SELECT *, 'pay' AS Source FROM pay WHERE user_id = ? ) AS Combined ORDER BY time DESC;", new String[]{String.valueOf(key2)});

        return cursor;
    }





}
