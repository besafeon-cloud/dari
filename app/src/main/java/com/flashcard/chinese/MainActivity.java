package com.flashcard.chinese;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<WordData> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        words = WordRepository.loadWords(this);
        updateStats();
    }

    private void updateStats() {
        int total = words.size();
        int weak = 0, mastered = 0;
        for (WordData w : words) {
            if (w.isWeak) weak++;
            if (w.correctCount > 0 && w.wrongCount == 0) mastered++;
        }

        TextView tvStats = findViewById(R.id.tvStats);
        tvStats.setText("전체 " + total + "개 · 취약 " + weak + "개 · 완전암기 " + mastered + "개");

        Button btnWeak = findViewById(R.id.btnWeakMode);
        btnWeak.setEnabled(weak > 0);
        btnWeak.setAlpha(weak > 0 ? 1.0f : 0.4f);
        if (weak > 0) btnWeak.setText("취약 카드 반복 (" + weak + "개)");
        else btnWeak.setText("취약 카드 없음");
    }

    public void startAllMode(View v) {
        Intent i = new Intent(this, FlashcardActivity.class);
        i.putExtra("mode", "all");
        startActivity(i);
    }

    public void startWeakMode(View v) {
        Intent i = new Intent(this, FlashcardActivity.class);
        i.putExtra("mode", "weak");
        startActivity(i);
    }

    public void resetProgress(View v) {
        WordRepository.resetWords(this);
        words = WordRepository.loadWords(this);
        updateStats();
        android.widget.Toast.makeText(this, "학습 기록이 초기화되었습니다.", android.widget.Toast.LENGTH_SHORT).show();
    }
}
