package com.flashcard.chinese;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlashcardActivity extends AppCompatActivity {

    private List<WordData> deck;
    private List<WordData> allWords;
    private int currentIndex = 0;
    private boolean isFlipped = false;
    private String mode;

    private TtsManager tts;

    // Views
    private CardView cardFront, cardBack;
    private TextView tvChinese, tvPinyin, tvKorean, tvCategory;
    private TextView tvProgress, tvCounter;
    private ProgressBar progressBar;
    private LinearLayout llAnswerButtons;
    private Button btnCorrect, btnWrong;
    private TextView tvWeakBadge;
    private ImageButton btnSpeakFront, btnSpeakBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        mode = getIntent().getStringExtra("mode");
        allWords = WordRepository.loadWords(this);
        buildDeck();

        bindViews();
        setupTts();
        showCard();
    }

    private void buildDeck() {
        deck = new ArrayList<>();
        if ("weak".equals(mode)) {
            for (WordData w : allWords) if (w.isWeak) deck.add(w);
        } else {
            deck.addAll(allWords);
        }
        Collections.shuffle(deck);
    }

    private void bindViews() {
        cardFront = findViewById(R.id.cardFront);
        cardBack = findViewById(R.id.cardBack);
        tvChinese = findViewById(R.id.tvChinese);
        tvPinyin = findViewById(R.id.tvPinyin);
        tvKorean = findViewById(R.id.tvKorean);
        tvCategory = findViewById(R.id.tvCategory);
        tvProgress = findViewById(R.id.tvProgress);
        tvCounter = findViewById(R.id.tvCounter);
        progressBar = findViewById(R.id.progressBar);
        llAnswerButtons = findViewById(R.id.llAnswerButtons);
        btnCorrect = findViewById(R.id.btnCorrect);
        btnWrong = findViewById(R.id.btnWrong);
        tvWeakBadge = findViewById(R.id.tvWeakBadge);
        btnSpeakFront = findViewById(R.id.btnSpeakFront);
        btnSpeakBack = findViewById(R.id.btnSpeakBack);

        cardFront.setOnClickListener(v -> flipCard());
        cardBack.setOnClickListener(v -> flipCard());

        btnCorrect.setOnClickListener(v -> markAnswer(true));
        btnWrong.setOnClickListener(v -> markAnswer(false));

        btnSpeakFront.setOnClickListener(v -> {
            if (deck != null && currentIndex < deck.size())
                tts.speakChinese(deck.get(currentIndex).chinese);
        });
        btnSpeakBack.setOnClickListener(v -> {
            if (deck != null && currentIndex < deck.size())
                tts.speakKorean(deck.get(currentIndex).korean);
        });

        progressBar.setMax(deck.size());
    }

    private void setupTts() {
        tts = new TtsManager(this, null);
    }

    private void showCard() {
        if (currentIndex >= deck.size()) {
            finishSession();
            return;
        }
        WordData word = deck.get(currentIndex);
        isFlipped = false;

        // Front face
        tvChinese.setText(word.chinese);
        tvPinyin.setText(ToneColorizer.colorize(word.pinyin));
        tvCategory.setText(word.category);

        // Back face
        tvKorean.setText(word.korean);

        // Weak badge
        tvWeakBadge.setVisibility(word.isWeak ? View.VISIBLE : View.GONE);

        // Show front, hide back
        cardFront.setVisibility(View.VISIBLE);
        cardBack.setVisibility(View.GONE);
        llAnswerButtons.setVisibility(View.GONE);

        // Progress
        tvProgress.setText((currentIndex + 1) + " / " + deck.size());
        tvCounter.setText("맞힘 " + word.correctCount + " · 틀림 " + word.wrongCount);
        progressBar.setProgress(currentIndex);
    }

    private void flipCard() {
        isFlipped = !isFlipped;
        if (isFlipped) {
            // Animate front → back
            cardFront.setVisibility(View.GONE);
            cardBack.setVisibility(View.VISIBLE);
            llAnswerButtons.setVisibility(View.VISIBLE);
            // Auto-speak Korean on flip
            if (deck != null && currentIndex < deck.size())
                tts.speakKorean(deck.get(currentIndex).korean);
        } else {
            cardFront.setVisibility(View.VISIBLE);
            cardBack.setVisibility(View.GONE);
            llAnswerButtons.setVisibility(View.GONE);
        }
    }

    private void markAnswer(boolean correct) {
        WordData word = deck.get(currentIndex);
        // Update in allWords list
        for (WordData w : allWords) {
            if (w.chinese.equals(word.chinese)) {
                if (correct) {
                    w.correctCount++;
                    // Remove weak flag if correct 2 times in a row
                    if (w.correctCount >= 2 && w.wrongCount == 0) w.isWeak = false;
                    if (w.wrongCount > 0 && w.correctCount >= w.wrongCount * 2) w.isWeak = false;
                } else {
                    w.wrongCount++;
                    w.isWeak = true;
                }
                word.correctCount = w.correctCount;
                word.wrongCount = w.wrongCount;
                word.isWeak = w.isWeak;
                break;
            }
        }
        WordRepository.saveWords(this, allWords);
        currentIndex++;
        showCard();
    }

    private void finishSession() {
        int correct = 0, wrong = 0;
        for (WordData w : deck) {
            if (w.correctCount > 0) correct++;
            if (w.wrongCount > 0) wrong++;
        }
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("total", deck.size());
        i.putExtra("correct", correct);
        i.putExtra("mode", mode);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) tts.shutdown();
    }
}
