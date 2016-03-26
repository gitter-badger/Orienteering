package com.rustyclock.orienteering.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.rustyclock.orienteering.model.Checkpoint;

import java.io.InputStream;
import java.sql.SQLException;

/**
 * Created by Mateusz Jablonski
 * on 2015-06-10.
 */
public class DbHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "orienteering.db";

    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Checkpoint.class);
        } catch (SQLException e) {
            Log.e(DbHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }
}
