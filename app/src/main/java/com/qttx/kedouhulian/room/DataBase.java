package com.qttx.kedouhulian.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.qttx.kedouhulian.bean.RegionsBean;

/**
 * @author huangyr
 * @date 2018/12/4
 */
@Database(entities = {RegionsBean.class}, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    private static final String DB_NAME = "KeDouDatabase.db";

    private static volatile DataBase INSTANCE;

    public abstract CityDao cityDao();

    public static DataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DataBase.class, DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
