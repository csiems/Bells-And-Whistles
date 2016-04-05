package com.epicodus.bellsandwhistles;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.epicodus.bellsandwhistles.utils.OnDoubleTapListener;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {
    @Bind(R.id.doubleTapTextView) TextView mDoubleTapTextView;
    @Bind(R.id.longTapTextView) TextView mLongTapTextView;
    @Bind(R.id.dragTextView) TextView mDragTextView;
    @Bind(R.id.parentRelativeLayout) RelativeLayout mParentRelativeLayout;
    @Bind(R.id.outsideRelativeLayout) RelativeLayout mOutsideRelativeLayout;

    private final String msg = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLongTapTextView.setOnLongClickListener(this);
        mDragTextView.setOnLongClickListener(this);

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
                    mDoubleTapTextView.animate()
                            .alpha(0.4f)
                            .rotation(720)
                            .xBy(200)
                            .yBy(200)
                            .setDuration(2000)
                            .withStartAction(setTextRunnable(mDoubleTapTextView, "Wheee!"))
                            .withEndAction(setTextRunnable(mDoubleTapTextView, "Double Tap Me!"));
                } else {
                    mDoubleTapTextView.animate()
                            .alpha(1f)
                            .rotation(0)
                            .xBy(-200)
                            .yBy(-200)
                            .setDuration(2000)
                            .withStartAction(setTextRunnable(mDoubleTapTextView, "Wheee!"))
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
                    .withEndAction(setTextRunnable(mLongTapTextView, "Long Click Me!"));
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

}
