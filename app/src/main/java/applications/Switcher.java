package applications;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import utils.ContactUtils;

/**
 * Created by gvol on 28/1/2018.
 */

public class Switcher {

    public static Action selectActionbyType(Action app, String type) {
        if (type.equals(Constatns.CALL_APP)) {
            app = InitActionObj(
                    app,type,Constatns.ACTION_CALL,true,"contact",
                    "Πείτε μου το όνομα της επαφής","tel:",
                    false,
                    "Η επαφή δεν βρέθηκε.",Constatns.CH_STAGE,"Επιθυμείτε να πραγματοποιηθεί το τηλεφώνημα"
            );
        }
        return app;
    }

    public static Action transforminfo (Action app, Context con){
        if (app.type.equals("make_call")){

            String contact = app.data.get("contact");
            Log.i("Switcher","the user told : "+contact);
            //Optimizing DB Calls
            ArrayList<String> TelsfromDB = ContactUtils.ContactNumber(contact, con);
            if (TelsfromDB.size() > 0 ) {

                if(!TelsfromDB.get(0).equals("0") || !TelsfromDB.get(0).equals("1")){
                    Log.i("switcher","data contact name = "+ TelsfromDB.get(0));

                    app.UriQuery = TelsfromDB.get(0);
                    app.Stage = Constatns.VR_STAGE;
                }else if(TelsfromDB.get(0).equals("0")) {
                    Log.i("switcher","data number = "+ TelsfromDB.get(0));
                    //Strip Whitespace on raw number
                    app.UriQuery = app.data.get("contact").replace(" ","");
                    app.Stage = Constatns.VR_STAGE;
                }
                else if(TelsfromDB.get(0).equals("1")) {
                    Log.i("switcher","data wrong number = "+ TelsfromDB.get(0));
                    app.NOT_FOUND = "Μη έγκυρος αριθμός";
                    app.Stage = Constatns.NF_STAGE;
                }

            }
            else {

                app.Stage = Constatns.NF_STAGE;
            }
        }

        return app;
    }


    private static Action InitActionObj(Action app,String type,String IntentAction,boolean requiresUri,
                                        String AppName,String SoundMessage,String uri,
                                        boolean MultiStageCommFromStart,String NOT_FOUND,String Stage,String VerifyMessage
                                        ){

        app.type = type;
        app.IntentAction = IntentAction;
        app.RequiresUri = requiresUri;
        app.data.put(AppName, null);
        app.data_requests.put(AppName, SoundMessage);
        app.Current_Key = AppName;
        app.IntentURIprefix = Uri.parse(uri);
        app.MultiStageCommFromStart = MultiStageCommFromStart;
        app.NOT_FOUND = NOT_FOUND;
        app.Stage = Stage;
        app.VERIFY_MESSAGE =VerifyMessage;

        return app;

    }


}
