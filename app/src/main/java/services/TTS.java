package services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by bill on 11/23/17.
 */

public class TTS extends Service implements TextToSpeech.OnInitListener {
    public static final String MESSAGE_STRING = "Message";
    public static final String HAS_RECOGNITION_EXTRA = "HasRecognition";
    public static final String HAS_WIT_EXTRA = "HasWit";
    private static final HashMap<String, String> map = new HashMap<String, String>();
    private TextToSpeech tts;
    private String msg;
    private boolean hasRecognition;
    private boolean hasWit;


    @Override
    public void onCreate() {
        super.onCreate();
        tts = new TextToSpeech(getApplicationContext(), this);
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
        tts.setPitch(1.3f);
        tts.setSpeechRate(1f);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG","StartCommand");
        msg = intent.getStringExtra(MESSAGE_STRING);
        hasRecognition = intent.getBooleanExtra(HAS_RECOGNITION_EXTRA,false);
        hasWit = intent.getBooleanExtra(HAS_WIT_EXTRA,false);
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, map);
        return super.onStartCommand(intent, flags, startId);

    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.getDefault());
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String s) {

                }

                @Override
                public void onDone(String s) {
                    if (hasRecognition){
                        Intent recognitionservice = new Intent(getBaseContext(),STT.class);
                        recognitionservice.putExtra(HAS_WIT_EXTRA,hasWit);
                        startService(recognitionservice);
                    }

                }

                @Override
                public void onError(String s) {

                }
            });

        }

    }

    public void cancel() {

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
