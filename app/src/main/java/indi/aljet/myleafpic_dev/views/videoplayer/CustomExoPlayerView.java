package indi.aljet.myleafpic_dev.views.videoplayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.upstream.ParsingLoadable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import indi.aljet.myleafpic_dev.R;

/**
 * Created by PC-LJL on 2017/10/30.
 */

public class CustomExoPlayerView extends FrameLayout {



    View surfaceView;
    @BindView(R.id.shutter)
    View shutterView;
    @BindView(R.id.subtitles)
    SubtitleView subtitleLayout;
    @BindView(R.id.video_frame)
      AspectRatioFrameLayout layout;

    @BindView(R.id.control)
      CustomPlayBackController controller;
    private  final ComponentListener componentListener;

    private SimpleExoPlayer player;
    private boolean useController = true;
    private int controllerShowTimeoutMs;

    public CustomExoPlayerView(Context context) {
        this(context,null);
    }

    public CustomExoPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomExoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        boolean useTextureView = false;
        int resizeMode = AspectRatioFrameLayout
                .RESIZE_MODE_FIT;
        int rewindMs =CustomPlayBackController
                .DEFAULT_REWIND_MS;
        int fastForwardMs = CustomPlayBackController
                .DEFAULT_FAST_FORWARD_MS;
        int controllerShowTimeoutMs =
                CustomPlayBackController
                .DEFAULT_SHOW_TIMEOUT_MS;
        if(attrs != null){
            TypedArray a = context.getTheme()
                    .obtainStyledAttributes(attrs,
                            com.google.android.exoplayer2.R
                    .styleable.SimpleExoPlayerView,0,0);
            try{
                useController = a.getBoolean(com.google.android.exoplayer2.R
                .styleable.SimpleExoPlayerView_use_controller,
                        useController);
                //无法找到SimpleExoPlayerView_use_texture_view
                useTextureView = a.getBoolean(com.google.android.exoplayer2.
                        R.styleable.SimpleExoPlayerView_use_controller,
                        useTextureView);
                resizeMode = a.getInt(com.google.android.exoplayer2.R
                .styleable.SimpleExoPlayerView_resize_mode,
                        AspectRatioFrameLayout
                .RESIZE_MODE_FIT);
                rewindMs = a.getInt(com.google.android.exoplayer2.R
                .styleable.SimpleExoPlayerView_rewind_increment,
                        rewindMs);
                fastForwardMs = a.getInt(com.google.android.exoplayer2.R
                .styleable.SimpleExoPlayerView_fastforward_increment,
                        fastForwardMs);
                controllerShowTimeoutMs = a.getInt(com.google.android.exoplayer2.R
                .styleable.SimpleExoPlayerView_show_timeout,
                        controllerShowTimeoutMs);
            }finally{
                a.recycle();
            }
        }
        LayoutInflater.from(context).inflate(R
        .layout.exo_player,this);
        componentListener = new ComponentListener();
        ButterKnife.bind(this);
        subtitleLayout.setUserDefaultStyle();
        subtitleLayout.setUserDefaultTextSize();

        controller.hide();
        controller.setRewindIncrementMs(rewindMs);
        controller.setFastForwardIncrementMs(fastForwardMs);
        this.controllerShowTimeoutMs = controllerShowTimeoutMs;

        View view = useTextureView ? new
                TextureView(context) : new SurfaceView(context);
        ViewGroup.LayoutParams params = new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        surfaceView = view;
        layout.addView(surfaceView,0);
    }


    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public void setPlayer(SimpleExoPlayer player) {
        if (this.player == player) {
            return;
        }
        if (this.player != null) {
            this.player.setTextOutput(null);
            this.player.setVideoListener(null);
            this.player.removeListener(componentListener);
            this.player.setVideoSurface(null);
        }
        this.player = player;
        if (useController) {
            controller.setPlayer(player);
        }
        if (player != null) {
            if (surfaceView instanceof TextureView) {
                player.setVideoTextureView((TextureView) surfaceView);
            } else if (surfaceView instanceof SurfaceView) {
                player.setVideoSurfaceView((SurfaceView) surfaceView);
            }
            player.setVideoListener(componentListener);
            player.addListener(componentListener);
            player.setTextOutput(componentListener);
            maybeShowController(false);
        } else {
            shutterView.setVisibility(VISIBLE);
            controller.hide();
        }
    }

    public void setResizeMode(int resizeMode) {
        layout.setResizeMode(resizeMode);
    }

    public boolean getUseController() {
        return useController;
    }

    public void setUseController(boolean useController) {
        if (this.useController == useController) {
            return;
        }
        this.useController = useController;
        if (useController) {
            controller.setPlayer(player);
        } else {
            controller.hide();
            controller.setPlayer(null);
        }
    }

    public int getControllerShowTimeoutMs() {
        return controllerShowTimeoutMs;
    }

    public void setControllerShowTimeoutMs(
            int controllerShowTimeoutMs) {
        this.controllerShowTimeoutMs = controllerShowTimeoutMs;}

    public void setControllerVisibilityListener(
            CustomPlayBackController.VisibilityListener listener) {
        controller.setVisibilityListener(listener);
    }

    public void setRewindIncrementMs(int rewindMs) {
        controller.setRewindIncrementMs(rewindMs);
    }

    public void setFastForwardIncrementMs(int fastForwardMs) {
        controller.setFastForwardIncrementMs(fastForwardMs);
    }

    public View getVideoSurfaceView() {
        return surfaceView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!useController || player == null ||
                event.getAction() != MotionEvent
                .ACTION_DOWN){
            return false;
        }
        if(controller.isVisible()){
            controller.hide();
        }else{
            maybeShowController(true);
        }
        return true;
    }


    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        if(!useController || player == null){
            return false;
        }
        maybeShowController(true);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return useController ? controller
                .dispatchTouchEvent(ev)
                : super.dispatchTouchEvent(ev);
    }



    private void maybeShowController(boolean isForced){
        if(!useController || player == null){
            return;
        }
        int playbackState = player.getPlaybackState();
        boolean showIndefinitely = playbackState ==
                ExoPlayer.STATE_IDLE || playbackState == ExoPlayer
                .STATE_ENDED || !player.getPlayWhenReady();
        boolean wasShowingIndefinitely = controller
                .isVisible() && controller.getShowTimeoutMs()
                <= 0;
        controller.setShowTimeoutMs(showIndefinitely ? 0 :
        controllerShowTimeoutMs);
        if(isForced || showIndefinitely || wasShowingIndefinitely){
            controller.show();
        }
    }


    private final class ComponentListener implements
            SimpleExoPlayer.VideoListener,
            TextRenderer.Output,ExoPlayer
    .EventListener{
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }


        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            maybeShowController(false);
        }



        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity() {

        }



        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float
                pixelWidthHeightRatio) {
            layout.setAspectRatio(height == 0 ? 1 :
                    (width * pixelWidthHeightRatio) / height);

        }



        @Override
        public void onRenderedFirstFrame() {
            shutterView.setVisibility(GONE);
        }

        @Override
        public void onVideoTracksDisabled() {

        }

        @Override
        public void onCues(List<Cue> cues) {
            subtitleLayout.onCues(cues);
        }
    }

}
