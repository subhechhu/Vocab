package com.subhechhu.vocabb;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_table")
//@Entity is a room annotation, creates all the necessary code to create SQLite object
public class WordModel {

    @PrimaryKey
    @NonNull
    private String word;

    private String pronunciation;

    private String meaning;

    private String sentence;

    private int correct;

    private int incorrect;

    public WordModel(String word, String pronunciation, String meaning, String sentence, int correct, int incorrect) {
        this.word = word;
        this.pronunciation = pronunciation;
        this.meaning = meaning;
        this.sentence = sentence;
        this.correct = correct;
        this.incorrect = incorrect;
    }

    public String getWord() {
        return word;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getSentence() {
        return sentence;
    }


    public int getCorrect() {
        return correct;
    }


    public int getIncorrect() {
        return incorrect;
    }

    @Override
    public String toString() {
        return "WordModel{" +
                "word='" + word + '\'' +
                ", pronunciation='" + pronunciation + '\'' +
                ", meaning='" + meaning + '\'' +
                ", sentence='" + sentence + '\'' +
                ", correct=" + correct +
                ", incorrect=" + incorrect +
                '}';
    }
}
