package com.subhechhu.vocabb.localdb;

/*
DAO, data access object has to be an interface or
abstract classes as we don't provide the method body.
Room library handles that for us
 */

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.subhechhu.vocabb.WordModel;

import java.util.List;

@Dao
public interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(WordModel wordModel);

    @Update
    void update(WordModel wordModel);

    @Delete
    void delete(WordModel wordModel);

    @Query("DELETE FROM word_table")
    void deleteAllWords();

    @Query("Select * FROM WORD_TABLE ORDER BY WORD ASC")
    LiveData<List<WordModel>> getAllWords();

    @Query("SELECT * FROM WORD_TABLE ORDER BY RANDOM() LIMIT 1")
    LiveData<WordModel> getRandom();

}
