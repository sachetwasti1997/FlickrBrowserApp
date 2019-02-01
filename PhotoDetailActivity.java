package com.sachet.flickrbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolBar(true);

        Intent intent = getIntent();
        /*We used the getIntent() method to retrive the intent that started this activity*/
        Photo photo = (Photo)intent.getSerializableExtra(PHOTO_TRANSFER);
        /*And to retrive the photo object we used getSerialisableExtra() and then we cast that into photo to make sure that we get the right type back*/
        /*showing the picture in the imageView is straightforward, same as the onBindView method of the Adapter*/
        if(photo!=null){

            TextView photoTitle = findViewById(R.id.photo_title);
            photoTitle.setText("Title: "+photo.getTitle());

            TextView photoAuthor = findViewById(R.id.photo_author);
            photoAuthor.setText("Author: "+photo.getAuthor());

            TextView photoTags = findViewById(R.id.photo_tags);
            photoTags.setText("Tags: "+photo.getTags());

            ImageView imageView = findViewById(R.id.photo_image);
            Picasso.get().load(photo.getLink())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }
    }

}
