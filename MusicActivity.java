package com.example.weizhentao;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.weizhentao.entity.Music;
import com.example.weizhentao.utils.Common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {
    //定义activity_music.xml的控件对象
    //设置音乐播放模式
    private int i = 0;
    private int playMode = 0;
    private int buttonWitch = 0;
    private ImageView bgImgv;
    private TextView titleTv;
    private TextView artistTv;
    private TextView currrentTv;
    private TextView totalTv;
    private ImageView prevImgv;
    private ImageView nextImgv;
    private int position;
    private ImageView discImagv;
    private MediaPlayer mediaPlayer;
    private ImageView pauseImgv;
    private ImageView downImg;
    private ImageView styleImg;
    private SeekBar seekBar;
    private int totaltime;
    private boolean isStop;
    private ObjectAnimator objectAnimator = null;
    private RotateAnimation rotateAnimation = null;
    private RotateAnimation rotateAnimation2 = null;
    private String TAG = "MusicActivity";
    //Handler实现向主线程进行传值
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            seekBar.setProgress((int) (msg.what));
            currrentTv.setText(formatTime(msg.what));
        }
    };

    //MusicActivity onCreate（）方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);


        bingID();                                                                           //调用bingID();方法实现对控件的绑定

        Intent intent = getIntent();                                                    //通过getIntent()方法实现intent信息的获取
        position = intent.getIntExtra("position", 0);            //获取position

        mediaPlayer = new MediaPlayer();
        prevAndnextplaying(Common.musicList.get(position).path);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {               //seekbar设置监听，实现指哪放到哪
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    //prevAndnext() 实现页面的展现
    private void prevAndnextplaying(String path) {
        isStop = false;
        mediaPlayer.reset();
        titleTv.setText(Common.musicList.get(position).title);
        artistTv.setText(Common.musicList.get(position).artist + "--" + Common.musicList.get(position).album);
        pauseImgv.setImageResource(R.mipmap.ic_play_btn_pause);
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();                   // 准备
            mediaPlayer.start();                        // 启动
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(!mediaPlayer.isPlaying()){
                        setPlayMode();
                    }

                }
            });
        } catch (IllegalArgumentException | SecurityException | IllegalStateException
                | IOException e) {
            e.printStackTrace();
        }


        totalTv.setText(formatTime(Common.musicList.get(position).length));
        seekBar.setMax(Common.musicList.get(position).length);

        MusicThread musicThread = new MusicThread();                                         //启动线程
        new Thread(musicThread).start();



        rotateAnimation = new RotateAnimation(-25f, 0f, Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF, 0.1f);
        rotateAnimation.setDuration(500);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setStartOffset(500);
        rotateAnimation.cancel();


        rotateAnimation2 = new RotateAnimation(0f, -25f, Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF, 0.1f);
        rotateAnimation2.setDuration(500);
        rotateAnimation2.setInterpolator(new LinearInterpolator());
        rotateAnimation2.setRepeatCount(0);
        rotateAnimation2.setFillAfter(true);
        rotateAnimation2.cancel();


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setPlayMode() {
        if (playMode == 0)//全部循环
        {
            if (position == Common.musicList.size() - 1)//默认循环播放
            {
                position = 0;// 第一首
                mediaPlayer.reset();
                objectAnimator.pause();
                prevAndnextplaying(Common.musicList.get(position).path);

            } else {
                position++;
                mediaPlayer.reset();
                objectAnimator.pause();
                prevAndnextplaying(Common.musicList.get(position).path);
            }
        } else if (playMode == 1)//单曲循环
        {
            //position不需要更改
            mediaPlayer.reset();
            objectAnimator.pause();
            prevAndnextplaying(Common.musicList.get(position).path);
        } else if (playMode == 2)//随机
        {
            position = (int) (Math.random() * Common.musicList.size());//随机播放
            mediaPlayer.reset();
            objectAnimator.pause();
            prevAndnextplaying(Common.musicList.get(position).path);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setBtnMode() {
        if (playMode == 0)//全部循环
        {
            if (position == Common.musicList.size() - 1)//默认循环播放
            {
                if (buttonWitch == 1) {
                    position--;
                    mediaPlayer.reset();
                    objectAnimator.pause();
                    prevAndnextplaying(Common.musicList.get(position).path);
                } else if (buttonWitch == 2) {
                    position = 0;// 第一首
                    mediaPlayer.reset();
                    objectAnimator.pause();
                    prevAndnextplaying(Common.musicList.get(position).path);
                }
            } else if (position == 0) {
                if (buttonWitch == 1) {
                    position = Common.musicList.size() - 1;
                    mediaPlayer.reset();
                    objectAnimator.pause();
                    prevAndnextplaying(Common.musicList.get(position).path);
                } else if (buttonWitch == 2) {
                    position++;
                    mediaPlayer.reset();
                    objectAnimator.pause();
                    prevAndnextplaying(Common.musicList.get(position).path);
                }
            }else {
                if(buttonWitch ==1){
                    position--;
                    mediaPlayer.reset();
                    objectAnimator.pause();
                    prevAndnextplaying(Common.musicList.get(position).path);

                }else if(buttonWitch ==2){
                    position++;
                    mediaPlayer.reset();
                    objectAnimator.pause();
                    prevAndnextplaying(Common.musicList.get(position).path);
                }
            }
        } else if (playMode == 1)//单曲循环
        {
            //position不需要更改
            mediaPlayer.reset();
            objectAnimator.pause();
            prevAndnextplaying(Common.musicList.get(position).path);
        } else if (playMode == 2)//随机
        {
            position = (int) (Math.random() * Common.musicList.size());//随机播放
            mediaPlayer.reset();
            objectAnimator.pause();
            prevAndnextplaying(Common.musicList.get(position).path);
        }
    }

    //格式化数字
    private String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");    //规定固定的格式
        String totaltime = simpleDateFormat.format(date);
        return totaltime;
    }


    //绑定id，设置监听
    private void bingID() {
        titleTv = findViewById(R.id.music_title_tv);
        artistTv = findViewById(R.id.music_artist_tv);
        bgImgv = findViewById(R.id.music_bg_imgv);
        currrentTv = findViewById(R.id.music_current_tv);
        totalTv = findViewById(R.id.music_total_tv);
        prevImgv = findViewById(R.id.music_prev_imgv);
        nextImgv = findViewById(R.id.music_next_imgv);
        discImagv = findViewById(R.id.music_disc_imagv);
        pauseImgv = findViewById(R.id.music_pause_imgv);
        downImg = findViewById(R.id.music_down_imgv);
        seekBar = findViewById(R.id.music_seekbar);
        styleImg = findViewById(R.id.music_play_btn_loop_img);
        pauseImgv.setOnClickListener(this);
        prevImgv.setOnClickListener(this);
        nextImgv.setOnClickListener(this);
        downImg.setOnClickListener(this);
        styleImg.setOnClickListener(this);

    }

    //onClick（）点击监听
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_prev_imgv:
                buttonWitch = 1;
                setBtnMode();
                break;
            case R.id.music_next_imgv:
                buttonWitch = 2;
                setBtnMode();
                break;
            case R.id.music_pause_imgv:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    objectAnimator.pause();
                    pauseImgv.setImageResource(R.mipmap.ic_play_btn_play);
                } else {
                    mediaPlayer.start();
                    objectAnimator.resume();
                    pauseImgv.setImageResource(R.mipmap.ic_play_btn_pause);
                }
                break;
            case R.id.music_play_btn_loop_img:
                i++;
                if (i % 3 == 1) {
                    Toast.makeText(MusicActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
                    playMode = 1;
                    styleImg.setImageResource(R.mipmap.ic_play_btn_one);
                }
                if (i % 3 == 2) {
                    Toast.makeText(MusicActivity.this, "随机播放", Toast.LENGTH_SHORT).show();
                    playMode = 2;
                    styleImg.setImageResource(R.mipmap.ic_play_btn_shuffle);
                }
                if (i % 3 == 0) {
                    Toast.makeText(MusicActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
                    playMode = 0;
                    styleImg.setImageResource(R.mipmap.ic_play_btn_loop);
                }
                break;
            case R.id.music_down_imgv:
                this.finish();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        for (Music music : Common.musicList
                ) {
            music.isPlaying = false;
        }
        Common.musicList.get(position).isPlaying = true;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        i = 0;
        isStop = false;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }


    }

    //创建一个类MusicThread实现Runnable接口，实现多线程
    class MusicThread implements Runnable {

        @Override
        public void run() {
            while (!isStop && Common.musicList.get(position) != null) {
                try {
                    //让线程睡眠1000毫秒
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //放送给Handler现在的运行到的时间，进行ui更新
                handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());

            }
        }
    }


}

