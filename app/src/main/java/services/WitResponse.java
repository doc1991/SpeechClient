package services;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import ai.wit.sdk.Wit;
import utils.SearchStringHelper;
import utils.jsonparsers.Witobj;
import utils.Constants;

import static utils.Constants.MaestroComm;

/**
 * Created by bill on 11/4/17.
 */

public class WitResponse extends AsyncTask<String, Void, Witobj> {

    public static final int STATUS_ERROR_COMMAND = 0;
    private static final String TAG = WitResponse.class.getSimpleName();
    private static final String witurl = "https://api.wit.ai/message?v=20171106&q=";
    private static final String accessToken = "Bearer CKZRXPXVE2D2XYFU34PYPQS6PLAFRR5R";
    private static final String header = "Authorization";
    private WeakReference<Context> contextWeakReference;


    public WitResponse(Context context) {
        contextWeakReference = new WeakReference<Context>(context);
    }

    @Override
    protected void onPostExecute(Witobj response) {
        Intent intent = new Intent(MaestroComm);
        intent.putExtra("Sender","WIT");
        intent.putExtra("WitOBJ", response);
        LocalBroadcastManager.getInstance(contextWeakReference.get()).sendBroadcast(intent);

    }

    @Override
    protected Witobj doInBackground(String... strings) {
        String query = strings[0];
        String queryencoded = null;
        Witobj resp2 = null;
        Log.i(TAG, "query is " + query);

        try {
            //encode query for putting it to url
            queryencoded = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.i(TAG, "UnsupportedEncodingException: " + e.getMessage());
        }
        String witrequesturl = witurl + queryencoded;
        Log.i(TAG, "query url is " + witrequesturl);

        try {
            //put in hash map enities of json
            resp2 = GetResults(witrequesturl);


        } catch (IOException e) {
            Log.i(TAG, "IOException: " + e.getMessage());
        }

        return resp2;
    }

    //get WitObj response from wit.ai
    private Witobj GetResults(String url) throws IOException {

        HttpURLConnection connection = null;
        String result = null;
        InputStream stream = null;

        try {
            //create connection with wit
            URL urlWit = new URL(url);
            connection = (HttpURLConnection) urlWit.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty(header, accessToken);

            if (connection.getResponseCode() != 200) {
                Log.i(TAG, "HTTP error: " + connection.getResponseCode());
                throw new RuntimeException("failed: HTTP error code: " + connection.getResponseCode());
            }
            //put response in inputstream
            stream = connection.getInputStream();
            if (stream != null) {
                //make stram strin with function StreamToString
                result = SearchStringHelper.StreamToString(stream);
            }

        } finally {
            //free resources
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        Witobj resp = null;
        try{
            resp = new Gson().fromJson(result, Witobj.class);

        }
        finally {

        }

        return resp;

    }



}