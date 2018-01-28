package services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import utils.Constants;

public class Maestro extends Service {

    public String TAG = "Maestro Service";




    public Maestro() {

    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter iff= new IntentFilter(Constants.MaestroComm);
        LocalBroadcastManager.getInstance(this).registerReceiver(MaestroReceiver,iff);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //Broadcast Receivers Endpoints
    private BroadcastReceiver MaestroReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            intent.getSerializableExtra("WitOBJ");

            CommandReceived();

        }
    };

    private void CommandReceived(){

    }
}
