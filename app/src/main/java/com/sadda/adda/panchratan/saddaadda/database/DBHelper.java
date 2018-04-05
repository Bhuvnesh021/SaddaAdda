package com.sadda.adda.panchratan.saddaadda.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sadda.adda.panchratan.saddaadda.objects.Comment;
import com.sadda.adda.panchratan.saddaadda.objects.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 14-09-2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "room_expense";
    public static final String TABLE_COMMENTS = "t_comments";
    public static final String TABLE_ADDED_ITEMS = "t_added_items";
    public static final String COLUMN_COMMENT_USER_NAME = "user_name";
    public static final String COLUMN_COMMENT_COMMENT = "comment";
    public static final String COLUMN_COMMENT_DATE = "date";
    public static final String COLUMN_COMMENT_ID = "id";
    public static final String COLUMN_ADDED_ITEMS_CATEGORY = "category";
    public static final String COLUMN_ADDED_ITEMS_ITEM_NAME = "item";
    public static final String COLUMN_ADDED_ITEMS_QUANTITY = "quantity";
    public static final String COLUMN_ADDED_ITEMS_PRICE = "price";
    public static final String COLUMN_ADDED_ITEMS_ADDED_NAMES = "names";
    public static final String COLUMN_ADDED_ITEMS_DATE = "date";
    public static final String COLUMN_ADDED_ITEMS_USER_NAME = "user_name";
    public static final String COLUMN_ADDED_ITEMS_DESCRIPTION = "description";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_COMMENTS + " (" + COLUMN_COMMENT_ID + " INTEGER AUTO INCREMENT PRIMARY KEY, " + COLUMN_COMMENT_USER_NAME + " text, " + COLUMN_COMMENT_COMMENT + " text, " + COLUMN_COMMENT_DATE + " text);");
        db.execSQL("CREATE TABLE " + TABLE_ADDED_ITEMS + "(" + COLUMN_COMMENT_ID + "  INTEGER AUTO INCREMENT PRIMARY KEY, " + COLUMN_ADDED_ITEMS_CATEGORY + " text, " + COLUMN_ADDED_ITEMS_ITEM_NAME + " text, " + COLUMN_ADDED_ITEMS_QUANTITY + " text, " + COLUMN_ADDED_ITEMS_PRICE + " text, " + COLUMN_ADDED_ITEMS_ADDED_NAMES + " text, " + COLUMN_ADDED_ITEMS_DATE + " text, " + COLUMN_ADDED_ITEMS_USER_NAME + " text, " + COLUMN_ADDED_ITEMS_DESCRIPTION + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_COMMENTS);
        db.execSQL("drop table if exists " + TABLE_ADDED_ITEMS);
    }

    public void clearAllDataItemsTable() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_ADDED_ITEMS);
        sqLiteDatabase.close();
    }

    public void clearAllDataCommentTable() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_COMMENTS);
        sqLiteDatabase.close();
    }

    public void addComment(Comment comment) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COMMENT_USER_NAME, comment.getUserName());
        contentValues.put(COLUMN_COMMENT_COMMENT, comment.getComment());
        contentValues.put(COLUMN_COMMENT_DATE, comment.getTimeAndDate());
        sqLiteDatabase.insert(TABLE_COMMENTS, null, contentValues);
        sqLiteDatabase.close();
    }




    public void addItem(Item item) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ADDED_ITEMS_CATEGORY, item.getCategory());
        contentValues.put(COLUMN_ADDED_ITEMS_ITEM_NAME, item.getItemName());
        contentValues.put(COLUMN_ADDED_ITEMS_QUANTITY, item.getQuantity());
        contentValues.put(COLUMN_ADDED_ITEMS_PRICE, item.getPrice());
        contentValues.put(COLUMN_ADDED_ITEMS_ADDED_NAMES, item.getNames());
        contentValues.put(COLUMN_ADDED_ITEMS_DATE, item.getDate());
        contentValues.put(COLUMN_ADDED_ITEMS_USER_NAME, item.getUserName());
        contentValues.put(COLUMN_ADDED_ITEMS_DESCRIPTION, item.getDescription());
        sqLiteDatabase.insert(TABLE_ADDED_ITEMS, null, contentValues);
        sqLiteDatabase.close();
    }

    public List<Item> getAllItems() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_ADDED_ITEMS, null);
        List<Item> itemsList = null;
        if (cursor != null) {
            Log.i(TAG, "getAllItems: cursor.size(): " + cursor.getCount());
            if (cursor.getCount() > 0) {
                itemsList = new ArrayList<>();
                cursor.moveToFirst();
                do {
                    String category = cursor.getString(cursor.getColumnIndex(COLUMN_ADDED_ITEMS_CATEGORY));
                    String itemName = cursor.getString(cursor.getColumnIndex(COLUMN_ADDED_ITEMS_ITEM_NAME));
                    String quantity = cursor.getString(cursor.getColumnIndex(COLUMN_ADDED_ITEMS_QUANTITY));
                    String price = cursor.getString(cursor.getColumnIndex(COLUMN_ADDED_ITEMS_PRICE));
                    String addedNames = cursor.getString(cursor.getColumnIndex(COLUMN_ADDED_ITEMS_ADDED_NAMES));
                    String date = cursor.getString(cursor.getColumnIndex(COLUMN_ADDED_ITEMS_DATE));
                    String userName = cursor.getString(cursor.getColumnIndex(COLUMN_ADDED_ITEMS_USER_NAME));
                    String itemDescription = cursor.getString(cursor.getColumnIndex(COLUMN_ADDED_ITEMS_DESCRIPTION));
                    Item item = new Item();
                    item.setCategory(category);
                    item.setItemName(itemName);
                    item.setQuantity(quantity);
                    item.setPrice(price);
                    item.setNames(addedNames);
                    item.setDate(date);
                    item.setUserName(userName);
                    item.setDescription(itemDescription);
                    itemsList.add(item);
                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        sqLiteDatabase.close();
        return itemsList;


    }

    public List<Comment> getAllComments() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_COMMENTS, null);
        List<Comment> commentArrayList = null;
        if (cursor != null) {
            Log.i(TAG, "getAllComments: cursor.size(): " + cursor.getCount());
            if (cursor.getCount() > 0) {
                commentArrayList = new ArrayList<>();
                cursor.moveToFirst();
                do {
                    String commentUserName = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT_USER_NAME));
                    String comment = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT_COMMENT));
                    String timeDate = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT_DATE));
                    Comment commentObj = new Comment();
                    commentObj.setUserName(commentUserName);
                    commentObj.setComment(comment);
                    commentObj.setTimeAndDate(timeDate);
                    commentArrayList.add(commentObj);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return commentArrayList;
    }
}
