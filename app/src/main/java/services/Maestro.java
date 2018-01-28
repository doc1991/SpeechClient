package services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import actions.Action;
import actions.Call;
import utils.Constants;
import utils.jsonparsers.Witobj;

public class Maestro extends Service {

    public String TAG = "Maestro Service";
    private final IBinder mBinder = new LocalBinder();
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



    //Broadcast Receivers Endpoints
    private BroadcastReceiver MaestroReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CommandReceived(intent);

        }
    };

    //STORIES!!

    private void CommandReceived(Intent intent){
        String sender1 = intent.getStringExtra("Sender");
        Log.d(TAG, sender1);

        if(sender1.equals("BTN")){
            firstquery();
        }
        if(sender1.equals("TTS")){
        }
        if(sender1.equals("WIT")){
            Witobj resp = (Witobj) intent.getSerializableExtra("WitOBJ");
            String type = resp.getEntities().getIntent().get(0).getValue();

            if (type.equals("make_call")){
                Call callit = new Call();


            }


        }


    }


    //Helper
    private void firstquery(){
    Intent msg = new Intent();
    msg.putExtra(TTS.MESSAGE_STRING, "Παρακαλώ, πείτε μου πως μπορώ να βοηθήσω");
    msg.putExtra(TTS.HAS_RECOGNITION_EXTRA,true);
    ttsService.StartSpeak(msg);
    }


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


