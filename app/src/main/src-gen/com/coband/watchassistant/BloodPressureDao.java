package com.coband.watchassistant;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.coband.watchassistant.BloodPressure;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BLOOD_PRESSURE".
*/
public class BloodPressureDao extends AbstractDao<BloodPressure, Long> {

    public static final String TABLENAME = "BLOOD_PRESSURE";

    /**
     * Properties of entity BloodPressure.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Date = new Property(1, long.class, "date", false, "DATE");
        public final static Property BloodPressures = new Property(2, String.class, "bloodPressures", false, "BLOOD_PRESSURES");
        public final static Property Upload = new Property(3, boolean.class, "upload", false, "UPLOAD");
        public final static Property Uid = new Property(4, String.class, "uid", false, "UID");
    };


    public BloodPressureDao(DaoConfig config) {
        super(config);
    }
    
    public BloodPressureDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BLOOD_PRESSURE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"DATE\" INTEGER NOT NULL ," + // 1: date
                "\"BLOOD_PRESSURES\" TEXT," + // 2: bloodPressures
                "\"UPLOAD\" INTEGER NOT NULL ," + // 3: upload
                "\"UID\" TEXT NOT NULL );"); // 4: uid
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BLOOD_PRESSURE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, BloodPressure entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getDate());
 
        String bloodPressures = entity.getBloodPressures();
        if (bloodPressures != null) {
            stmt.bindString(3, bloodPressures);
        }
        stmt.bindLong(4, entity.getUpload() ? 1L: 0L);
        stmt.bindString(5, entity.getUid());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public BloodPressure readEntity(Cursor cursor, int offset) {
        BloodPressure entity = new BloodPressure( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // date
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // bloodPressures
            cursor.getShort(offset + 3) != 0, // upload
            cursor.getString(offset + 4) // uid
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, BloodPressure entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDate(cursor.getLong(offset + 1));
        entity.setBloodPressures(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUpload(cursor.getShort(offset + 3) != 0);
        entity.setUid(cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(BloodPressure entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(BloodPressure entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
