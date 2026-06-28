package com.flashcard.chinese;

public class WordData {
    public String chinese;      // 한자
    public String pinyin;       // 병음 (예: "nǐ hǎo")
    public String korean;       // 한국어 의미
    public String category;     // 카테고리
    public int wrongCount;      // 틀린 횟수
    public int correctCount;    // 맞힌 횟수
    public boolean isWeak;      // 취약 카드 여부

    public WordData(String chinese, String pinyin, String korean, String category) {
        this.chinese = chinese;
        this.pinyin = pinyin;
        this.korean = korean;
        this.category = category;
        this.wrongCount = 0;
        this.correctCount = 0;
        this.isWeak = false;
    }
}
