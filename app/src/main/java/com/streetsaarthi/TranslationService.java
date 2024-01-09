package com.streetsaarthi;

import android.os.AsyncTask;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TranslationService extends AsyncTask<String, Void, String> {

    private String apiKey;
    private String sourceLanguage;
    private String targetLanguage;
    private TranslationListener listener;

    public TranslationService(String apiKey, String sourceLanguage, String targetLanguage, TranslationListener listener) {
        this.apiKey = apiKey;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String textToTranslate = params[0];

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://translation.googleapis.com/language/translate/v2?key=" + apiKey + "&q=" + textToTranslate + "&source=" + sourceLanguage + "&target=" + targetLanguage)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            String jsonResponse = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            String translatedText = jsonObject.getAsJsonObject("data").getAsJsonArray("translations").get(0).getAsJsonObject().get("translatedText").getAsString();
            return translatedText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (listener != null) {
            if (result != null) {
                listener.onTranslationSuccess(result);
            } else {
                listener.onTranslationError("An error occurred while translating the text");
            }
        }
    }

    public interface TranslationListener {
        void onTranslationSuccess(String translatedText);
        void onTranslationError(String errorMessage);
    }
}