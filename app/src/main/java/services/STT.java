package services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by gvol on 10/12/2017.
 */

public class STT extends Service implements RecognitionListener {

    private final IBinder mBinder = new LocalBinder();


    public static final String HAS_WIT = "hasWit";
    private String LOG_TAG = null;
    private String msg ="";
    private Intent SpeechIntent;
    private SpeechRecognizer speech;
    private boolean hasWit;
    Handler mainHandler;

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    public class LocalBinder extends Binder {
        public STT getService() {
            // Return this instance of LocalService so clients can call public methods
            return STT.this;
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();
        mainHandler = new Handler();
        LOG_TAG = this.getClass().getSimpleName();
        Log.i(LOG_TAG, "In onCreate");
        speech = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        speech.setRecognitionListener(this);
        SpeechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SpeechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        SpeechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());
        SpeechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        SpeechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        SpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Called by the system every time a client explicitly starts the service by calling
     * {@link Context#startService}, providing the arguments it supplied and a
     * unique integer token representing the start request.  Do not call this method directly.
     * <p>
     * <p>For backwards compatibility, the default implementation calls
     * {@link #onStart} and returns either {@link #START_STICKY}
     * or {@link #START_STICKY_COMPATIBILITY}.
     * <p>
     * <p>If you need your application to run on platform versions prior to API
     * level 5, you can use the following model to handle the older {@link #onStart}
     * callback in that case.  The <code>handleCommand</code> method is implemented by
     * you as appropriate:
     * <p>
     * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/ForegroundService.java
     * start_compatibility}
     * <p>
     * <p class="caution">Note that the system calls this on your
     * service's main thread.  A service's main thread is the same
     * thread where UI operations take place for Activities running in the
     * same process.  You should always avoid stalling the main
     * thread's event loop.  When doing long-running operations,
     * network calls, or heavy disk I/O, you should kick off a new
     * thread, or use {@link AsyncTask}.</p>
     *
     * @param intent  The Intent supplied to {@link Context#startService},
     *                as given.  This may be null if the service is being restarted after
     *                its process has gone away, and it had previously returned anything
     *                except {@link #START_STICKY_COMPATIBILITY}.
     * @param flags   Additional data about this start request.
     * @param startId A unique integer representing this specific request to
     *                start.  Use with {@link #stopSelfResult(int)}.
     * @return The return value indicates what semantics the system should
     * use for the service's current started state.  It may be one of the
     * constants associated with the {@link #START_CONTINUATION_MASK} bits.
     * @see #stopSelfResult(int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /* Init Recognition */



        return START_NOT_STICKY;
    }

    /**
     * Called when the endpointer is ready for the user to start speaking.
     *
     * @param params parameters set by the recognition service. Reserved for future use.
     */
    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    /**
     * The user has started to speak.
     */
    @Override
    public void onBeginningOfSpeech() {

    }

    /**
     * The sound level in the audio stream has changed. There is no guarantee that this method will
     * be called.
     *
     * @param rmsdB the new RMS dB value
     */
    @Override
    public void onRmsChanged(float rmsdB) {

    }

    /**
     * More sound has been received. The purpose of this function is to allow giving feedback to the
     * user regarding the captured audio. There is no guarantee that this method will be called.
     *
     * @param buffer a buffer containing a sequence of big-endian 16-bit integers representing a
     *               single channel audio stream. The sample rate is implementation dependent.
     */
    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    /**
     * Called after the user stops speaking.
     */
    @Override
    public void onEndOfSpeech() {

    }

    /**
     * A network or recognition error occurred.
     *
     * @param error code is defined in {@link SpeechRecognizer}
     */
    @Override
    public void onError(int error) {

    }

    /**
     * Called when recognition results are ready.
     *
     * @param results the recognition results. To retrieve the results in {@code
     *                ArrayList<String>} format use {@link Bundle#getStringArrayList(String)} with
     *                {@link SpeechRecognizer#RESULTS_RECOGNITION} as a parameter. A float array of
     *                confidence values might also be given in {@link SpeechRecognizer#CONFIDENCE_SCORES}.
     */
    @Override
    public void onResults(Bundle results) {
        String query = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
        new WitResponse(getApplicationContext()).execute(query);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Intent intent = new Intent("SpeechPartialResults");
        intent.putExtra("Speech",partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d("PARTIAL","PARTIAL");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    public void startlisten(){
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                speech.startListening(SpeechIntent);
            }
        });

    }
}