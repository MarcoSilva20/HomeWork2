package com.example.marco.hw2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InformationSong extends AppCompatActivity implements  DeleteDialog.NoticeDialogListener, View.OnClickListener {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private Songs mDisplaySong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_song);
        ((Button) findViewById(R.id.takePhoto)).setOnClickListener(this);

        Songs song = getIntent().getParcelableExtra(MainActivity.songExtra);
        if(song!=null){
            displayTask(song);
        }

        FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.deleteSong);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_LONG).show();
                DialogFragment newFragment = DeleteDialog.newInstance();
                newFragment.show(getSupportFragmentManager(),"DeleteDialogTag");
            }
        });

    }

    public void displayTask(Songs task){
        ((TextView) findViewById(R.id.songTitle)).setText(task.title);
        ((TextView) findViewById(R.id.artist)).setText(task.artist);
        ((TextView) findViewById(R.id.release_date)).setText(task.release_data);
        ImageView taskImage = (ImageView) findViewById(R.id.songPhoto);
        if(task.photo!=null && !task.photo.isEmpty()){
            Bitmap imagebimap = PicUtils.decodePic(task.photo,10);
            taskImage.setImageBitmap(imagebimap);

        }
        else{
            taskImage.setImageBitmap(null);
        }

        mDisplaySong = task;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        TextView txt = (TextView) findViewById(R.id.songTitle);
        TextView txt1 = (TextView) findViewById(R.id.artist);
        TextView txt2 = (TextView) findViewById(R.id.release_date);
        Intent data = new Intent();
        data.putExtra(MainActivity.song_title,txt.getText().toString());
        data.putExtra(MainActivity.artist,txt1.getText().toString());
        data.putExtra(MainActivity.release_date,txt2.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onDialogNegativelick(DialogFragment dialog) {
        setResult(0, null);
        finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            mDisplaySong.addPicPath(mCurrentPhotoPath);
            ImageView taskImage = (ImageView) findViewById(R.id.songPhoto);
            Bitmap imageBitmao = PicUtils.decodePic(mDisplaySong.photo,taskImage.getWidth(),taskImage.getHeight());
            taskImage.setImageBitmap(imageBitmao);
        }

    }



    @Override
    public void onClick(View v) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.marco.hw2.fileprovider", photoFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpeg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;


    }

}
