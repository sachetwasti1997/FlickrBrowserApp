package com.sachet.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlickrJsonData";
    private List<Photo> mPhotoList = null;
    private String mBaseURL;
    private String mLanguage;
    private boolean mMatchAll;

    private final OnDataAvialable mCallBack;
    private boolean runningOnsameThread = false;
    interface OnDataAvialable {
        void onDataAvialable(List<Photo> data, DownloadStatus status);
    }

    public GetFlickrJsonData(String baseURL, String language, boolean matchAll, OnDataAvialable callBack) {
        mBaseURL = baseURL;
        mLanguage = language;
        mMatchAll = matchAll;
        mCallBack = callBack;
    }
//    void executeOnSameThread(String searchCriteria){
//        String destinationURI = createURI(searchCriteria,mLanguage,mMatchAll);
//        runningOnsameThread = true;
//        GetRawData getRawData = new GetRawData(this);
//        getRawData.execute(destinationURI);
//    }

    @Override
    protected void onPostExecute(List<Photo> list) {
        Log.d(TAG, "onPostExecute starts");

        if(mCallBack!=null){
            mCallBack.onDataAvialable(mPhotoList,DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute ends");
    }

    @Override
    protected List<Photo> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: Started..............");
        String downloadUri = createURI(strings[0],mLanguage,mMatchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runIntSameThread(downloadUri);
        return null;
    }

    private String createURI(String searchCriteria, String lang, boolean matchAll){
        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("tags",searchCriteria)
                .appendQueryParameter("tagmode",matchAll?"ALL":"ANY")
                .appendQueryParameter("lang",lang)
                .appendQueryParameter("format","json")
                .appendQueryParameter("nojsoncallback","1")
                .build().toString();
    }
    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: started");
        if(status == DownloadStatus.OK){
            mPhotoList = new ArrayList<>();
            try{
                JSONObject jsonObject = new JSONObject(data);//this jsonobject is used to get the json array of items that we want to process
                JSONArray itemsArray = jsonObject.getJSONArray("items");//this is the same array for which the above object is used
                for(int i=0; i<itemsArray.length();i++){
                    JSONObject jsonObject1 = itemsArray.getJSONObject(i);
                    String title = jsonObject1.getString("title");
                    String author = jsonObject1.getString("author");
                    String authorId = jsonObject1.getString("author_id");
                    String tags = jsonObject1.getString("tags");
                    //the link for this particular object is inside media tag
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("media");
                    String photoUrl = jsonObject2.getString("m");
                    String link = photoUrl.replaceFirst("_m.","_b.");
                    Photo photo = new Photo(title,author,authorId,link,tags,photoUrl);
                    mPhotoList.add(photo);
                }
            }catch(JSONException e){
                Log.e(TAG, "onDownloadComplete: error while reading the data******"+ e.getMessage() );
                e.printStackTrace();
                status=DownloadStatus.FAILED_OR_EMPTY;
            }

//            if(runningOnsameThread && mCallBack!=null){
//                //now informing the caller that the data was processed and returning null if there was any error
//                mCallBack.onDataAvialable(mPhotoList,status);
//            }
        }
        Log.d(TAG, "onDownloadComplete: ended");
    }
}
