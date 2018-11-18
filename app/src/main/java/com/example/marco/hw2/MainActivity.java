package com.example.marco.hw2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements  DeleteDialog.NoticeDialogListener {

    static public ArrayList<Songs> myTasks;
    public static final int ADD_SONG = 1;
    public static final int DELETE_SONG = 2;
    public static final String songExtra = "Song";

    public static String song_title = "song_title";
    public static String artist = "artist";
    public static String release_date = "release_date";

    private int listitemposition = -1;

    public static final String TASK_FILE = "com.example.marco.hw2.TaskFile";
    public static final String NUM_TASKS = "NumofTasks";
    public static final String TASK = "task_";
    public static final String ARTIST = "artist_";
    public static final String RELEASE_DATA = "release_data_";
    public static final String PHOTO = "PHOTO_";




    static{
        myTasks = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restoreTasks();

        final SongListFragment taskFr = (SongListFragment) getSupportFragmentManager().findFragmentById(R.id.taskFragment);
        final ArrayAdapter<Songs> taskAdapter = (ArrayAdapter<Songs>) taskFr.getListAdapter();
        taskFr.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Item SELECTED", Toast.LENGTH_LONG).show();
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    SongInfoFragment frag = (SongInfoFragment) getSupportFragmentManager().findFragmentById(R.id.displayFragment);
                    frag.displayTask((Songs) parent.getItemAtPosition(position));
                }
                else{
                    startSecondActivity(parent, position);
                }
            }
        });

    }

    private void startSecondActivity(AdapterView<?> parent, int position){
        Intent intent = new Intent(this,InformationSong.class);
        Songs tmp = (Songs) parent.getItemAtPosition(position);
        intent.putExtra(songExtra,tmp);
        startActivityForResult(intent,DELETE_SONG);

    }

    public void addSong(View view) {
        Intent add_song = new Intent(getApplicationContext(),SecondActivity.class);
        startActivityForResult(add_song, ADD_SONG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == ADD_SONG) {
                song_title = data.getStringExtra(song_title);
                artist = data.getStringExtra(artist);
                release_date = data.getStringExtra(release_date);
                myTasks.add(new Songs(song_title,artist,release_date));
            }

            if(requestCode == DELETE_SONG){
                song_title = data.getStringExtra(song_title);
                Songs remove = null;
                for (Songs i: myTasks) {
                    if(i.title.equals(song_title)) remove = i;
                }
                myTasks.remove(remove);
            }

            SongListFragment taskFr = (SongListFragment) getSupportFragmentManager().findFragmentById(R.id.taskFragment);
            ArrayAdapter<Songs> taskAdapter = (ArrayAdapter<Songs>) taskFr.getListAdapter();
            taskAdapter.notifyDataSetChanged();
        }
    }

   @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if(listitemposition==-1){

            TextView song_title = (TextView) findViewById(R.id.songTitle);
            String st = song_title.getText().toString();
            Songs remove = null;
            for (Songs i: myTasks) {
                if(i.title.equals(st)) remove = i;
            }
            myTasks.remove(remove);
            SongListFragment taskFr = (SongListFragment) getSupportFragmentManager().findFragmentById(R.id.taskFragment);
            ArrayAdapter<Songs> taskAdapter = (ArrayAdapter<Songs>) taskFr.getListAdapter();
            taskAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDialogNegativelick(DialogFragment dialog) {
        View v = findViewById(R.id.addSong);
        Snackbar.make(v,"Delete canceled!", Snackbar.LENGTH_LONG).setAction("Retry?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newF = DeleteDialog.newInstance();
                newF.show(getSupportFragmentManager(),"DeleteDIalogTag");
            }
        }).show();
    }

    private boolean saveTasks(){
        SharedPreferences tasks = getSharedPreferences(TASK_FILE,MODE_PRIVATE);
        SharedPreferences.Editor editor = tasks.edit();
        editor.clear();
        editor.putInt(NUM_TASKS,myTasks.size());

        for (Integer i=0;i<myTasks.size();++i){
            editor.putString(TASK+i.toString(),myTasks.get(i).title);
            editor.putString(ARTIST+i.toString(),myTasks.get(i).artist);
            editor.putString(RELEASE_DATA+i.toString(),myTasks.get(i).release_data);
            editor.putString(PHOTO+i.toString(),myTasks.get(i).photo);
        }

        return editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!saveTasks()){
            Toast.makeText(getApplicationContext(),"Save Failed!",Toast.LENGTH_LONG).show();
        }

        saveTasktoFile();
    }

    private void restoreTasks(){
        SharedPreferences tasks = getSharedPreferences(TASK_FILE,MODE_PRIVATE);
        int numofTasks = tasks.getInt("NumofTasks",0);
        if(numofTasks!=0){
            myTasks.clear();

            for (Integer i=0;i<numofTasks;++i){
                String title = tasks.getString(TASK+i.toString(),"0");
                String artist = tasks.getString(ARTIST+i.toString(),"0");
                String release_date = tasks.getString(RELEASE_DATA+i.toString(),"0");
                String photo = tasks.getString(PHOTO+i.toString(),"");
                Songs tmp = new Songs(title,artist,release_date,photo);

                myTasks.add(tmp);
            }
        }
        //restoreTasksFromFile();
    }

    private void saveTasktoFile(){
        String filename = "TaskFile.txt";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputStream.getFD()));
            String delim = ";";

            for (Integer i = 0; i < myTasks.size(); ++i) {
                Songs tmp = myTasks.get(i);
                String line = tmp.title + delim + tmp.artist + delim + tmp.release_data+delim+tmp.photo;
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
