package com.group17.mathreinforcementtool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Notes.db";
    public static final int VERSION_NUM = 2;

    public static final String TABLE_NAME = "table_notes";
    public static final String KEY_ID = "id";
    public static final String KEY_NOTE_TITLE = "TITLE";
    public static final String KEY_NOTE_BODY = "BODY";

    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_NOTE_TITLE + " text not null, "
            + KEY_NOTE_BODY + " text not null);";

    public NotesDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("NotesDatabaseHelper", "Calling onCreate");
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i("NotesDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i("NotesDatabaseHelper", "Calling onDowngrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}