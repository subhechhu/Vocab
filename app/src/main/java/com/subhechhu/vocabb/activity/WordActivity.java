package com.subhechhu.vocabb.activity;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.subhechhu.vocabb.R;
import com.subhechhu.vocabb.WordModel;
import com.subhechhu.vocabb.localdb.WordViewModel;

import java.util.Locale;

public class WordActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextView textView_word, textView_meaning, textView_show, textView_incorrect, textView_correct;
    String word, meaning, sentence, pronunciation;
    int correct, incorrect;

    boolean isMeaningShown;
    TextToSpeech tts;

    WordViewModel wordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        }

        setContentView(R.layout.activity_word);

        textView_word = findViewById(R.id.textView_word);
        textView_meaning = findViewById(R.id.textView_meaning);
        textView_show = findViewById(R.id.textView_show);
        textView_incorrect = findViewById(R.id.textView_incorrect);
        textView_correct = findViewById(R.id.textView_correct);

        tts = new TextToSpeech(this, this);

        textView_show.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (isMeaningShown) {
                    textView_show.setText("Show Meaning");
                    textView_meaning.setVisibility(View.INVISIBLE);
                } else {
                    textView_show.setText("Hide Meaning");
                    textView_meaning.setVisibility(View.VISIBLE);
                }
                isMeaningShown = !isMeaningShown;
            }
        });

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        getRandomWord();


        findViewById(R.id.imageview_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.imageview_speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.setSpeechRate(0.75f);
                    tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });

        findViewById(R.id.button_td).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WordActivity.this, "Whoopss", Toast.LENGTH_SHORT).show();

                incorrect = incorrect + 1;
                WordModel wordModel = new WordModel(
                        word,
                        pronunciation,
                        meaning,
                        sentence,
                        correct,
                        incorrect
                );
                wordViewModel.update(wordModel);

                if (isMeaningShown)
                    textView_show.performClick();
                getRandomWord();
            }
        });

        findViewById(R.id.button_tu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WordActivity.this, "Bravooo", Toast.LENGTH_SHORT).show();

                correct = correct + 1;
                WordModel wordModel = new WordModel(
                        word,
                        pronunciation,
                        meaning,
                        sentence,
                        correct,
                        incorrect
                );
                wordViewModel.update(wordModel);

                if (isMeaningShown)
                    textView_show.performClick();
                getRandomWord();
            }
        });
    }

    private void getRandomWord() {
        wordViewModel.getRandomWord().observe(this, new Observer<WordModel>() {
            @Override
            public void onChanged(WordModel wordModel) {
                if (wordModel != null) {
                    word = wordModel.getWord();
                    pronunciation = wordModel.getPronunciation();
                    meaning = wordModel.getMeaning();
                    sentence = wordModel.getSentence();
                    correct = wordModel.getCorrect();
                    incorrect = wordModel.getIncorrect();

                    updateUI(word, correct, incorrect, meaning);
                }else {
                    Toast.makeText(WordActivity.this,"Add Some Words First",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateUI(String word, int correct, int incorrect, String meaning) {
        textView_word.setText(word);
        textView_correct.setText(String.valueOf(correct));
        textView_incorrect.setText(String.valueOf(incorrect));
        textView_meaning.setText(meaning);
    }


    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(), "Language not supported",
                        Toast.LENGTH_LONG).show();
                Log.e("TTS", "Language is not supported");
            }
        } else {
            Toast.makeText(this, "Text To Speech Initialisation Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
