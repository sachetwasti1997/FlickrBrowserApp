package com.sachet.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus{ IDLE, PROCESSING, NOT_INITIALISED,FAILED_OR_EMPTY, OK}

class GetRawData extends AsyncTask<String,Void,String> {
    //doing all these changes we are now able to use the same GetRawData class whereever we want
    private static final String TAG = "GetRawData";
    DownloadStatus mDownloadStatus;
    private final OnDownloadComplete mCallBack;

    public GetRawData(OnDownloadComplete mainActivity){
        this.mCallBack = mainActivity;
        mDownloadStatus = DownloadStatus.IDLE;
    }

    interface OnDownloadComplete{
        void onDownloadComplete(String data, DownloadStatus status);
    }

//    @Override
//    protected void onPostExecute(String s) {
//        //if any method calls its super method that is nessecary than using it in this way is likely to cause errors
//        if(mCallBack!=null)mCallBack.onDownloadComplete(s, mDownloadStatus);
//        Log.d(TAG, "onPostExecute ends.......");
//    }

    void runIntSameThread(String s){
        Log.d(TAG, "runIntSameThread: started");

//        onPostExecute(doInBackground(s));
        //Thus we change the runIntSame thread as below to prevent it
        if(mCallBack !=null){
            mCallBack.onDownloadComplete(doInBackground(s),DownloadStatus.OK);
        }

        Log.d(TAG, "runIntSameThread: ended");
    }

    @Override
    protected String doInBackground(String... strings) {
        BufferedReader bufferedReader = null;
        HttpURLConnection httpURLConnection = null;

        if(strings == null){
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }
        
        try{
//            Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress("172.16.30.20",8080));
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            int responce = httpURLConnection.getResponseCode();
            Log.d(TAG, "doInBackground: The responce code is "+responce);

            StringBuilder result = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
//            String line;
//            while(null != (line=bufferedReader.readLine())){
            for(String line=bufferedReader.readLine(); line!=null; line = bufferedReader.readLine()){
                result.append(line).append("\n");
            }
            mDownloadStatus = DownloadStatus.OK;
            return result.toString();


        } catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: Invalid URL " +e.getMessage());
        }catch(IOException e){
            Log.e(TAG, "doInBackground: Error while reading the data from the net "+e.getMessage());
        }catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception! Needs Permission "+e.getMessage());
        }finally{
            /*In case of no errors this block gets executed before the return statement*/
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            if(bufferedReader!=null){
                try{
                    bufferedReader.close();
                }catch(IOException e){
                    Log.d(TAG, "doInBackground: error while downloading "+e.getMessage());
                }
            }
        }

        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}


















