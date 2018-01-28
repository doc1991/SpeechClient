package actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gvol on 17/12/2017.
 */

public class Action {
    //Config Variables
    private String TAG = "ActionClass";
    public String IntentAction = null;
    public Uri IntentURIprefix = Uri.EMPTY;
    public String UriQuery = "";
    public int Flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK;
    public boolean RequiresVerification = false;
    public boolean MultiStageComm = false;
    public boolean RequiresUri = false;
    public HashMap<String,String> data = new HashMap<>();
    public HashMap<String,String> data_requests = new HashMap<>();
    public String Stage = "IN";

    //Messages
    public String VERIFY_MESSAGE = "";
    public String ACTION_FAILED = "";
    public String NOT_FOUND = "";
    public String LAUNCHED = "";

    //Constructor
    public Action() {
    }


    //Run the Intent
    public void runIntent(Context con) {
        Intent curIntent = CreateIntent();
        con.startActivity(curIntent);
    }

    //Helper Classes
    private Intent CreateIntent() {
        Intent runInt;
        if (RequiresUri) {
            runInt = new Intent(IntentAction, Uri.parse(IntentURIprefix + Uri.encode(UriQuery, "UTF-8")));
        } else {
            runInt = new Intent(IntentAction);
        }
        runInt.setFlags(Flags);
        return runInt;
    }

    private ArrayList<String> getEmptyExtras(Bundle extras) {
        ArrayList<String> empties = new ArrayList<>();
        for (String key : extras.keySet()) {
            Object value = extras.get(key);
            if (value == "") {
                empties.add(key);
            }
        }
        return empties;
    }


}

