package com.subhechhu.vocabb.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.subhechhu.vocabb.AppController;
import com.subhechhu.vocabb.R;
import com.subhechhu.vocabb.WordModel;
import com.subhechhu.vocabb.localdb.WordViewModel;

import java.util.List;


public class HomeActivity extends AppCompatActivity {

    TextView textView_count, textView_user;
    ProgressBar progressBar;

    WordViewModel wordViewModel;

    int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        }

        setContentView(R.layout.activity_home);
        textView_count = findViewById(R.id.textView_count);
        textView_user = findViewById(R.id.textView_user);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        if (AppController.getBoolean(
                AppController.getInstance().getString(R.string.login_pref))) {
            wordViewModel.getAllWords();
            AppController.storePreferenceBoolean(AppController.getInstance().getString(R.string.login_pref), false);
        }

        wordViewModel.getAllWords().observe(this, new Observer<List<WordModel>>() {
            @Override
            public void onChanged(List<WordModel> wordModels) {
                size = wordModels.size();
                textView_count.setText(getString(R.string.total_word_count, size));
            }
        });

        findViewById(R.id.button_add).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddActivity.class);
            intent.putExtra("from", "Main");
            intent.putExtra("word", "");
            intent.putExtra("pronunciation", "");
            intent.putExtra("sentence", "");
            intent.putExtra("meaning", "");
            intent.putExtra("correct", 0);
            intent.putExtra("incorrect", 0);
            startActivity(intent);
        });

        findViewById(R.id.button_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ListActivity.class));
            }
        });

        findViewById(R.id.button_word).setOnClickListener(view -> {
            if (size == 0)
                Toast.makeText(this, "Add Some Words First", Toast.LENGTH_SHORT).show();
            else
                startActivity(new Intent(HomeActivity.this, WordActivity.class));
        });

        findViewById(R.id.button_meaning).setOnClickListener(view -> {
            if (size == 0)
                Toast.makeText(this, "Add Some Words First", Toast.LENGTH_SHORT).show();
            else
                startActivity(new Intent(HomeActivity.this, MeaningActivity.class));
        });

    }
}