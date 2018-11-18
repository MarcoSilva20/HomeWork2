package com.example.marco.hw2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongInfoFragment extends Fragment implements View.OnClickListener {


    public SongInfoFragment() {
        // Required empty public constructor
    }

    Context context;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private Songs mDisplaySong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_song_info, container, false);
        context = rootView.getContext();

        // Inflate the layout for this fragment
        return rootView;
    }

    public void displayTask(Songs task) {
        View displayTaskVieew = getActivity().findViewById(R.id.displayFragment);
        displayTaskVieew.setVisibility(View.VISIBLE);
        ((TextView) getActivity().findViewById(R.id.songTitle)).setText(task.title);
        ((TextView) getActivity().findViewById(R.id.artist)).setText(task.artist);
        ((TextView) getActivity().findViewById(R.id.release_date)).setText(task.release_data);
        ImageView taskImage = (ImageView) getActivity().findViewById(R.id.songPhoto);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((Button) getActivity().findViewById(R.id.takePhoto)).setOnClickListener(this);
        Intent intent = getActivity().getIntent();
        Songs receivedSong = intent.getParcelableExtra(MainActivity.songExtra);
        if (receivedSong != null) {
            displayTask(receivedSong);
        }

        FloatingActionButton addSong = (FloatingActionButton) getActivity().findViewById(R.id.addSong);
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_song = new Intent(context, SecondActivity.class);
                startActivityForResult(add_song, 5);
            }
        });
        final SongListFragment taskFr = (SongListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.taskFragment);
        FloatingActionButton delete = (FloatingActionButton) getActivity().findViewById(R.id.deleteSong);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show();
                DialogFragment newFragment = DeleteDialog.newInstance();
                newFragment.show(getActivity().getSupportFragmentManager(), "DeleteDialogTag");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            mDisplaySong.addPicPath(mCurrentPhotoPath);
            ImageView taskImage = (ImageView) getActivity().findViewById(R.id.songPhoto);
            Bitmap imageBitmao = PicUtils.decodePic(mDisplaySong.photo,taskImage.getWidth(),taskImage.getHeight());
            taskImage.setImageBitmap(imageBitmao);
        }
        else if (requestCode == 5 && resultCode == RESULT_OK) {
            String song_title = data.getStringExtra("song_title");
            if (requestCode == 5) {
                String artist = data.getStringExtra("artist");
                String release_date = data.getStringExtra("release_date");
                Songs add = new Songs(song_title, artist, release_date);
                MainActivity.myTasks.add(add);
                SongListFragment taskFr = (SongListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.taskFragment);
                ArrayAdapter<Songs> taskAdapter = (ArrayAdapter<Songs>) taskFr.getListAdapter();
                taskAdapter.notifyDataSetChanged();
            }
        }

    }



    @Override
    public void onClick(View v) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(getActivity(), "com.example.marco.hw2.fileprovider", photoFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpeg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;


    }

}
