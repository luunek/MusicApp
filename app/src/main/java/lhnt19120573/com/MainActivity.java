package lhnt19120573.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
//
    TextView textViewSongName, textViewBegin, textViewEnd;
    ImageButton imageButtonPlay, imageButtonStop, imageButtonBack, imageButtonNext;
    SeekBar seekBarDisplay;
    ImageView imageViewMp3;
//
    ArrayList<Song> songArrayList;
    int index = 0;
    MediaPlayer mediaPlayer;
    Animation animationMp3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        getIdXml();
        addSong();
        Collections.shuffle(songArrayList);
        createMediaPlayer();
        animationMp3 = AnimationUtils.loadAnimation(this, R.anim.anim_mp3);
        imageViewMp3.startAnimation(animationMp3);

        //
        seekBarDisplay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                updateTimeBegin();
            }
        });

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index--;
                if(index < 0)
                {
                    index = songArrayList.size() - 1;
                }
                //x??a b??? nh??? c?? + c???p ph??t l???i m???i
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

                createMediaPlayer();
                //start b??i ti???p theo
                mediaPlayer.start();
                updateTimeBegin();
                imageButtonPlay.setImageResource(R.drawable.pause);
            }
        });

        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                if(index == songArrayList.size())
                {
                    index = 0;
                }
                //x??a b??? nh??? c?? + c???p ph??t l???i m???i
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

                createMediaPlayer();
                //start b??i ti???p theo
                mediaPlayer.start();
                updateTimeBegin();
                imageButtonPlay.setImageResource(R.drawable.pause);
            }
        });

        imageButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                //th??m release m?? hong th??m c??ng ???????c
                mediaPlayer.release();
                imageButtonPlay.setImageResource(R.drawable.play);
                createMediaPlayer();
            }
        });

        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    imageButtonPlay.setImageResource(R.drawable.play);
                }
                else
                {
                    mediaPlayer.start();
                    imageButtonPlay.setImageResource(R.drawable.pause);
                }
                updateTimeBegin();
            }
        });
    }

    private void updateTimeBegin(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                textViewBegin.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                seekBarDisplay.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
                //ki???m tra n???u k???t th??c b??i h??t th?? chuy???n qua b??i ti???p theo, copy ph???n button NEXT
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        index++;
                        if(index == songArrayList.size())
                        {
                            index = 0;
                        }
                        //x??a b??? nh??? c?? + c???p ph??t l???i m???i
                        if(mediaPlayer.isPlaying())
                        {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }

                        createMediaPlayer();
                        //start b??i ti???p theo
                        mediaPlayer.start();
                        updateTimeBegin();
                        imageButtonPlay.setImageResource(R.drawable.pause);
                    }
                });
            }
        }, 100);
    }
    private void setTimeEnd(){
        SimpleDateFormat dinhDang = new SimpleDateFormat("mm:ss");
        textViewEnd.setText(dinhDang.format(mediaPlayer.getDuration()));
        seekBarDisplay.setMax(mediaPlayer.getDuration());
    }
    private void createMediaPlayer() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, songArrayList.get(index).getResource());
        textViewSongName.setText(songArrayList.get(index).getTitle());
        setTimeEnd();
    }

    private void addSong() {
        songArrayList = new ArrayList<>();
        songArrayList.add(new Song("??nh n???ng c???a anh", R.raw.anh_nang_cua_anh));
        songArrayList.add(new Song("H???n m???t mai", R.raw.hen_mot_mai));
        songArrayList.add(new Song("L???ng y??n", R.raw.lang_yen));
        songArrayList.add(new Song("N???c thang l??n thi??n ???????ng", R.raw.nac_thang_len_thien_duong));
        songArrayList.add(new Song("Ng?????i t???ng y??u anh r???t s??u n???ng", R.raw.nguoi_tung_yeu_anh_rat_sau_nang));
        songArrayList.add(new Song("Nh???ng k??? m???t m??", R.raw.nhung_ke_mong_mo));
        songArrayList.add(new Song("Sau t???t c???", R.raw.sau_tat_ca));
    }

    private void getIdXml() {
        textViewBegin = findViewById(R.id.textViewBegin);
        textViewEnd = findViewById(R.id.textViewEnd);
        textViewSongName = findViewById(R.id.textViewSongName);
        seekBarDisplay = findViewById(R.id.seekBarDisplay);
        imageButtonBack = findViewById(R.id.imgButtonBack);
        imageButtonNext = findViewById(R.id.imgButtonNext);
        imageButtonStop = findViewById(R.id.imgButtonStop);
        imageButtonPlay = findViewById(R.id.imgButtonPlay);
        imageViewMp3 = findViewById(R.id.imageViewMp3);
    }
}