package com.subhechhu.vocabb.localdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.subhechhu.vocabb.WordModel;
import com.subhechhu.vocabb.apirequest.WebServiceRepository;

import java.util.List;

public class WordViewModel extends AndroidViewModel{
    private WordRepository repository;
    WebServiceRepository webServiceRepository ;

    public WordViewModel(@NonNull Application application) {
        super(application);
        repository = new WordRepository(application);
        webServiceRepository = new WebServiceRepository(application);
    }

    public void insert(WordModel wordModel) {
        repository.insert(wordModel);
    }

    public void update(WordModel wordModel) {
        repository.update(wordModel);
    }

    public void delete(WordModel wordModel) {
        repository.delete(wordModel);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<WordModel>> getAllWords() {
        return repository.getAllWords();
    }

    public LiveData<WordModel> getRandomWord() {
        return repository.getRandomWord();
    }

    public void fetchFromOwlbot(String url) {
        webServiceRepository.getDetailsFromWeb(url);
    }

    public LiveData<String> responseFromOwlbot(){
        return webServiceRepository.setDetailsFromWeb();
    }

}
