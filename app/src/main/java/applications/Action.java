package applications;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gvol on 17/12/2017.
 */

public class Action {
    //Config Variables
    private String TAG = "ActionClass";
    public String IntentAction = null;
    public String type = "";
    public Uri IntentURIprefix = Uri.EMPTY;
    public String UriQuery = "";
    public boolean RequiresVerification = false;
    public boolean MultiStageCommFromStart = false;
    public boolean RequiresUri = false;
    //Data for apps demanding content like sms
    public HashMap<String,String> data = new HashMap<>();
    //Request data from user with message
    public HashMap<String,String> data_requests = new HashMap<>();
    public boolean waiting_data = false;
    public String Current_Key = "";
    public String Stage = Constatns.IN_STAGE;
    public boolean IsTelNumber=false;

    //Messages
    public String VERIFY_MESSAGE = "";
    public String ACTION_FAILED = "";
    public String NOT_FOUND = "";
    public String LAUNCHED = "";

    //Constructor
    public Action() {
    }


    public void Init(){
        IntentAction = null;
        type = "";
       IntentURIprefix = Uri.EMPTY;
        UriQuery = "";
        RequiresVerification = false;
        MultiStageCommFromStart = false;
       RequiresUri = false;
        //Data for apps demanding content like sms
        data = new HashMap<>();
        //Request data from user with message
       data_requests = new HashMap<>();
         waiting_data = false;
        Current_Key = "";
         Stage = Constatns.IN_STAGE;
        IsTelNumber=false;
        //Messages
       VERIFY_MESSAGE = "";
       ACTION_FAILED = "";
        NOT_FOUND = "";
        LAUNCHED = "";

    }
    //Run the Intent
    public void runIntent(Context con) {
        Stage = Constatns.CP_STAGE;
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
        runInt.setFlags(Constatns.FLAGS);
        return runInt;
    }




}

