package com.streetsaarthi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions

//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.services.translate.Translate;
//import com.google.api.services.translate.model.TranslationsListResponse;
import java.util.Arrays


//import org.json.JSONObject


class MainActivity2 : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var button: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webpage)
        webView = findViewById(R.id.webView)
        button = findViewById(R.id.button)

        button.setOnClickListener {

//            initLanguage(
//                "EN",
//                "PN",
//                "Hi i am dinesh"
//            )
//            val googleTranslate = GoogleTranslate()
//// Perform the translation by invoking the execute method, but first save the result in a String.
//// The second parameter is the source language, the third is the terget language
//// Perform the translation by invoking the execute method, but first save the result in a String.
//// The second parameter is the source language, the third is the terget language
//            val result: String =
//                googleTranslate.execute("the text to be translated", "en", "de").get()

//
//
//            try {
//
//                /*
//                      * Objects needed for the translate object
//                      */
//                val netHttpTransport = NetHttpTransport()
//                val jacksonFactory = JacksonFactory()
//
//                /*
//                      * Creating the Google Translate object
//                      */
//                val translate = Translate.Builder(netHttpTransport, jacksonFactory, null).build()
//
//                /*
//                      * Setting the textToTranslate, the API_KEY and TARGET_LANGUAGE
//                      */
//                val listToTranslate = translate.Translations().list(
//                    Arrays.asList("the text to be translated"), "de"
//                ).setKey(getString(R.string.google_place_key))
//
//                /*
//                      * If you want to let Google detects the language automatically, remove the next line
//                      * This line set the source language of the translated text
//                      */listToTranslate.setSource("en")
//
//                /*
//                      * Executing the translation and saving the response in the response object
//                      */
//                val response = listToTranslate.execute()
//
//                /*
//                      * The response has the form of: {"translatedText":"blabla"}
//                      * We need only the translated text between the second double quotes pair
//                      * therefore using getTranslatedText
//                      */response.translations[0].translatedText
//            } catch (e: Exception) {
//                Log.e("Google Response ", e.message!!)
//
//                /*
//                      * I would return empty string if there is an error
//                      * to let the method which invoked the translating method know that there is an error
//                      * and subsequently it deals with it
//                      */""
//            }
//        }
//
//


        }
    }



//    private fun initLanguage(idSL: Any?, idTL: Any?, text: String?) {
//        val option = FirebaseTranslatorOptions.Builder()
//            .setSourceLanguage(idSL as Int)
//            .setTargetLanguage(idTL as Int)
//            .build()
//        val textTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(option)
//
//        // Download model for the first time
//
//        textTranslator.downloadModelIfNeeded()
//            .addOnSuccessListener {
//                Log.e("MainActivity", "Download Success")
//            }
//            .addOnFailureListener {
//                Log.e("MainActivity", "Download Failed: $it")
//            }
//
//
//        // Translate text from source language to target language related with model
//        textTranslator.translate(text.toString())
//            .addOnSuccessListener {
//                //tvResult.text = it
//                Log.e("MainActivity", "TranslateAA Success $it")
//            }.addOnFailureListener {
//                Log.e("MainActivity", "TranslateAA Failed: $it")
//            }
//    }

override fun onBackPressed() {
    if (webView.canGoBack())
        webView.goBack()
    else
        super.onBackPressed()
}

}