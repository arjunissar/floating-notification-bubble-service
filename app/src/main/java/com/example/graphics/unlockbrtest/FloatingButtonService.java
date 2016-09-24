package com.example.graphics.unlockbrtest;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by graphics on 9/22/2016.
 */
public class FloatingButtonService extends Service {

        private WindowManager windowManager;
        private ImageView chatHead;
        private RelativeLayout mRelativeLayout;
        private RelativeLayout mInnerLayout;
        private boolean isLayoutVisible = true;
        private GestureDetector gestureDetector;
        private View mView;
        private View mPointer;
        private Button mClose;

        @Override public IBinder onBind(Intent intent) {
            // Not used
            return null;
        }

        @Override public void onCreate() {
            super.onCreate();
            Log.v("Service Created","Service Created");
            gestureDetector = new GestureDetector(this, new SingleTapConfirm());
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);

            LayoutInflater inflater = (LayoutInflater) this.getSystemService( this.LAYOUT_INFLATER_SERVICE );
            mView = inflater.inflate(R.layout.floating, null);

//            mRelativeLayout = new RelativeLayout(this);
//
//            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            rlp.setMargins(16,16,16,16);
//
//            mRelativeLayout.setLayoutParams(params);
//
//            chatHead = new ImageView(this);
//            chatHead.setImageResource(R.drawable.launcher);
//            chatHead.setId(View.generateViewId());
//            chatHead.setElevation(5.0f);
//            chatHead.setPadding(20,16,16,16);
//            chatHead.setForegroundGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
//            chatHead.setBackground(getResources().getDrawable(R.drawable.shape_circular));
//
//            RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
//                    150,
//                    150);
//            buttonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            buttonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            buttonParams.setMargins(16,0,0,0);
//
//            mRelativeLayout.addView(chatHead, buttonParams);
//
//            final RelativeLayout innerRelativeLayout = new RelativeLayout(this);
//            innerRelativeLayout.setBackground(getResources().getDrawable(R.drawable.shape_round_edges));
//            innerRelativeLayout.setElevation(5.0f);
//
//            RelativeLayout.LayoutParams irlp = new RelativeLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            irlp.setMargins(16,16,16,16);
//            irlp.addRule(RelativeLayout.BELOW, chatHead.getId());
//
//            mRelativeLayout.addView(innerRelativeLayout, irlp);
//
//            mRelativeLayout.setLayoutParams(params);
//
//            chatHead.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(isLayoutVisible) {
//                        innerRelativeLayout.setVisibility(View.GONE);
//                        isLayoutVisible = false;
//                    } else {
//                        innerRelativeLayout.setVisibility(View.VISIBLE);
//                        isLayoutVisible = true;
//                    }
//
//                  }
//            });

            mRelativeLayout = (RelativeLayout) mView.findViewById(R.id.outerRelativeLayout);
            mInnerLayout = (RelativeLayout) mView.findViewById(R.id.innerRelativeLayout);
            mPointer = (View) mView.findViewById(R.id.pointer);
            mClose = (Button) mView.findViewById(R.id.buttonClose);

            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopSelf();
                }
            });

            chatHead = (ImageView) mView.findViewById(R.id.imagebutton);

            windowManager.addView(mView, params);

            chatHead.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override public boolean onTouch(View v, MotionEvent event) {
                        if (gestureDetector.onTouchEvent(event)) {
                            if (isLayoutVisible) {
                                mInnerLayout.setVisibility(View.GONE);
                                mPointer.setVisibility(View.GONE);
                                isLayoutVisible = false;
                            } else {
                                mInnerLayout.setVisibility(View.VISIBLE);
                                mPointer.setVisibility(View.VISIBLE);
                                isLayoutVisible = true;
                            }
                            return true;
                        } else {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    initialX = params.x;
                                    initialY = params.y;
                                    initialTouchX = event.getRawX();
                                    initialTouchY = event.getRawY();
                                    return true;
                                case MotionEvent.ACTION_UP:
                                    return true;
                                case MotionEvent.ACTION_MOVE:
                                    params.x = initialX + (int) (event.getRawX() - initialTouchX);
                                    params.y = initialY + (int) (event.getRawY() - initialTouchY);
                                    windowManager.updateViewLayout(mView, params);
                                    return true;
                            }
                        }
                    return false;
                }
            });
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (mRelativeLayout != null) windowManager.removeView(mView);
        }

        private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                return true;
            }
        }
}

