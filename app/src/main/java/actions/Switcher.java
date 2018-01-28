package actions;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by gvol on 28/1/2018.
 */

public class Switcher {

    public static Action selectActionbyType(Action app, String type) {
        if (type.equals("make_call")) {
            app.IntentAction = Intent.ACTION_CALL;
            app.RequiresUri = true;
            app.data.put("contact", null);
            app.data_requests.put("contact_request", "Πείτε μου ποιόν θέλετε να καλέσω");
            app.IntentURIprefix = Uri.parse("tel:");
            app.Stage = "CH";
        }
        return app;
    }
}
