package recogniton_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.bill.Activities.R;

import applications.Action;
import applications.AppIntentService;
import applications.CallTel;
import applications.Constatns;
import applications.Switcher;
import recognize.Constants;
import utils.jsonparsers.Witobj;

/**
 * Created by bill on 12/12/17.
 */

public abstract class Interact extends SpeechService {

    private final String TAG = this.getClass().getSimpleName();
    private int RETRY_FLAG = 0;
    private int RETRY_LIMIT = 5;
    private String result ="";
    private boolean HAS_APP = false;
    private Action app;

    final static public String MaestroComm = "Maestro Communication Channell";
    @Override
    public void onCreate() {
        super.onCreate();
        /*IntentFilter broadcastFilter = new IntentFilter(ResponseReceiver.LOCAL_ACTION);
        ResponseReceiver receiver = new ResponseReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(receiver, broadcastFilter);
        */
        app = new Action();
        IntentFilter iff= new IntentFilter(MaestroComm);
        LocalBroadcastManager.getInstance(this).registerReceiver(MaestroReceiver,iff);
    }
    private void CommandReceived(Intent intent){
        String sender1 = intent.getStringExtra("Sender");
        Log.d(TAG, sender1);


        //Button Press
        if(sender1.equals("BTN")){
            // Retry flag initialization for the NULL loop of WIT
            RETRY_FLAG = 0;
            //app = new Action();
            speak(getString(R.string.helper_prompt),true);
        }

        //Speech end without user feedback
        /*if(sender1.equals("TTS")){
            Log.i(TAG,"end without feedback");

        }*/

        //Wit Object Response
        if(sender1.equals("WIT")){

            Witobj resp = (Witobj) intent.getSerializableExtra("WitOBJ");

            //IF response = null
            //Retry to catch user command - ends after RETRY_LIMIT


            Log.i(TAG,"stage is : "+app.Stage);

            if(resp.getEntities().getIntent() == null && app.Stage.equals(Constatns.IN_STAGE)){
                app.Stage ="NS";
                if (RETRY_FLAG < RETRY_LIMIT){
                    speak(getString(R.string.command_repeat),true);
                }
                else {
                    speak(getString(R.string.retry_silent_stop),false);
                }
                RETRY_FLAG = RETRY_FLAG + 1;
            }


            //Initialization Phase
            if (app.Stage.equals(Constatns.IN_STAGE)){

                String type = resp.getEntities().getIntent().get(0).getValue();
                Log.i(TAG,"type = "+type);
                app = Switcher.selectActionbyType(app,type);
            }

            //Data Fill Phase
            if (app.Stage.equals(Constatns.CH_STAGE)){

                //One time no multistage comminicators to pass data from appdata
                if(!app.MultiStageCommFromStart && resp.getEntities().getAppData() != null && resp.getEntities().getAppData().get(0).getConfidence()> 0.8){
                    app.data.put(app.Current_Key,resp.getEntities().getAppData().get(0).getValue());
                }

                //Multi stage comm gatherer
                if(resp.getText() != null && app.waiting_data){
                    app.data.put(app.Current_Key,resp.getText());
                    Log.i(TAG,"data value = "+resp.getText());
                }

                //Multi Stage comm Loop
                for(String key:app.data.keySet()) {
                    Log.i(TAG,"data value = "+app.data.get(key));
                    if (app.data.get(key) == null) {
                        speak(app.data_requests.get(key),true);
                        app.Current_Key = key;
                        app.waiting_data = true;
                        break;
                    }
                    else{
                        app.Stage = Constatns.TR_STAGE;
                    }
                }
            }
            if (app.Stage.equals(Constatns.TR_STAGE)){
                app = Switcher.transforminfo(app,getApplicationContext());
            }

            if (app.Stage.equals(Constatns.VR_STAGE)){

                speak(app.VERIFY_MESSAGE,true);
                Log.i(TAG,"result= "+result);



            }
            Log.i(TAG,"stage after vr is : "+app.Stage);
            if (app.Stage.equals(Constatns.RUN_STAGE)){
                app.runIntent(getApplicationContext());
                app.Stage = Constatns.CP_STAGE;
            }

            if (app.Stage.equals(Constatns.NF_STAGE)){

                speak(app.NOT_FOUND,false);
                Log.i(TAG,"message not found  = "+app.NOT_FOUND);

            }
            if (app.Stage.equals(Constatns.CP_STAGE)){
                Log.i(TAG,"completed");
                setActivated(false);
                app.Init();
                SendMessage("");
            }
            if(app.Stage.equals("NS")){
                app.Stage=Constatns.IN_STAGE;
                app.Init();
                SendMessage("");
            }


        }
    }
    private BroadcastReceiver MaestroReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CommandReceived(intent);
        }
    };
    private void speak (String message,boolean recognize_after){
        Intent msg = new Intent();

        StartMessage(message);
    }

    @Override
    public void OnSpeechResult(String Result) {
        super.OnSpeechResult(Result);

        this.result = Result;
        if(result.contains("ναι")) {

            app.Stage = Constatns.RUN_STAGE;
        }
        else if(result.contains("όχι") ) {
            Log.d(TAG,"app stage : "+app.Stage);

            app.Stage = Constatns.CP_STAGE;
            speak("όπως επιθυμείτε", false);
        }
    }

    @Override
    public void OnSpeechError(int Error) {
        super.OnSpeechError(Error);
        //isinteractive = false;
        if (isActivated()) {
            setActivated(false);
            app.Init();
            SendMessage("");
        }
    }
}
