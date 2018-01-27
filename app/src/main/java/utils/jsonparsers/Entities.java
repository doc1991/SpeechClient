
package utils.jsonparsers;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entities {

    @SerializedName("datetime")
    @Expose
    private List<Datetime> datetime = null;
    @SerializedName("app_data")
    @Expose
    private List<AppDatum> appData = null;
    @SerializedName("intent")
    @Expose
    private List<Intent> intent = null;

    public List<Datetime> getDatetime() {
        return datetime;
    }

    public void setDatetime(List<Datetime> datetime) {
        this.datetime = datetime;
    }

    public List<AppDatum> getAppData() {
        return appData;
    }

    public void setAppData(List<AppDatum> appData) {
        this.appData = appData;
    }

    public List<Intent> getIntent() {
        return intent;
    }

    public void setIntent(List<Intent> intent) {
        this.intent = intent;
    }

}
