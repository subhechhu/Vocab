package com.subhechhu.vocabb.localdb;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.subhechhu.vocabb.WordModel;

@Database(entities = {WordModel.class}, version = 1)
public abstract class WordDB extends RoomDatabase {

    private static  WordDB instance;

    public abstract WordDao wordDao();

    //Sync to create a single instance in case of multi thread.
    public static synchronized WordDB getInstance(Context context){
        Log.e("TAG","------------- WordDB -------------");
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
            WordDB.class,"word_db")
                    .fallbackToDestructiveMigration() //useful when db is changed. it prevents the app to crash by replacing older db by new db
                    .build();
        }
        return instance;
    }
}
