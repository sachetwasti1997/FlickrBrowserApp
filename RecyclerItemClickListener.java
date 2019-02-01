package com.sachet.flickrbrowser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "RecyclerItemClickListen";
    /*All the approaches to use the recycleview listener uses a callback mechanism.
    * So we must use an Interface to call back on*/
    interface OnRecyclerClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private final OnRecyclerClickListener mOnRecyclerClickListener;
    private final GestureDetectorCompat mGestureDetectorCompat;

    /*Detects various gestures and events using the supplied MotionEvents. The GestureDetector.OnGestureListener
    * callback will notify users when a particular motion event has occurred. This class should only be used with
    * MotionEvents reported via touch (don't use for trackball events). To use this class:
        ->Create an instance of the GestureDetector for your View
        ->In the View.onTouchEvent(MotionEvent) method ensure you call onTouchEvent(MotionEvent). The methods
          defined in your callback will be executed when the events occur.
        ->If listening for GestureDetector.OnContextClickListener.onContextClick(MotionEvent) you must call
          onGenericMotionEvent(MotionEvent) in View.onGenericMotionEvent(MotionEvent).*/
    /*We need a context for our gesture detector to work and also a reference to the recyclerview that we detecting the taps on*/
    /*We can override OnInterruptTouchEvent method to intercept all the touch events that may happen in the recyclerview, this method
     is called whenever any type of touch happens as tap, doubletap, swipe,or anything*/

    /*Firstly both methods do the asame thing, by using the motionevent that is passed as the parameter they check to see which event is
     underneath it by usint the coordinates on the screen that were tapped*/
    /*findChildViewUnder() does the work of finding the view underneath it if any using the coordinates supplied*/
    public RecyclerItemClickListener(Context context,final RecyclerView recyclerView,OnRecyclerClickListener listener) {
        mGestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(mOnRecyclerClickListener!=null&&childView!=null){
                    mOnRecyclerClickListener.onItemClick(childView,recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(mOnRecyclerClickListener!=null&&childView!=null){
                    mOnRecyclerClickListener.onItemLongClick(childView,recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
                mOnRecyclerClickListener = listener;
    }

    /*Android framework causes the below method which inturn calls the onTouch method of the gesture detector, which then calls the appropriate method for
     the gesturing question*/
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        if(mGestureDetectorCompat!=null){
            boolean result = mGestureDetectorCompat.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: returned: "+result);
            return result;
        }else{
            Log.d(TAG, "onInterceptTouchEvent: returned false");
            return false;
        }
//        return false;
        //If we returned a true here we wont be able to do the scrolling
        /*The onInterceptTouchEvent() method is called whenever a touch event is detected on the surface of a
        * ViewGroup, including on the surface of its children. If onInterceptTouchEvent() returns true, the MotionEvent
        * is intercepted, meaning it is not passed on to the child, but rather to the onTouchEvent() method of the parent.*/
        /*The onInterceptTouchEvent() method gives a parent the chance to see any touch event before its children do. If you
         return true from onInterceptTouchEvent(), the child view that was previously handling touch events receives an
         ACTION_CANCEL, and the events from that point forward are sent to the parent's onTouchEvent() method for the usual
         handling. onInterceptTouchEvent() can also return false and simply spy on events as they travel down the view hierarchy
         to their usual targets, which will handle the events with their own onTouchEvent(). */
    }
}
