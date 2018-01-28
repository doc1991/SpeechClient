package actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import utils.ContactUtils;

/**
 * Created by gvol on 28/1/2018.
 */

public class Switcher {

    public static Action selectActionbyType(Action app, String type) {
        if (type.equals("make_call")) {
            app.type = "make_call";
            app.IntentAction = Intent.ACTION_CALL;
            app.RequiresUri = true;
            app.data.put("contact", null);
            app.data_requests.put("contact", "Πείτε μου το όνομα της επαφής");
            app.Current_Key = "contact";
            app.IntentURIprefix = Uri.parse("tel:");
            app.MultiStageCommFromStart = false;
            app.NOT_FOUND = "Η επαφή δεν βρέθηκε.";
            app.Stage = "CH";
        }
        return app;
    }

    public static Action transforminfo (Action app, Context con){
        if (app.type.equals("make_call")){
            if (ContactUtils.ContactNumber(app.data.get("contact"),con).size() != 0) {
                app.data.put("contact", ContactUtils.ContactNumber(app.data.get("contact"), con).get(0));
                app.UriQuery = app.data.get("contact");
                app.Stage = "CP";
            }
            else {
                app.Stage = "NF";
            }
        }

        return app;
    }

}
