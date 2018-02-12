package recogniton_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.bill.Activities.R;

import applications.AppIntentService;
import wit_connection.WitResponse;
import wit_connection.WitResponseMessage;

/**
 * Created by bill on 11/14/17.
 */

public abstract class SpeechService extends ServiceHelper  {

    public static final String BroadcastAction = "com.example.bill.Activities.MainActivity.UpdateGui";
    private final String TAG = this.getClass().getSimpleName();
    public static final String HAS_WIT = "hasWit";
    private String msg ="";
    private boolean hasWit;
    //broadcast for actions on clicking notification
    private final BroadcastReceiver NotAction = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "activated is " + isActivated());
            if (isActivated()) {
                StopSrecognition();
                setActivated(false);
            } else {
                StartInteract();

            }
        }
    };
    private Intent broadcastIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        //intent to communicate with foreground service
        broadcastIntent = new Intent(BroadcastAction);
        registerReceiver(NotAction, new IntentFilter("notification.action"));/**/
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "unregisterReceiver ");
        unregisterReceiver(NotAction);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    //speech listener methods
    @Override
    public void OnSpeechLiveResult(String LiveResult) {
        Log.i(TAG, "activated is " + isActivated() + " live result is " + LiveResult);
        //send the text from speech of user on main activity
        if (isActivated() ) {
            SendMessage(LiveResult);
        } else {
            SendMessage("");
        }
    }

    @Override
    public void OnSpeechResult(String Result) {
        Log.i(TAG, "activated is " + isActivated() + " final result is " + Result);



        if (isActivated() ) {
            new WitResponse(getApplicationContext()).execute(Result);
        } else if (Result.equals(getResources().getString(R.string.title_activity_gui))) {
            Mute(false);
            StartMessage(getApplicationContext().getResources().getString(R.string.StartMessage));
            setActivated(true);
        }
    }

    //send message to activity
    protected void SendMessage(String msg) {
        Log.i(TAG, "message of sendmessage method  is   " + msg);
        broadcastIntent.putExtra("result", msg);
        sendBroadcast(broadcastIntent);
    }


}
