package com.subhechhu.vocabb.localdb;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.subhechhu.vocabb.WordModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WordRepository {
    private WordDao wordDao;

    public WordRepository(Application application) {
        WordDB wordDB = WordDB.getInstance(application);
        wordDao = wordDB.wordDao();
    }

    public void insert(final WordModel wordModel) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                wordDao.insert(wordModel);
            }
        };
        execute(runnable);
    }

    public void update(final WordModel wordModel) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                wordDao.update(wordModel);
            }
        };
        execute(runnable);
    }

    public void delete(final WordModel wordModel) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                wordDao.delete(wordModel);
            }
        };
        execute(runnable);
    }

    public void deleteAll() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                wordDao.deleteAllWords();
            }
        };
    }

    public LiveData<WordModel> getRandomWord() {
        return wordDao.getRandom();
    }

    public LiveData<WordModel> getMeaning() {
        return wordDao.getRandom();
    }

    public LiveData<List<WordModel>> getAllWords() {
        return wordDao.getAllWords();
    }

    private void execute(Runnable runnable) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(runnable);
    }
}