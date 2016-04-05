package com.epicodus.bellsandwhistles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.bellsandwhistles.utils.OnDoubleTapListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.doubleTapTextView) TextView mDoubleTapTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDoubleTapTextView.setOnTouchListener(new OnDoubleTapListener(this) {
            @Override
            public void onDoubleTap(MotionEvent e) {
                if( mDoubleTapTextView.getAlpha() == 1) {
                    mDoubleTapTextView.animate()
                            .alpha(0.4f)
                            .rotation(720)
                            .xBy(200)
                            .yBy(200)
                            .setDuration(2000).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            mDoubleTapTextView.setText("Wheee!!!!!");
                        }
                    });
                } else {
                    mDoubleTapTextView.animate()
                            .alpha(1f)
                            .rotation(0)
                            .xBy(-200)
                            .yBy(-200)
                            .setDuration(2000).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            mDoubleTapTextView.setText("Double Tap Me!");
                        }
                    });

                }
            }
        });
    }
}
