package com.example.shrisha.listentome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    SpeechRecognizer speechRecognizer;
    Intent recognizerIntent;

    RecognitionListener recognitionListener;

    ImageButton imageButton;
    TextView textView;
    LinearLayout linearLayout;
    //Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textView = (TextView) findViewById(R.id.textView);
        //textView.setText("Preparing listener");
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        findViewById(R.id.scrollView).setVerticalScrollBarEnabled(true);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        imageButton = findViewById(R.id.btn_listener);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en_IN");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechRecognizer.startListening(recognizerIntent);
            }
        });

        /*if(PackageManager.PERMISSION_GRANTED != checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO)) {
            return;
        }*/

        if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            return;
        }
    }

    private TextView getTextView(String textMsg) {
        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        /*params.setMargins((int) R.dimen.default_margin,
                (int) R.dimen.default_margin,
                (int) R.dimen.default_margin,
                (int) R.dimen.default_margin);*/
/*        params.topMargin = (int) R.dimen.default_margin;
        params.leftMargin = (int) R.dimen.default_margin;*/

        TextView textView = new TextView(MainActivity.this);
        textView.setText(textMsg);
        textView.setTextSize(20);
        textView.setBackgroundColor(Color.argb(180,
                new Random().nextInt(256),
                new Random().nextInt(256),
                new Random().nextInt(256)));
        //textView.setTextSize();
        textView.setLayoutParams(params);

        return textView;
    }

    long lastKeyTime;
    Toast backPressToast;
    @Override
    public void onBackPressed() {
        long currentKeyTime = System.currentTimeMillis();
        if (currentKeyTime - lastKeyTime > 5000) {
            backPressToast = Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_LONG);
            backPressToast.show();
            lastKeyTime = currentKeyTime;
        } else {
            if(backPressToast != null) {
                backPressToast.cancel();
                backPressToast = null;
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            default:
                return super.onOptionsItemSelected(item);

            case R.id.device_settings:
                intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                startActivity(intent);
                return true;

            case R.id.app_settings:
                intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", MainActivity.this.getPackageName(), null));
                startActivity(intent);
                return true;

            case R.id.quit:
                finishAffinity();
                return true;

            case R.id.restart:
                recreate();
                return true;
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");

        super.onStart();

        //textView.setText("Preparing parameters");
        linearLayout.addView(getTextView("Preparing parameters"));
        imageButton.setImageResource(android.R.drawable.presence_audio_away);

        recognitionListener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d(TAG, "onReadyForSpeech: params: " + params.toString());
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d(TAG, "onBeginningOfSpeech");

            }

            @Override
            public void onRmsChanged(float rmsdB) {
                Log.d(TAG, "onRmsChanged: rmsdB: " + String.valueOf(rmsdB));

            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                Log.d(TAG, "onBufferReceived: buffer: " + buffer.toString());
            }

            @Override
            public void onEndOfSpeech() {
                Log.d(TAG, "onEndOfSpeech");
            }

            @Override
            public void onError(int error) {
                switch (error) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        //textView.setText("Audio error");
                        linearLayout.addView(getTextView("Audio error"));
                        Log.d(TAG, "onError: ERROR_AUDIO");
                        break;

                    case SpeechRecognizer.ERROR_CLIENT:
                        //textView.setText("Client-side error");
                        linearLayout.addView(getTextView("Client-side error"));
                        Log.d(TAG, "onError: ERROR_CLIENT");
                        break;

                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        //textView.setText("Necessary permissions missing.");
                        linearLayout.addView(getTextView("Necessary permissions missing."));
                        Log.d(TAG, "onError: ERROR_INSUFFICIENT_PERMISSIONS");
                        break;

                    case SpeechRecognizer.ERROR_NETWORK:
                        //textView.setText("Network failure");
                        linearLayout.addView(getTextView("Network failure"));
                        Log.d(TAG, "onError: ERROR_NETWORK");
                        break;

                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        //textView.setText("Network error: request timed out.");
                        linearLayout.addView(getTextView("Network error: request timed out."));
                        Log.d(TAG, "onError: ERROR_NETWORK_TIMEOUT");
                        break;

                    case SpeechRecognizer.ERROR_NO_MATCH:
                        //textView.setText("No match occurred");
                        linearLayout.addView(getTextView("No match occurred"));
                        Log.d(TAG, "onError: ERROR_NO_MATCH");
                        break;

                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        //textView.setText("Recogniser is busy. try restarting the app");
                        linearLayout.addView(getTextView("Recogniser is busy. try restarting the app"));
                        Log.d(TAG, "onError: ERROR_RECOGNIZER_BUSY");
                        break;

                    case SpeechRecognizer.ERROR_SERVER:
                        //textView.setText("Server-side error");
                        linearLayout.addView(getTextView("Server-side error"));
                        Log.d(TAG, "onError: ERROR_SERVER");
                        break;

                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        //textView.setText("Speech timeout");
                        linearLayout.addView(getTextView("Speech timeout"));
                        Log.d(TAG, "onError: ERROR_SPEECH_TIMEOUT");
                        break;

                    default:
                        //textView.setText("Something else happened");
                        linearLayout.addView(getTextView("Something else happened"));
                        Log.d(TAG, "onError: unknown");
                        break;
                }
            }

            @Override
            public void onResults(Bundle results) {
                //textView.setText("Finished listening. The results are here");
                linearLayout.addView(getTextView("Finished listening. The results are here"));
                Log.d(TAG, "onResults: results: " + results.toString());
                if(results != null && results.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)) {
                    List<String> words = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    float[] scores = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
                    /*for ((String word, float score): (words, scores)) {
                        linearLayout.addView(getTextView(word + " : " + String.valueOf(score)));
                    }*/
                    //int i = 0;
                    for(int j = 0; words.listIterator().hasNext() && j < scores.length; j++) {

                        linearLayout.addView(getTextView(words.get(j) + " : " + String.valueOf(scores[j])));
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                //textView.setText("Received partial results");
                linearLayout.addView(getTextView("Received partial results"));
                Log.d(TAG, "onPartialResults: partialResults: " + partialResults.toString());

            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                //textView.setText("SOME EVENT OCCURRED");
                linearLayout.addView(getTextView("SOME EVENT OCCURRED"));
                Log.d(TAG, "onEvent: eventCode: " + eventType + ", params: " + params.toString());
            }
        };

        speechRecognizer.setRecognitionListener(recognitionListener);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");

        super.onResume();
        //textView.setText("Listener ready. Start speaking..");
        linearLayout.addView(getTextView("Listener ready. Start speaking.."));
        imageButton.setImageResource(android.R.drawable.presence_audio_online);
        speechRecognizer.startListening(recognizerIntent);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");

        speechRecognizer.cancel();
        //textView.setText("Stopped listening");
        linearLayout.addView(getTextView("Stopped listening"));
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");

        super.onStop();
        //textView.setText("Removing parameters");
        linearLayout.addView(getTextView("Removing parameters"));
        recognitionListener = null;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");

        //textView.setText("Destroying listener");
        linearLayout.addView(getTextView("Destroying listener"));
        recognizerIntent = null;
        speechRecognizer = null;
        super.onDestroy();
    }
}
