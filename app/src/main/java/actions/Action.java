package actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by gvol on 17/12/2017.
 */

public class Action {
    //Config Variables
    private String TAG = "ActionClass";
    public LocalBroadcastManager localmanager = null;
    public Intent intent = null;
    public String IntentAction = "";
    public Uri IntentURIprefix = Uri.EMPTY;
    public String UriQuery = "";
    public int Flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK;
    public String IntentMimeType = "";
    public Bundle Extras = new Bundle();
    public boolean RequiresVerification = false;
    public boolean MultiStageComm = false;
    public boolean RequiresUri = false;
    public int StatusCode = 0;

    //Messages
    public String VERIFY_MESSAGE = "";
    public ArrayList<String> DATA_REQUESTS = new ArrayList<>();
    public String ACTION_FAILED = "";
    public String NOT_FOUND = "";
    public String LAUNCHED = "";

    //Constructor
    public Action() {
    }

    //Check for MissingAttributes
    public ArrayList<String> checkMissingAttributes() {
        ArrayList<String> Attributes = new ArrayList<>();
        if (RequiresUri && UriQuery.equals("")) {
            Attributes.add("UriQuery");
        }
        Attributes.addAll(getEmptyExtras(Extras));
        return Attributes;
    }

    //Run the Intent
    public int runIntent(Context con, Boolean verified) {
        Intent curIntent = CreateIntent();
        if (RequiresVerification && !verified) {
            StatusCode = 1;
            Log.d(TAG, "Activity Requires User Verification");
            return StatusCode;
        } else {
            con.startActivity(curIntent);
            StatusCode = 2;
            return StatusCode;
        }
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

