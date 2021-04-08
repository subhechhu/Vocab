package com.subhechhu.vocabb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private List<WordModel> wordListModel = new ArrayList<>();

    private Context context;
    private ItemClick itemClick;

    public CustomAdapter(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_words, textView_meanings, textView_incorrect, textView_correct;
        LinearLayout linearLayout_main;

        MyViewHolder(View view) {
            super(view);
            textView_words = view.findViewById(R.id.textView_word);
            textView_meanings = itemView.findViewById(R.id.textView_meaning);
            textView_incorrect = itemView.findViewById(R.id.textView_incorrect);
            textView_correct = itemView.findViewById(R.id.textView_correct);
            linearLayout_main = itemView.findViewById(R.id.linearLayout_main);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_content, parent, false);
        return new MyViewHolder(itemView);
    }

    public void showList(List<WordModel> displayedList) {
        this.wordListModel = displayedList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final WordModel modelWord = wordListModel.get(position);
        holder.textView_words.setText(modelWord.getWord() + " (" + modelWord.getPronunciation() + ")");
        holder.textView_meanings.setText(modelWord.getMeaning());
        holder.textView_correct.setText("" + modelWord.getCorrect());
        holder.textView_incorrect.setText("" + modelWord.getIncorrect());
        holder.linearLayout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onClick(modelWord);
            }
        });

        holder.linearLayout_main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemClick.onLongClick(modelWord);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordListModel.size();
    }

    public interface ItemClick {
        void onClick(WordModel modelWord);
        void onLongClick(WordModel modelWord);
    }
}
