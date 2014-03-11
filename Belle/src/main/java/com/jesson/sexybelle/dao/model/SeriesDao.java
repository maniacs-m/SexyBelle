package com.jesson.sexybelle.dao.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.jesson.sexybelle.dao.model.Series;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SERIES.
*/
public class SeriesDao extends AbstractDao<Series, Void> {

    public static final String TABLENAME = "SERIES";

    /**
     * Properties of entity Series.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Type = new Property(0, int.class, "type", false, "TYPE");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
    };


    public SeriesDao(DaoConfig config) {
        super(config);
    }
    
    public SeriesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SERIES' (" + //
                "'TYPE' INTEGER NOT NULL ," + // 0: type
                "'TITLE' TEXT NOT NULL );"); // 1: title
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SERIES'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Series entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getType());
        stmt.bindString(2, entity.getTitle());
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public Series readEntity(Cursor cursor, int offset) {
        Series entity = new Series( //
            cursor.getInt(offset + 0), // type
            cursor.getString(offset + 1) // title
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Series entity, int offset) {
        entity.setType(cursor.getInt(offset + 0));
        entity.setTitle(cursor.getString(offset + 1));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(Series entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(Series entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}