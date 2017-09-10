package id.co.qodr.androiexpertasynctaskloader;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<WeatherItems>> {

    private ListView listView;
    private WeatherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);

        adapter = new WeatherAdapter(this);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<ArrayList<WeatherItems>> onCreateLoader(int id, Bundle args) {
        Log.d("Create Loader", "1 ");
        return new MyAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<WeatherItems>> loader, ArrayList<WeatherItems> data) {
        Log.d("Load Finish", "1 ");

        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<WeatherItems>> loader) {
        Log.d("Load Reset", "1 ");
        ArrayList<WeatherItems> data = new ArrayList<>();
        adapter.setData(data);
    }
}
