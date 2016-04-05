package com.epicodus.bellsandwhistles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.bellsandwhistles.utils.OnDoubleTapListener;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {
    @Bind(R.id.doubleTapTextView) TextView mDoubleTapTextView;
    @Bind(R.id.longTapTextView) TextView mLongTapTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLongTapTextView.setOnLongClickListener(this);

        mDoubleTapTextView.setOnTouchListener(new OnDoubleTapListener(this) {
            @Override
            public void onDoubleTap(MotionEvent e) {
                if( mDoubleTapTextView.getAlpha() == 1) {
                    mDoubleTapTextView.animate()
                            .alpha(0.4f)
                            .rotation(720)
                            .xBy(200)
                            .yBy(200)
                            .setDuration(2000)
                            .withEndAction(setTextRunnable(mDoubleTapTextView, "Wheee!"));
                } else {
                    mDoubleTapTextView.animate()
                            .alpha(1f)
                            .rotation(0)
                            .xBy(-200)
                            .yBy(-200)
                            .setDuration(2000)
                            .withEndAction(setTextRunnable(mDoubleTapTextView, "Double Tap Me!"));
                }
            }
        });


    }

    @Override
    public boolean onLongClick(View v) {
        if (v == mLongTapTextView) {
            mLongTapTextView.animate()
                    .translationX(30)
                    .setInterpolator(new CycleInterpolator(20))
                    .setDuration(3000)
                    .withStartAction(setTextRunnable(mLongTapTextView, "Earthquake!"))
                    .withEndAction(setTextRunnable(mLongTapTextView, "Long Press Me!"));
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

}
