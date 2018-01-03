package indi.aljet.myleafpic_dev.util;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by PC-LJL on 2017/11/14.
 */

public class RecyclerItemClickListener implements
        RecyclerView.OnItemTouchListener {

    public interface OnItemClickListener{
        void onItemClick(View view ,int position);
        void onLongItemClick(View view,int position);
    }

    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    public RecyclerItemClickListener(final RecyclerView rv,
                                     final OnItemClickListener mListener) {
        this.mListener = mListener;
        mGestureDetector = new GestureDetector(rv.getContext()
        ,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(),
                        e.getY());
                if(child != null && mListener != null){
                    mListener.onLongItemClick(child,
                            rv.getChildAdapterPosition(child));
                }
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(),
                e.getY());
        if(childView != null && mListener != null && mGestureDetector
                .onTouchEvent(e)){
            mListener.onItemClick(childView,
                    rv.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
