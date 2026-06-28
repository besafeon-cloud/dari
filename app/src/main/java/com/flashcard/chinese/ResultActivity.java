package com.flashcard.chinese;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int total = getIntent().getIntExtra("total", 0);
        int correct = getIntent().getIntExtra("correct", 0);
        String mode = getIntent().getStringExtra("mode");

        TextView tvResult = findViewById(R.id.tvResult);
        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvWeak = findViewById(R.id.tvWeak);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnRetry = findViewById(R.id.btnRetry);

        int wrong = total - correct;
        int percent = total > 0 ? (correct * 100 / total) : 0;

        tvResult.setText(percent >= 80 ? "훌륭해요! 🎉" : percent >= 50 ? "잘하고 있어요! 💪" : "조금 더 연습해요 📖");
        tvScore.setText(total + "장 중 " + correct + "장 맞힘 (" + percent + "%)");

        List<WordData> words = WordRepository.loadWords(this);
        int weakCount = 0;
        for (WordData w : words) if (w.isWeak) weakCount++;
        tvWeak.setText("취약 카드: " + weakCount + "개");

        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });

        btnRetry.setOnClickListener(v -> {
            Intent i = new Intent(this, FlashcardActivity.class);
            i.putExtra("mode", mode);
            startActivity(i);
            finish();
        });
    }
}
