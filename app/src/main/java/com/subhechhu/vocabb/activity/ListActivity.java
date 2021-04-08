package com.subhechhu.vocabb.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.subhechhu.vocabb.AppController;
import com.subhechhu.vocabb.CustomAdapter;
import com.subhechhu.vocabb.R;
import com.subhechhu.vocabb.WordModel;
import com.subhechhu.vocabb.localdb.WordViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListActivity extends AppCompatActivity implements CustomAdapter.ItemClick, TextToSpeech.OnInitListener {

    RecyclerView recyclerView;
    List<WordModel> listOfWords;
    WordModel tempList;
    CustomAdapter customAdapter;

    TextView textView_empty;
    EditText editText_search;
    Button button_clear;
    RelativeLayout parentLayout;

    WordViewModel wordViewModel;

    TextToSpeech tts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        }

        setContentView(R.layout.activity_list);

        parentLayout = findViewById(R.id.parentLayout);
        recyclerView = findViewById(R.id.recyclerView_main);
        textView_empty = findViewById(R.id.textView_empty);
        editText_search = findViewById(R.id.editText_search);
        button_clear = findViewById(R.id.button_clear);

        if (AppController.getBoolean(AppController.getInstance().getString(R.string.list_pref))) {
            final Snackbar snack = Snackbar.make(
                    parentLayout,
                    "Tap and hold for pronunciation",
                    Snackbar.LENGTH_INDEFINITE);

            snack.setAction("GET IT", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.storePreferenceBoolean(AppController.getInstance().getString(R.string.list_pref), false);
                    snack.dismiss();
                }
            }).setActionTextColor(ContextCompat.getColor(ListActivity.this, R.color.hint));
            snack.show();
        }

        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    button_clear.setVisibility(View.INVISIBLE);
                } else {
                    button_clear.setVisibility(View.VISIBLE);
                }
                filter(editable.toString());
            }
        });

        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText_search.setText("");
            }
        });

        listOfWords = new ArrayList<>();
        tts = new TextToSpeech(this, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        customAdapter = new CustomAdapter(this);
        recyclerView.setAdapter(customAdapter);

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        wordViewModel.getAllWords().observe(this, new Observer<List<WordModel>>() {
            @Override
            public void onChanged(List<WordModel> wordModels) {
                Log.e("TAGGED", "size: " + wordModels.size());
                if (wordModels.size() > 0) {
                    textView_empty.setVisibility(View.INVISIBLE);
                    listOfWords = wordModels;
                    customAdapter.showList(wordModels);
                } else {
                    textView_empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void filter(String searchWord) {
        List<WordModel> searchedWord = new ArrayList<>();
        for (WordModel model : listOfWords) {
            if (model.getWord().contains(searchWord) || model.getPronunciation().contains(searchWord)) {
                searchedWord.add(model);
            }
        }
        if (customAdapter != null) {
            customAdapter.showList(searchedWord);
        }
    }


    @Override
    public void onClick(WordModel modelWord) {
        Intent intent = new Intent(ListActivity.this, AddActivity.class);
        intent.putExtra("from", "List");
        intent.putExtra("word", modelWord.getWord());
        intent.putExtra("pronunciation", modelWord.getPronunciation());
        intent.putExtra("sentence", modelWord.getSentence());
        intent.putExtra("meaning", modelWord.getMeaning());
        intent.putExtra("correct", modelWord.getCorrect());
        intent.putExtra("incorrect", modelWord.getIncorrect());
        startActivity(intent);
    }

    @Override
    public void onLongClick(WordModel modelWord) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.setSpeechRate(0.75f);
            tts.speak(modelWord.getWord(), TextToSpeech.QUEUE_FLUSH, null, null);
        }
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
            Toast.makeText(this, "Text To Speech Initilization Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
