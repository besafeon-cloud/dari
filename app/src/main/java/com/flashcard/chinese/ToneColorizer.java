package com.flashcard.chinese;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * 성조 색상 표시
 * 1성(ā ē ī ō ū) → 빨강
 * 2성(á é í ó ú) → 주황
 * 3성(ǎ ě ǐ ǒ ǔ) → 초록
 * 4성(à è ì ò ù) → 파랑
 * 경성(a e i o u) → 회색
 */
public class ToneColorizer {

    private static final int COLOR_TONE1 = Color.parseColor("#E53935"); // 빨강
    private static final int COLOR_TONE2 = Color.parseColor("#FB8C00"); // 주황
    private static final int COLOR_TONE3 = Color.parseColor("#43A047"); // 초록
    private static final int COLOR_TONE4 = Color.parseColor("#1E88E5"); // 파랑
    private static final int COLOR_TONE0 = Color.parseColor("#9E9E9E"); // 회색(경성)

    private static final String TONE1_CHARS = "āēīōūǖ";
    private static final String TONE2_CHARS = "áéíóúǘ";
    private static final String TONE3_CHARS = "ǎěǐǒǔǚ";
    private static final String TONE4_CHARS = "àèìòùǜ";

    public static SpannableStringBuilder colorize(String pinyin) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        // Split by spaces → each syllable gets its own color
        String[] syllables = pinyin.split(" ");
        for (int i = 0; i < syllables.length; i++) {
            String syl = syllables[i];
            int tone = getTone(syl);
            int color = getColor(tone);
            int start = sb.length();
            sb.append(syl);
            sb.setSpan(new ForegroundColorSpan(color), start, sb.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (i < syllables.length - 1) sb.append(" ");
        }
        return sb;
    }

    private static int getTone(String syllable) {
        for (char c : syllable.toCharArray()) {
            if (TONE1_CHARS.indexOf(c) >= 0) return 1;
            if (TONE2_CHARS.indexOf(c) >= 0) return 2;
            if (TONE3_CHARS.indexOf(c) >= 0) return 3;
            if (TONE4_CHARS.indexOf(c) >= 0) return 4;
        }
        return 0;
    }

    private static int getColor(int tone) {
        switch (tone) {
            case 1: return COLOR_TONE1;
            case 2: return COLOR_TONE2;
            case 3: return COLOR_TONE3;
            case 4: return COLOR_TONE4;
            default: return COLOR_TONE0;
        }
    }
}
