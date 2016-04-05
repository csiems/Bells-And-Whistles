package com.epicodus.bellsandwhistles;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.bellsandwhistles.utils.OnDoubleTapListener;
import com.epicodus.bellsandwhistles.utils.OnSwipeTouchListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, SensorEventListener {
    @Bind(R.id.doubleTapTextView) TextView mDoubleTapTextView;
    @Bind(R.id.longTapTextView) TextView mLongTapTextView;
    @Bind(R.id.dragTextView) TextView mDragTextView;
    @Bind(R.id.parentRelativeLayout) RelativeLayout mParentRelativeLayout;
    @Bind(R.id.outsideRelativeLayout) RelativeLayout mOutsideRelativeLayout;
    @Bind(R.id.shakeInstructionsTextView) TextView mShakeInstructionsTextView;
    @Bind(R.id.flingTextView) TextView mFlingTextView;

    private final String msg = MainActivity.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1500;
    private long lastShakeTime = 0;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLongTapTextView.setOnLongClickListener(this);
        mDragTextView.setOnLongClickListener(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, mSensorManager.SENSOR_DELAY_NORMAL);

        mOutsideRelativeLayout.setOnDragListener(new View.OnDragListener() {

            @Override
            public boolean onDrag(View v, DragEvent event) {
                return true;
            }
        });


        mParentRelativeLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final View draggedView = (View) event.getLocalState();

                switch(event.getAction())
                {
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        break;

                    case DragEvent.ACTION_DRAG_EXITED :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION  :
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "ACTION_DROP event");
                        //getx and gety are getting coords of middle of view
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        //setx and sety set the upper left corner of view to those coords
                        //the math below corrects for that
                        draggedView.setX(x_cord - (draggedView.getWidth()/2));
                        draggedView.setY(y_cord - (draggedView.getHeight()/2));
                        draggedView.setVisibility(View.VISIBLE);
                        break;

                    case DragEvent.ACTION_DRAG_ENDED   :
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");
                        draggedView.setVisibility(View.VISIBLE);
                        break;

                    default: break;
                }
                return true;
            }
        });

        mDoubleTapTextView.setOnTouchListener(new OnDoubleTapListener(this) {
            @Override
            public void onDoubleTap(MotionEvent e) {
                if( mDoubleTapTextView.getAlpha() == 1) {
                    soundManager("downStairs");
                    mDoubleTapTextView.animate()
                            .alpha(0.4f)
                            .rotation(720)
                            .yBy(500)
                            .setDuration(2640)
                            .withStartAction(setTextRunnable(mDoubleTapTextView, "Wheee!"))
                            .withEndAction(setTextRunnable(mDoubleTapTextView, "Double Tap Me!"));
                } else {
                    soundManager("twirlyWhirly");
                    mDoubleTapTextView.animate()
                            .alpha(1f)
                            .rotation(0)
                            .yBy(-500)
                            .setDuration(4100)
                            .withStartAction(setTextRunnable(mDoubleTapTextView, "Wheee!"))
                            .withEndAction(setTextRunnable(mDoubleTapTextView, "Double Tap Me!"));
                }
            }
        });



        mFlingTextView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {
                soundManager("slideWhistle");
                mFlingTextView.animate()
                        .translationY(500)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(550)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                mFlingTextView.animate()
                                        .translationY(0)
                                        .setDuration(320);
                            }
                        });

            }

            @Override
            public void onSwipeLeft() {
                soundManager("boing");
                mFlingTextView.animate()
                        .translationX(-50)
                        .setInterpolator(new BounceInterpolator())
                        .setDuration(650);
            }

            @Override
            public void onSwipeUp() {
                mFlingTextView.animate()
                        .translationY(-500)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(550)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                mFlingTextView.animate()
                                        .translationY(0)
                                        .setDuration(320);
                            }
                        });
            }

            @Override
            public void onSwipeRight() {
                soundManager("boing");
                mFlingTextView.animate()
                        .translationX(50)
                        .setInterpolator(new BounceInterpolator())
                        .setDuration(650);
            }

        });


    }

    @Override
    public boolean onLongClick(View v) {
        if (v == mLongTapTextView) {
            mLongTapTextView.animate()
                .rotation(5)
                .setInterpolator(new CycleInterpolator(10))
                .setDuration(1000)
                .withStartAction(setTextRunnable(mLongTapTextView, "Whoaa!"))
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        if (vibrator.hasVibrator()) {
                            vibrator.vibrate(2320);
                        }
                        soundManager("earthquake");
                        mLongTapTextView.animate()
                            .translationX(30)
                            .setInterpolator(new CycleInterpolator(20))
                            .setDuration(2320)
                            .withStartAction(setTextRunnable(mLongTapTextView, "Earthquake!"))
                            .withEndAction(setTextRunnable(mLongTapTextView, "Long Click Me!"));
                    }
                });

        }
        if (v == mDragTextView) {
            View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(v);
            v.startDrag(null, dragShadow, v, 0);
            v.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    public Runnable setTextRunnable(final TextView v, final String message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                v.setText(message);
            }
        };
        return runnable;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastUpdate) > 100) {
                long timeDifference = currentTime - lastUpdate;
                lastUpdate = currentTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/timeDifference * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    long now = System.currentTimeMillis();
                    if (now - lastShakeTime > 1000) {
                        ColorDrawable background = (ColorDrawable) mOutsideRelativeLayout.getBackground();
                        if (background.getColor() == getResources().getColor(R.color.colorAccent)) {
                            mOutsideRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                            mShakeInstructionsTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                        } else {
                            mOutsideRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            mShakeInstructionsTextView.setTextColor(getResources().getColor(R.color.colorGrey));
                        }
                        lastShakeTime = System.currentTimeMillis();
                    }
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void soundManager(String soundName) {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        if (soundName.equals("downStairs")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.comedy_music_falling_down_stairs);
        }

        if (soundName.equals("slideWhistle")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.comedy_slide_whistle_playing_down);
        }

        if (soundName.equals("twirlyWhirly")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.comedy_twirly_whirly);
        }
        if (soundName.equals("earthquake")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.dragging_stone_along_concrete);
        }
        if (soundName.equals("boing")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.comedy_boing_jews_harp_sprong);

        }
        mediaPlayer.start();
    }


}
