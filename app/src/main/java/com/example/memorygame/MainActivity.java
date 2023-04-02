package com.example.memorygame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    TextView NEW;
    TextView SCORE;
    TextView HIGHSCORE;
    Button START;

    int score = 0;
    int position  = 0;
    int highscore = 0;

    int compArray[] = new int[100];

//    0- Red
//    1- Yellow
//    2- Blue
//    3- Green


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage("This is Memory Builder Game.\n" +
                                      "At each level a color would be added to the existing sequence.\n" +
                                      "You have to memorize the sequence and press the colors " +
                                      "in that order to clear a current level.");

        alertDialogBuilder.setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        NEW = findViewById(R.id.Color);
        SCORE = findViewById(R.id.Score);
        START = findViewById(R.id.Start);
        HIGHSCORE = findViewById(R.id.HighScore);

        START.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

        highscore = SavedText();


        Button red = findViewById(R.id.Red);
        Button yellow = findViewById(R.id.Yellow);
        Button blue = findViewById(R.id.Blue);
        Button green = findViewById(R.id.Green);


        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200); //You can manage the blinking time with this parameter
        anim.setStartOffset(0);


        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound(R.id.Red);
                red.startAnimation(anim);
                Clicked(0);

            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound(R.id.Yellow);
                yellow.startAnimation(anim);
                Clicked(1);
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound(R.id.Blue);
                blue.startAnimation(anim);
                Clicked(2);
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound(R.id.Green);
                green.startAnimation(anim);
                Clicked(3);
            }
        });

    }

    public void Clicked(int color){

        if (position < score){

            check(color);
        }

        if (position == score){
            add();
            position = 0;

        }
    }

    // Start or Restart the Game
    public void start(){
        position = 0;
        score = 0;
        for (int i: compArray) {
            i = -1;
        }
        START.setVisibility(View.GONE);
        NEW.setText("Start the Game");
        SCORE.setText("0");
        add();
    }

    // Check the current Sequence
    public void check(int currentColor){

        if (currentColor == compArray[position]){
            position++;
        }
        else{
            MediaPlayer p = MediaPlayer.create(this, R.raw.sad);
            p.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            p.start();
            start();
            START.setVisibility(View.VISIBLE);
            NEW.setText("Start the Game");
            SCORE.setText("0");
        }
        
    }

    // Add to the Sequence
    public void add(){

        Random random = new Random();
        int newColor = random.nextInt(4);


        Toast.makeText(this, "New Level", Toast.LENGTH_SHORT).show();
        compArray[score] = newColor;
        score++;

        switch (newColor){
            case 0:
                NEW.setText("Red");
                SCORE.setText("Level:"+score);
                break;
            case 1:
                NEW.setText("Yellow");
                SCORE.setText("Level:"+score);
                break;
            case 2:
                NEW.setText("Blue");
                SCORE.setText("Level:"+score);
                break;
            case 3:
                NEW.setText("Green");
                SCORE.setText("Level:"+score);
                break;
            default:
                break;

        }

        int temp = SavedText();

        if (score > temp ){
            highscore = score;
            SaveText(""+highscore);
        }

    }

    // Play Sound
    private void playSound(int id) {
        //function that play sound according to sound ID
        int audioRes = 0;
        switch (id) {
            case R.id.Red:
                audioRes = R.raw.doo;
                break;
            case R.id.Yellow:
                audioRes = R.raw.re;
                break;
            case R.id.Blue:
                audioRes = R.raw.mi;
                break;
            case R.id.Green:
                audioRes = R.raw.fa;
                break;
        }
        MediaPlayer p = MediaPlayer.create(this, audioRes);
        p.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        p.start();
    }

    public int SavedText(){

        SharedPreferences mySharedPreference = getSharedPreferences("MySharedData", MODE_PRIVATE);
        String text = mySharedPreference.getString("Text","0");
        HIGHSCORE.setText(text);
        int temp = Integer.parseInt(text);
        return temp;
    }

    public void SaveText(String text){

        HIGHSCORE.setText(text);

        SharedPreferences mySharedPreference = getSharedPreferences("MySharedData", MODE_PRIVATE);

        SharedPreferences.Editor editor = mySharedPreference.edit();
        editor.putString("Text",text);
        editor.commit();

    }
    
}