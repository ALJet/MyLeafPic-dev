package indi.aljet.myleafpic_dev.views;

import android.animation.AnimatorSet;
import android.view.GestureDetector;
import android.view.VelocityTracker;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * Created by PC-LJL on 2017/11/6.
 */

public class ZoomImageView {
    private boolean draggingDown = false;
    private float dragY;
    private float translationX;
    private float translationY;
    private float scale = 1;
    private float animationToX;
    private float animationToY;
    private float animateToScale;
    private float animationValue;
    private int currentRotation;
    private long animationStartTime;
    private AnimationSet imageMoveAnimation;
    private AnimatorSet changeModeAnimation;
    private GestureDetector gestureDetector;
    private DecelerateInterpolator interpolator = new
            DecelerateInterpolator(1.5f);
    private float pinchStartDistance;
    private float pinchStartScale = 1;
    private float pincheCenterX;
    private float pincheStartX;
    private float pincheStartY;
    private float moveStartX;
    private float moveStartY;
    private float minX;
    private float minY;
    private float maxX;
    private float maxY;
    private boolean canZoom = true;
    private boolean changingPage = false;
    private boolean zooming = false;
    private boolean moving = false;
    private boolean doubleTap = false;
    private boolean invalidCorrds = false;
    private boolean canDragDown = true;
    private boolean zommAnimation = false;
    private boolean discardTap = false;
    private int switchImageAfterAnimation = 0;
    private VelocityTracker velocityTracker = null;
    private Scroller scroller = null;

}
