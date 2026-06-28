package com.flashcard.chinese;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Locale;

public class TtsManager {

    private static final String TAG = "TtsManager";
    private TextToSpeech tts;
    private boolean isReady = false;

    public TtsManager(Context ctx, Runnable onReady) {
        tts = new TextToSpeech(ctx, status -> {
            if (status == TextToSpeech.SUCCESS) {
                isReady = true;
                if (onReady != null) onReady.run();
            } else {
                Log.e(TAG, "TTS init failed: " + status);
            }
        });
    }

    public void speakChinese(String text) {
        if (!isReady) return;
        int result = tts.setLanguage(Locale.SIMPLIFIED_CHINESE);
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // fallback to zh-CN string
            tts.setLanguage(new Locale("zh", "CN"));
        }
        Bundle params = new Bundle();
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "zh_utterance");
    }

    public void speakKorean(String text) {
        if (!isReady) return;
        int result = tts.setLanguage(new Locale("ko", "KR"));
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.w(TAG, "Korean TTS not supported");
        }
        Bundle params = new Bundle();
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "ko_utterance");
    }

    public boolean isReady() {
        return isReady;
    }

    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
