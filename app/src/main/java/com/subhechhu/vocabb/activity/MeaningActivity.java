package com.subhechhu.vocabb.activity;


import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.subhechhu.vocabb.R;
import com.subhechhu.vocabb.WordModel;
import com.subhechhu.vocabb.localdb.WordViewModel;

public class MeaningActivity extends AppCompatActivity {

    TextView textView_meaning, textView_incorrect, textView_correct;
    String word, meaning, sentence, pronunciation;
    EditText editText_word;
    Button button_check;
    int correct, incorrect;

    boolean wordAdded;
    WordViewModel wordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        }

        setContentView(R.layout.activity_sentence);

        textView_meaning = findViewById(R.id.textView_meaning);
        editText_word = findViewById(R.id.editText_word);
        textView_incorrect = findViewById(R.id.textView_incorrect);
        textView_correct = findViewById(R.id.textView_correct);

        button_check = findViewById(R.id.button_check);

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        getRandomWord();

        findViewById(R.id.imageview_close).setVisibility(View.INVISIBLE);
        findViewById(R.id.imageview_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editText_word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    wordAdded = true;
                    button_check.setText(R.string.check);
                } else {
                    wordAdded = false;
                    button_check.setText(R.string.give_up);
                }
            }
        });

        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!wordAdded) {
                    String message = "You gave up. The correct word is " + word;
                    incorrectCount(message);
                } else {
                    if (editText_word.getText().toString().trim().equalsIgnoreCase(word.trim())) {
                        correctCount();
                    } else {
                        String message = editText_word.getText().toString().trim() + " is wrong. Correct word is " + word;
                        incorrectCount(message);
                    }
                }
            }
        });
    }

    private void incorrectCount(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.main), message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Gotcha", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MeaningActivity.this, "Whoopss", Toast.LENGTH_SHORT).show();
                        incorrect++;

                        WordModel wordModel = new WordModel(
                                word,
                                pronunciation,
                                meaning,
                                sentence,
                                correct,
                                incorrect
                        );
                        wordViewModel.update(wordModel);

                        editText_word.setText("");
                        getRandomWord();
                    }
                });

        snackbar.show();
    }

    private void correctCount() {
        editText_word.setText("");
        Toast.makeText(MeaningActivity.this, "Bravooo", Toast.LENGTH_SHORT).show();
        correct++;

        WordModel wordModel = new WordModel(
                word,
                pronunciation,
                meaning,
                sentence,
                correct,
                incorrect
        );
        wordViewModel.update(wordModel);

        getRandomWord();
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

                    updateUI(correct, incorrect, meaning);
                } else {
                    Toast.makeText(MeaningActivity.this, "Add Some Words First", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(int correct, int incorrect, String meaning) {
        textView_meaning.setText(meaning);
        textView_correct.setText(String.valueOf(correct));
        textView_incorrect.setText(String.valueOf(incorrect));
    }
}

