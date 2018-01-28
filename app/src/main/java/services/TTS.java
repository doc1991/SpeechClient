package services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
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

    private final IBinder mBinder = new LocalBinder();

    public static final String MESSAGE_STRING = "Message";
    public static final String HAS_RECOGNITION_EXTRA = "HasRecognition";
    public static final String HAS_WIT_EXTRA = "HasWit";
    private static final HashMap<String, String> map = new HashMap<String, String>();
    private TextToSpeech tts;
    private String msg;
    private boolean hasRecognition;
    private boolean hasWit;
    STT sstService;
    boolean mBound = false;



    public class LocalBinder extends Binder {
        public TTS getService() {
            // Return this instance of LocalService so clients can call public methods
            return TTS.this;
        }
    }




    @Override
    public void onCreate() {
        super.onCreate();
        tts = new TextToSpeech(getApplicationContext(), this);
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
        tts.setPitch(1.3f);
        tts.setSpeechRate(1f);
        Intent sst = new Intent(getApplicationContext(),STT.class);
        bindService(sst,mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG","StartCommand");

        return super.onStartCommand(intent, flags, startId);

    }


    public IBinder onBind(Intent intent) {
        return mBinder;
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
                        if(mBound){
                            Log.d("DEBUG","BOUND TO START LISTEN");
                            sstService.startlisten(hasWit);
                        }
                    }

                }

                @Override
                public void onError(String s) {

                }
            });

        }

    }

    public void StartSpeak(Intent intent){
        msg = intent.getStringExtra(MESSAGE_STRING);
        hasRecognition = intent.getBooleanExtra(HAS_RECOGNITION_EXTRA,false);
        hasWit = intent.getBooleanExtra(HAS_WIT_EXTRA,false);
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, map);
    }

    public void cancel() {

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            STT.LocalBinder binder = (STT.LocalBinder) service;
            sstService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}
