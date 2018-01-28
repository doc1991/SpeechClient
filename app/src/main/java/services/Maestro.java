package services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.bill.Activities.R;

import actions.Action;
import utils.Constants;
import utils.ContactUtils;
import utils.jsonparsers.Witobj;

public class Maestro extends Service {

    public String TAG = "Maestro Service";
    private final IBinder mBinder = new LocalBinder();
    private int RETRY_FLAG = 0;
    private int RETRY_LIMIT = 5;
    TTS ttsService;
    boolean ttsbound = false;


    public Maestro() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent saycomm = new Intent(getApplicationContext(),TTS.class);
        bindService(saycomm,mConnection,Context.BIND_AUTO_CREATE);
        startService(saycomm);
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter iff= new IntentFilter(Constants.MaestroComm);
        LocalBroadcastManager.getInstance(this).registerReceiver(MaestroReceiver,iff);
    }


    //STORIES!!

    private void CommandReceived(Intent intent){
        String sender1 = intent.getStringExtra("Sender");
        Log.d(TAG, sender1);
        Action app = new Action();

        //Button Press
        if(sender1.equals("BTN")){
            // Retry flag initialization for the NULL loop of WIT
            RETRY_FLAG = 0;
            speak(getString(R.string.helper_prompt),true);
        }

        //Speech end without user feedback
        if(sender1.equals("TTS")){

        }

        //Wit Object Response
        if(sender1.equals("WIT")){
            Witobj resp = (Witobj) intent.getSerializableExtra("WitOBJ");


            //IF response = null
            //Retry to catch user command - ends after RETRY_LIMIT

            if(resp.getEntities() == null){
                if (RETRY_FLAG < RETRY_LIMIT){
                    speak(getString(R.string.command_repeat),true);
                }
                else {
                    speak(getString(R.string.retry_silent_stop),false);
                }
                RETRY_FLAG = RETRY_FLAG + 1;
            }


            //Response parcelable
            if (app.Stage.equals("IN")){
                String type = resp.getEntities().getIntent().get(0).getValue();

            }
            if (app.Stage.equals("CH")){

            }
            if (app.Stage.equals("AS")){

            }
            if (app.Stage.equals("VR")){

            }
            if (app.Stage.equals())



        }


    }



    //Helper methods

    private void speak (String message,boolean recognize_after){
        Intent msg = new Intent();
        msg.putExtra(TTS.MESSAGE_STRING, message);
        msg.putExtra(TTS.HAS_RECOGNITION_EXTRA,recognize_after);
        ttsService.StartSpeak(msg);
    }

    //Broadcast Receivers Endpoints

    private BroadcastReceiver MaestroReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CommandReceived(intent);
        }
    };

    //Bind-Binder BoilerPlate

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            TTS.LocalBinder binder = (TTS.LocalBinder) service;
            ttsService = binder.getService();
            ttsbound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            ttsbound = false;
        }
    };

    public class LocalBinder extends Binder {
        public Maestro getService() {
            // Return this instance of LocalService so clients can call public methods
            return Maestro.this;
        }
    }
}