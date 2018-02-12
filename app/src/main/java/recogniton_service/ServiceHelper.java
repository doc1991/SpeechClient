package recogniton_service;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.bill.Activities.R;

import recognize.AssistanListener;
import tts.SpeecHelper;
import tts.TtsProgressListener;

/**
 * Created by bill on 11/28/17.
 */

public abstract class ServiceHelper extends RecognitionService implements AssistanListener, TtsProgressListener {

    private final String TAG = this.getClass().getSimpleName();
    private SpeecHelper talkengine;
    private String startMessage = "";
    private String waitMessage = "";
    private boolean isActivated;
    private boolean isFinishedTts;

    @Override
    public void onCreate() {
        super.onCreate();
        Init();
    }

    private void Init() {
        Log.i(TAG, "Initialization object and messages");
        isFinishedTts =true;
        talkengine = new SpeecHelper(getApplicationContext(), this);
        startMessage = getApplicationContext().getResources().getString(R.string.StartMessage);
        waitMessage = getApplicationContext().getResources().getString(R.string.WaitMessage);
        setRecognition(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        free();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        free();
        return false;
    }

    @Override
    public void onStartTalk() {
        //on start talking assistant close recognition and enable beep
        isFinishedTts =false;
        Mute(false);
        runCloseSpeech();
    }


    @Override
    public void onEndTalk() {
        //on end talking assistant start recognition
        isFinishedTts =true;
        runStartSpeech();

    }

    @Override
    public void OnSpeechLiveResult(String LiveResult) {
    }


    /*@Override
    public void OnSpeechError(int Error) {
        //if recognition is listening user and reach speech time out time show message
        if (isActivated) {
            Toast.makeText(this, "Η αναγνώριση τερματίζει", Toast.LENGTH_SHORT).show();
        }
        isActivated = false;
        //close recognition if not continuous
        CancelOnNotContinuous();
        //mute audio beep
        Mute(true);
    }*/

    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "user ends speaking");

    }

    //function for starting tts speaking
    public void StartInteract() {
        Log.i(TAG, "Assistant starting speaking");
        isActivated = true;

        StartMessage(startMessage);
        if (!isContinuousSpeechRecognition())
            setFirst(true);
    }

    public void StartMessage(String msg) {

        talkengine.speak(msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    //free resources
    private void free() {
        Log.i(TAG, "Resources is free");
        if (talkengine != null)
            talkengine.cancel();
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        Log.i(TAG,"boolean activated is "+activated);
        isActivated = activated;

    }

    public boolean isFinishedTts() {
        return isFinishedTts;
    }

    public void setFinishedTts(boolean finishedTts) {
        isFinishedTts = finishedTts;
    }
}
