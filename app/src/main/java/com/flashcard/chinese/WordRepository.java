package com.flashcard.chinese;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WordRepository {

    private static final String PREF_NAME = "flashcard_prefs";
    private static final String KEY_WORDS = "word_data";

    private static final WordData[] DEFAULT_WORDS = {
        new WordData("\u666e\u901a\u8bdd", "Putonghua", "\ubcf4\ud1b5\ud654", "\uc6a9\uc5b4"),
        new WordData("\u6c49\u8bed\u62fc\u97f3", "Hanyu Pinyin", "\ud55c\uc5b4\ubcd1\uc74c", "\uc6a9\uc5b4"),
        new WordData("\u7b80\u4f53\u5b57", "jiantizi", "\uac04\uccb4\uc790", "\uc6a9\uc5b4"),
        new WordData("\u6c49\u8bed", "Hanyu", "\ud55c\uc5b4", "\uc6a9\uc5b4"),
        new WordData("\u97f5\u6bcd", "yunmu", "\uc6b4\ubaa8", "\uc6a9\uc5b4"),
        new WordData("\u58f0\u6bcd", "shengmu", "\uc131\ubaa8", "\uc6a9\uc5b4"),
        new WordData("\u58f0\u8c03", "shengdiao", "\uc131\uc870", "\uc6a9\uc5b4"),
        new WordData("\u8f7b\u58f0", "qingsheng", "\uacbd\uc131", "\uc6a9\uc5b4"),
        new WordData("\u4f60\u4eec\u597d", "Nimen hao", "\uc548\ub155 \uc5ec\ub7ec\ubd84", "\uc778\uc0ac"),
        new WordData("\u5927\u5bb6\u597d", "Dajia hao", "\uc5ec\ub7ec\ubd84 \uc548\ub155\ud558\uc138\uc694", "\uc778\uc0ac"),
        new WordData("\u5bf9\u4e0d\u8d77", "Duibuqi", "\ubbf8\uc548\ud569\ub2c8\ub2e4", "\uc778\uc0ac"),
        new WordData("\u6ca1\u5173\u7cfb", "Mei guanxi", "\uad1c\ucc2e\uc544\uc694", "\uc778\uc0ac"),
        new WordData("\u606d\u559c", "Gongxi", "\ucd95\ud558\ud569\ub2c8\ub2e4", "\uc778\uc0ac"),
        new WordData("\u597d\u4e45\u4e0d\u89c1", "Haojiu bu jian", "\uc624\ub79c\ub9cc\uc774\uc57c", "\uc778\uc0ac"),
        new WordData("\u4e0d\u597d\u610f\u601d", "Bu hao yi si", "\uc8c4\uc1a1\ud569\ub2c8\ub2e4", "\uc778\uc0ac"),
        new WordData("\u540c\u5b66", "tongxue", "\ud559\uad50 \uce5c\uad6c", "\ub2e8\uc5b4"),
        new WordData("\u665a\u5b89", "Wan an", "\uc798 \uc790\uc694", "\uc778\uc0ac"),
        new WordData("\u8001\u5e08", "lao shi", "\uc120\uc0dd\ub2d8", "\ub2e8\uc5b4"),
        new WordData("\u5b66\u751f", "xuesheng", "\ud559\uc0dd", "\ub2e8\uc5b4"),
        new WordData("\u7f8e\u56fd", "Meiguo", "\ubbf8\uad6d", "\uad6d\uac00"),
        new WordData("\u65e5\u672c", "Riben", "\uc77c\ubcf8", "\uad6d\uac00"),
        new WordData("\u4e2d\u56fd", "Zhongguo", "\uc911\uad6d", "\uad6d\uac00"),
        new WordData("\u897f\u73ed\u7259", "Xibanya", "\uc2a4\ud398\uc778", "\uad6d\uac00"),
        new WordData("\u97e9\u56fd", "Hanguo", "\ud55c\uad6d", "\uad6d\uac00"),
        new WordData("\u4f60\u597d", "Ni hao", "\uc548\ub155\ud558\uc138\uc694", "\uc778\uc0ac"),
    };

    public static List<WordData> getDefaultWords() {
        List<WordData> list = new ArrayList<>();
        for (WordData w : DEFAULT_WORDS) list.add(w);
        return list;
    }

    public static void saveWords(Context ctx, List<WordData> words) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = new Gson().toJson(words);
        prefs.edit().putString(KEY_WORDS, json).apply();
    }

    public static List<WordData> loadWords(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_WORDS, null);
        if (json == null) return getDefaultWords();
        Type type = new TypeToken<List<WordData>>(){}.getType();
        List<WordData> loaded = new Gson().fromJson(json, type);
        if (loaded == null || loaded.isEmpty()) return getDefaultWords();
        return loaded;
    }

    public static void resetWords(Context ctx) {
        saveWords(ctx, getDefaultWords());
    }
}