package com.sachet.flickrbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetFlickrJsonData.OnDataAvialable
        , RecyclerItemClickListener.OnRecyclerClickListener {
    private static final String TAG = "MainActivity";
    private FlickrRecyclerViewAdapter mFlickrRecyclerViewAdapter;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolBar(true);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,recyclerView,this));

        mFlickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(new ArrayList<Photo>(),this);
        recyclerView.setAdapter(mFlickrRecyclerViewAdapter);

        //Here we can pass this both as a context and the listener as we have implemented the interface



        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
        /*onResume & onPause should deal with saving the present state of the app & restoring that state back again so that you
        don't loose interstitial data when being interrupted by other services/apps/user actions.*/
        GetFlickrJsonData getFlickrJsonData= new GetFlickrJsonData("https://api.flickr.com/services/feeds/photos_public.gne",
                "en-us",true,this);
//        getFlickrJsonData.executeOnSameThread("android,nougat");
        getFlickrJsonData.execute("android,pie");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        Log.d(TAG, "onOptionsItemSelected() returned: returned");
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDataAvialable(List<Photo> data, DownloadStatus status){
        Log.d(TAG, "onDataAvialable: started******************************");
        //this function acts as the call back function
        if(status == DownloadStatus.OK){
            mFlickrRecyclerViewAdapter.loadNewData(data);
        }else{
            Log.d(TAG, "OnDownloadAvailable: download failed "+status);
        }
        Log.d(TAG, "onDataAvialable: ended***********************************");
    }
    /*There are a number of ways to find if a item is clcked in the recycledview, one of them is by implementing the interface
     RecycleView.OnItemTouchListener in the class but we will extend the class RecycleView.SimpleOnItemTouchListener*/

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        Toast.makeText(MainActivity.this,"Normal Tap at "+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
        Intent intent = new Intent(this,PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER,mFlickrRecyclerViewAdapter.getPhoto(position));
        //for this to work the Photo object should be serialisable
        startActivity(intent);
        /*Using the putExtra method we are telling the details of the photo that we are trying to display*/
        /*Works similar to a bundle*/
    }
}
