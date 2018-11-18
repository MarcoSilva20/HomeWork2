package com.example.marco.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

    }

    public void add(View view) {

        String song_title = ((EditText) findViewById(R.id.songTitle)).getText().toString();
        String artist = ((EditText) findViewById(R.id.artist)).getText().toString();
        String release_date = ((EditText) findViewById(R.id.release_date)).getText().toString();

        Intent data = new Intent();
        data.putExtra(MainActivity.song_title,song_title);
        data.putExtra(MainActivity.artist,artist);
        data.putExtra(MainActivity.release_date,release_date);
        setResult(RESULT_OK, data);
        finish();
    }

}
