package com.tomiyama.noir.photolayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_PICK_IMAGEFILE = 1001;

    private SquareImageView oneImageView;
    private SquareImageView twoImageView;
    private SquareImageView threeImageView;
    private SquareImageView fourImageView;

    private SquareImageView[] imageViews;

    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oneImageView = findViewById(R.id.image_view);
        twoImageView = findViewById(R.id.image_view_2);
        threeImageView = findViewById(R.id.image_view_3);
        fourImageView = findViewById(R.id.image_view_4);

        imageViews = new SquareImageView[]{
                            oneImageView,
                            twoImageView,
                            threeImageView,
                            fourImageView
                    };

        position = 0; //初期化

        setListener();

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.image_view:
                    position = 0;
                    setImagePicture();
                    Log.d("imageView","１番左");
                    break;
                case R.id.image_view_2:
                    position = 1;
                    setImagePicture();
                    Log.d("imageView2","にばんめ〜");
                    break;
                case R.id.image_view_3:
                    position = 2;
                    setImagePicture();
                    Log.d("imageView3","さんばんめ〜");
                    break;
                case R.id.image_view_4:
                    position = 3;
                    setImagePicture();
                    Log.d("imageView4","１番右");
                    break;
            }

        }
    };

    private void setListener(){
        for(int i = 0;i<imageViews.length;i++){
            imageViews[i].setOnClickListener(clickListener);
        }
    }

    private void setImagePicture(){

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {

            if(intent.getData() != null){

                ParcelFileDescriptor pfDescriptor = null;

                try{
                    Uri uri = intent.getData();

                    Log.d("uri:",uri.toString());

                    pfDescriptor = getContentResolver().openFileDescriptor(uri, "r");

                    if(pfDescriptor != null){

                        FileDescriptor fileDescriptor = pfDescriptor.getFileDescriptor();
                        Bitmap bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        pfDescriptor.close();

                        // 押したpositionに画像を配置
                        imageViews[position].setImageBitmap(bmp);
                        imageViews[position].setBackgroundColor(Color.parseColor("#00000000"));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try{
                        if(pfDescriptor != null){
                            pfDescriptor.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
