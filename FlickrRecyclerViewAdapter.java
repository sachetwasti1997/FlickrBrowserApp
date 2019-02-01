package com.sachet.flickrbrowser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/*Images add much-needed context and visual flair to Android applications. Picasso allows for hassle-free image loading
in your application—often in one line of code!
Picasso download the image in another thread and it manages for you:
    ->the placeholder in the meantime the image is still downloading
    ->resizing
    ->cropping/centering/scaling
    ->caching ( you don't have to download the image every time)
    ->it even does "image fade in", which is popular/normal now
*/

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecyclerViewAdapt";
    private List<Photo> mPhotoList;
    private Context mContext;

    public FlickrRecyclerViewAdapter(List<Photo> photoList, Context context) {
        mPhotoList = photoList;
        mContext = context;
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "FlickrImageViewHolder";
        private TextView title;
        private ImageView mImageView;

        public FlickrImageViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            mImageView = itemView.findViewById(R.id.thumbnail);
        }
    }

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /*onCreateViewHolder only creates a new view holder when there are no existing view holders which the RecyclerView can reuse.*/
        Log.d(TAG, "onCreateViewHolder starts");
        /*When attachToRoot is false, the layout file from the first parameter is inflated and returned as a View.*/
        /* If attachToRoot is set to true, then the layout file specified in the first parameter is inflated and
         attached to the ViewGroup specified in the second parameter.*/
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.browser,viewGroup,false);
        /*Context as itself is something like environment your code is executed in. It has access to ui(if it is an activity), it can contain
         some global data(application context), and has access to the resources(all of the contexts). Also, context allows you to perform common
         android operations like broadcasting intents, start activities and services.
         View describes one element of your ui. It can have onClickListeners, properties and so on. But every view is created in some context,
         usually Activity's context.So, views should be passed when you want to do something with a particular view. Context is passed when
         you need access to resources, global data or ui context, or launch other android components.*/
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder flickrImageViewHolder, int i) {
        /*Instead of creating new view for each new row, an old view is recycled and reused by binding new data to it.This happens exactly in
         onBindViewHolder(). Initially you will get new unused view holders and you have to fill them with data you want to display. But as you
         scroll you'll start getting view holders that were used for rows that went off screen and you have to replace old data that they held
         with new data.*/
        //Called by the layout manager when it wants new data in the existing row
        Photo photo = mPhotoList.get(i);
        Log.d(TAG, "onBindViewHolder: "+photo.getTitle()+" -> "+i);
        Picasso.get().load(photo.getImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(flickrImageViewHolder.mImageView);
        flickrImageViewHolder.title.setText(photo.getTitle());
    }

    @Override
    public int getItemCount() {
        return ((mPhotoList!=null) && (mPhotoList.size()!=0)?mPhotoList.size():0);
    }
    /*When the query changes and the new data is loaded we need to provide the adapter with the new data and for that purpose we use loadnewdata()*/
    void loadNewData(List<Photo> newPhotolist){
        mPhotoList = newPhotolist;
        notifyDataSetChanged();//tells the recycler view that data set is changed so that it can go ahead and change the display
    }
    public Photo getPhoto(int position){
        return ((mPhotoList!=null) && (mPhotoList.size()!=0)?mPhotoList.get(position):null);
    }
}
