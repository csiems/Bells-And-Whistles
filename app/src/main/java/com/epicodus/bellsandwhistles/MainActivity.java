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
                Toast.makeText(MainActivity.this, "You double-tapped the thing!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
