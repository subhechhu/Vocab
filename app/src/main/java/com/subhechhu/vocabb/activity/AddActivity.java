package com.subhechhu.vocabb.activity;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.subhechhu.vocabb.R;
import com.subhechhu.vocabb.WordModel;
import com.subhechhu.vocabb.localdb.WordViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    EditText et_word, et_sentence, et_meaning, et_pronunciation;
    Button add, delete;
    String word, pronunciation, sentence, meaning, from, url;
    ProgressBar progressBar2;

    int correct, incorrect;
    boolean fetchMeaning = true;
    private WordViewModel wordViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        }

        setContentView(R.layout.activity_add);

        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.INVISIBLE);

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        wordViewModel.responseFromOwlbot().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e("TAG", "OwlBot response: " + s);
                progressBar2.setVisibility(View.INVISIBLE);
                add.setText(R.string.add_details);
                parseResponse(s);
            }
        });

        word = getIntent().getStringExtra("word");
        pronunciation = getIntent().getStringExtra("pronunciation");
        sentence = getIntent().getStringExtra("sentence");
        meaning = getIntent().getStringExtra("meaning");
        from = getIntent().getStringExtra("from");
        correct = getIntent().getIntExtra("correct", 0);
        incorrect = getIntent().getIntExtra("incorrect", 0);


        et_word = findViewById(R.id.editText_newword);
        et_sentence = findViewById(R.id.editText_sentence);
        et_meaning = findViewById(R.id.editText_meaning);
        et_pronunciation = findViewById(R.id.editText_pronunciation);


        add = findViewById(R.id.button_add);
        delete = findViewById(R.id.button_delete);

        add.setText(R.string.fetch_details);

        if ("List".equalsIgnoreCase(from)) {
            delete.setVisibility(View.VISIBLE);
            et_word.setText(word);
            et_word.setEnabled(false);
            et_pronunciation.setText(pronunciation);
            et_meaning.setText(meaning);
            et_sentence.setText(sentence);
            add.setText(R.string.modify);
            correct = getIntent().getIntExtra("correct", 0);
            incorrect = getIntent().getIntExtra("incorrect", 0);
            fetchMeaning = false;
        }

        delete.setOnClickListener(view -> {
            WordModel modelWord = new WordModel(word,
                    pronunciation,
                    meaning,
                    sentence,
                    correct,
                    incorrect);
            wordViewModel.delete(modelWord);

            Toast.makeText(this, "Details for " + word + " is deleted", Toast.LENGTH_SHORT).show();

            finish();
        });

        findViewById(R.id.imageview_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fetchMeaning && !et_word.getText().toString().isEmpty()) {
                    progressBar2.setVisibility(View.VISIBLE);
                    fetchMeaning = false;
                    wordViewModel.fetchFromOwlbot(getString(R.string.dictionaryURL) + et_word.getText().toString());
                } else {
                    if (et_word.getText().toString().isEmpty() || et_meaning.getText().toString().isEmpty()
                            || et_pronunciation.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Fields Cannot Be Empty", Toast.LENGTH_SHORT).show();
                    } else {
                        addToStorage();
                    }
                }
            }
        });
    }

    private void addToStorage() {
        WordModel modelWord = new WordModel(et_word.getText().toString().toLowerCase(),
                et_pronunciation.getText().toString().toLowerCase(),
                et_meaning.getText().toString().toLowerCase(),
                et_sentence.getText().toString().toLowerCase().trim(),
                correct,
                incorrect);
        if ("List".equalsIgnoreCase(from)) {
            Toast.makeText(AddActivity.this, "Details for " + et_word.getText().toString() + " has been modified", Toast.LENGTH_SHORT).show();
            wordViewModel.update(modelWord);
            finish();
        } else {
            Toast.makeText(AddActivity.this, "New Word Added", Toast.LENGTH_SHORT).show();
            wordViewModel.insert(modelWord);

            et_word.setText("");
            et_pronunciation.setText("");
            et_meaning.setText("");
            et_sentence.setText("");
            fetchMeaning = true;
            add.setText(R.string.fetch_details);
        }
    }

    private void parseResponse(String s) {
        try {
            JSONObject jsonObjectResponse = new JSONObject(s);
            JSONArray definationArray = jsonObjectResponse.getJSONArray("definitions");
            for (int i = 0; i < definationArray.length(); i++) {
                JSONObject defObject = definationArray.getJSONObject(i);

                if (et_meaning.getText().length() == 0) {
                    if (!defObject.getString("definition").equals("null")) {
                        et_meaning.setText(defObject.getString("definition"));
                    }
                } else {
                    if (!defObject.getString("definition").equals("null")) {
                        et_meaning.append("\n\n" + defObject.getString("definition"));
                    }
                }

                if (et_sentence.getText().length() == 0) {
                    if (!defObject.getString("example").equals("null")) {
                        et_sentence.setText(defObject.getString("example"));
                    }
                } else {
                    if (!defObject.getString("example").equals("null")) {
                        et_sentence.append("\n\n" + defObject.getString("example"));
                    }
                }
            }
            if (!jsonObjectResponse.getString("pronunciation").equals("null"))
                et_pronunciation.setText(jsonObjectResponse.getString("pronunciation"));

        } catch (Exception e) {
            Toast.makeText(AddActivity.this, "No details found for " + et_word.getText().toString() + ". \nPlease add it manually", Toast.LENGTH_LONG).show();
        }
    }
}
