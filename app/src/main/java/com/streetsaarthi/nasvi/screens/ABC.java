package com.streetsaarthi.nasvi.screens;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.streetsaarthi.nasvi.R;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ABC extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);

        Button button = (Button) findViewById(R.id.btDownload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = null;
                try {
                    word = callUrlAndParseResult("en", "hi", "hello");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Log.e("TAG", "AAAAAA "+word);
            }
        });
    }

    private String callUrlAndParseResult(String langFrom, String langTo,
                                         String word) throws Exception
    {

        String url = "https://translate.googleapis.com/translate_a/single?"+
                "client=gtx&"+
                "sl=" + langFrom +
                "&tl=" + langTo +
                "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return parseResult(response.toString());
    }

    private String parseResult(String inputJson) throws Exception
    {
        /*
         * inputJson for word 'hello' translated to language Hindi from English-
         * [[["नमस्ते","hello",,,1]],,"en"]
         * We have to get 'नमस्ते ' from this json.
         */

        JSONArray jsonArray = new JSONArray(inputJson);
        JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
        JSONArray jsonArray3 = (JSONArray) jsonArray2.get(0);

        return jsonArray3.get(0).toString();
    }


}
