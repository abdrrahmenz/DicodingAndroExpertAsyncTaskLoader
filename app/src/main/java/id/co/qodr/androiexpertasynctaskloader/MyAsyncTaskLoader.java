package id.co.qodr.androiexpertasynctaskloader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by adul on 08/09/17.
 */

public class MyAsyncTaskLoader extends AsyncTaskLoader<ArrayList<WeatherItems>> {

    private static String ID_JAKARTA = "1642911";
    private static String ID_PEKALONGAN = "1631766";
    private static String ID_SEMARANG = "1627896";
    private static String API_KEY = "33f97808416c897fafc3e104ed2862a0";

    private ArrayList<WeatherItems> mData;
    public boolean hasResult = false;

    public MyAsyncTaskLoader(Context context) {
        super(context);
        onContentChanged();
        Log.d("INIT AsyncTaskLoader", " : 1");
    }

    @Override
    protected void onStartLoading() {
        Log.d("Content Changed", " onStartLoading: 1");
        if (takeContentChanged())
            forceLoad();
        else if (hasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(ArrayList<WeatherItems> data) {
        mData = data;
        hasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (hasResult){
            onReleaseResources(mData);
            mData = null;
            hasResult = false;
        }
    }

    @Override
    public ArrayList<WeatherItems> loadInBackground() {
        Log.d("LOAD BACKGROUND", "loadInBackground: 1");
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<WeatherItems> weatherItemses = new ArrayList<>();
        String url = "http://api.openweathermap.org/data/2.5/group?id="+ID_PEKALONGAN+","+ID_JAKARTA+
                ","+ID_SEMARANG+"&units=metric&appid="+API_KEY;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("list");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject weather = list.getJSONObject(i);
                        WeatherItems weatherItems = new WeatherItems(weather);
                        weatherItemses.add(weatherItems);
                    }
                    Log.d("REQUEST SUCCESS", " 1 ");
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("REQUEST FAIL", " 1 ");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        for (int i = 0; i < weatherItemses.size(); i++) {
            Log.d("KOTA", weatherItemses.get(i).getNama());
        }
        Log.d("BEFORE RETURN", "1 ");
        return weatherItemses;
    }

    private void onReleaseResources(ArrayList<WeatherItems> mData) {
        //nothing to do.
    }

    public ArrayList<WeatherItems> getResult(){
        return mData;
    }
}
