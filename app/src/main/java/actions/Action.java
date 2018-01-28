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
    public LocalBroadcastManager localmanager= null;
    public Intent intent = null;
    public String IntentAction= "";
    public Uri IntentURIprefix= Uri.EMPTY;
    public String UriQuery = "";
    public int Flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK;
    public String IntentMimeType= "";
    public Bundle Extras = new Bundle();
    public boolean RequiresVerification= false;
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
    public ArrayList<String> checkMissingAttributes(){
        ArrayList<String> Attributes = new ArrayList<>();
        if(RequiresUri && UriQuery.equals("")){
            Attributes.add("UriQuery");
        }
        Attributes.addAll(getEmptyExtras(Extras));
        return Attributes;
    }

    //Run the Intent
    public int runIntent(Context con,Boolean verified) {
        Intent curIntent = CreateIntent();
        if (RequiresVerification && !verified){
            StatusCode = 1;
            Log.d(TAG,"Activity Requires User Verification");
            return StatusCode;
        }
        else {
            con.startActivity(curIntent);
            StatusCode = 2;
            return StatusCode;
        }
    }

    //Helper Classes

    private Intent CreateIntent(){
        Intent runInt;
        if (RequiresUri) {
            runInt = new Intent(IntentAction, Uri.parse(IntentURIprefix + Uri.encode(UriQuery, "UTF-8")));
        } else{
            runInt = new Intent(IntentAction);
        }
        runInt.setFlags(Flags);
        return runInt;
    }

    private ArrayList<String> getEmptyExtras(Bundle extras){
        ArrayList<String> empties = new ArrayList<>();
        for (String key : extras.keySet()){
            Object value = extras.get(key);
            if (value == ""){
                empties.add(key);
            }
        }
        return empties;
    }

    public static final class ActionBuilder {
        //Config Variables
        public LocalBroadcastManager localmanager= null;
        public Intent intent = null;
        public String IntentAction= "";
        public Uri IntentURIprefix= Uri.EMPTY;
        public String UriQuery = "";
        public int Flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK;
        public String IntentMimeType= "";
        public Bundle Extras = new Bundle();
        public boolean RequiresVerification= false;
        public boolean MultiStageComm = false;
        public boolean RequiresUri = false;
        public int StatusCode = 0;
        //Messages
        public String VERIFY_MESSAGE = "";
        public ArrayList<String> DATA_REQUESTS = new ArrayList<>();
        public String ACTION_FAILED = "";
        public String NOT_FOUND = "";
        public String LAUNCHED = "";

        ActionBuilder() {
        }

        public static ActionBuilder anAction() {
            return new ActionBuilder();
        }

        public ActionBuilder setLocalmanager(LocalBroadcastManager localmanager) {
            this.localmanager = localmanager;
            return this;
        }

        public ActionBuilder setIntent(Intent intent) {
            this.intent = intent;
            return this;
        }

        public ActionBuilder setIntentAction(String IntentAction) {
            this.IntentAction = IntentAction;
            return this;
        }

        public ActionBuilder setURIprefix(String IntentURIprefix) {
            this.IntentURIprefix = Uri.parse(IntentURIprefix);
            return this;
        }

        public ActionBuilder setUriQuery(String UriQuery) {
            this.UriQuery = UriQuery;
            return this;
        }

        public ActionBuilder setFlags(int Flags) {
            this.Flags = Flags;
            return this;
        }

        public ActionBuilder setIntentMimeType(String IntentMimeType) {
            this.IntentMimeType = IntentMimeType;
            return this;
        }

        public ActionBuilder setExtra(String Key, String Value) {
            this.Extras.putString(Key,Value);
            return this;
        }

        public ActionBuilder setRequiresVerification(boolean RequiresVerification) {
            this.RequiresVerification = RequiresVerification;
            return this;
        }

        public ActionBuilder setMultiStageComm(boolean MultiStageComm) {
            this.MultiStageComm = MultiStageComm;
            return this;
        }

        public ActionBuilder setRequiresUri(boolean RequiresUri) {
            this.RequiresUri = RequiresUri;
            return this;
        }

        public ActionBuilder setStatusCode(int StatusCode) {
            this.StatusCode = StatusCode;
            return this;
        }

        public ActionBuilder setVERIFY_MESSAGE(String VERIFY_MESSAGE) {
            this.VERIFY_MESSAGE = VERIFY_MESSAGE;
            return this;
        }

        public ActionBuilder addDATA_REQUESTS(String Request) {
            this.DATA_REQUESTS.add(Request);
            return this;
        }

        public ActionBuilder setACTION_FAILED(String ACTION_FAILED) {
            this.ACTION_FAILED = ACTION_FAILED;
            return this;
        }

        public ActionBuilder setNOT_FOUND(String NOT_FOUND) {
            this.NOT_FOUND = NOT_FOUND;
            return this;
        }

        public ActionBuilder setLAUNCHED(String LAUNCHED) {
            this.LAUNCHED = LAUNCHED;
            return this;
        }

        public Action build() {
            Action action = new Action();
            action.IntentAction = this.IntentAction;
            action.UriQuery = this.UriQuery;
            action.MultiStageComm = this.MultiStageComm;
            action.RequiresUri = this.RequiresUri;
            action.Extras = this.Extras;
            action.Flags = this.Flags;
            action.RequiresVerification = this.RequiresVerification;
            action.intent = this.intent;
            action.StatusCode = this.StatusCode;
            action.NOT_FOUND = this.NOT_FOUND;
            action.DATA_REQUESTS = this.DATA_REQUESTS;
            action.IntentMimeType = this.IntentMimeType;
            action.localmanager = this.localmanager;
            action.LAUNCHED = this.LAUNCHED;
            action.VERIFY_MESSAGE = this.VERIFY_MESSAGE;
            action.ACTION_FAILED = this.ACTION_FAILED;
            action.IntentURIprefix = this.IntentURIprefix;
            return action;
        }
    }
}
