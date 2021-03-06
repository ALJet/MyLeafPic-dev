package indi.aljet.myleafpic_dev.views.videoplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;

import com.google.android.exoplayer2.util.Util;


import java.util.Formatter;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import horaapps.org.liz.ThemeHelper;
import indi.aljet.myleafpic_dev.R;

/**
 * Created by PC-LJL on 2017/10/30.
 */

public class CustomPlayBackController extends FrameLayout {

    public interface VisibilityListener{
        void  onVisibilityChange(int visibility);
    }

    private ThemeHelper themeHelper;
    public static final int DEFAULT_FAST_FORWARD_MS = 15000;
    public static final int DEFAULT_REWIND_MS = 5000;
    public static final int DEFAULT_SHOW_TIMEOUT_MS = 5000;
    private static final int PROGRESS_BAR_MAX = 1000;
    private static final long MAX_POSITION_FOR_SEEK_TO_PREVIOUS = 3000;

    private final ComponentListener componentListener;
    @BindView(R.id.prev)
      View previousButton;
    @BindView(R.id.next)
      View nextButton;

    @BindView(R.id.play)
      ImageButton playButton;
    @BindView(R.id.time)
      TextView time;
    @BindView(R.id.time_current)
    TextView timeCurrent;
    @BindView(R.id.mediacontroller_progress)
      SeekBar progressBar;

    @BindView(R.id.ffwd)
    View fastForwardButton;
    @BindView(R.id.rew)
        View rewindButton;
    private final StringBuilder formatBuilder;
    private final Formatter formatter;
    private final Timeline.Window currentWindow;

    private ExoPlayer player;
    private VisibilityListener visibilityListener;

    private boolean isAttachedToWindow;
    private boolean dragging;
    private int rewindMs;
    private int fastForwardMs;
    private int showTimeoutMs;
    private long hideAtMs;

    private final Runnable updateProgressAction = new
            Runnable() {
                @Override
                public void run() {
                    updateProgress();
                }
            };

    private Runnable hideAction = new
            Runnable() {
                @Override
                public void run() {
                    hide();
                }
            };


    public CustomPlayBackController(Context context){
        this(context,null);
    }

    public CustomPlayBackController(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomPlayBackController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rewindMs = DEFAULT_REWIND_MS;
        fastForwardMs = DEFAULT_FAST_FORWARD_MS;
        showTimeoutMs = DEFAULT_SHOW_TIMEOUT_MS;
        if(attrs != null){
            TypedArray a = context.getTheme()
                    .obtainStyledAttributes(attrs,
                            R.styleable.PlaybackControlView,0,0);
            try{
                rewindMs = a.getInt(R.styleable.PlaybackControlView_fastforward_increment,
                        fastForwardMs);
                showTimeoutMs = a.getInt(R
                .styleable.PlaybackControlView_show_timeout,
                        showTimeoutMs);
            }finally{
                a.recycle();
            }
        }
        currentWindow = new Timeline.Window();
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        componentListener = new ComponentListener();
        LayoutInflater.from(context).inflate(R
        .layout.exo_media_control,this);
        ButterKnife.bind(this);

        progressBar.setOnSeekBarChangeListener(componentListener);
        progressBar.setMax(PROGRESS_BAR_MAX);
        playButton.setOnClickListener(componentListener);
        previousButton.setOnClickListener(componentListener);
        nextButton.setOnClickListener(componentListener);
        rewindButton.setOnClickListener(componentListener);
        fastForwardButton.setOnClickListener(componentListener);

        themeHelper = ThemeHelper.getInstance(getContext());

//        progressBar.getProgressDrawable().
//                setColorFilter(
//                        new PorterDuffColorFilter
//                                (themeHelper.isPrimaryEqualAccent()
//                ? ColorPalette.getDarkerColor
//                                        (themeHelper.getAccentColor()):
//                                        themeHelper.getAccentColor(), PorterDuff.Mode.SRC_IN));
//        progressBar.getThumb().
//                setColorFilter(
//                        new PorterDuffColorFilter
//                                (themeHelper.isPrimaryEqualAccent()
//                ? ColorPalette.getDarkerColor
//                                        (themeHelper.getAccentColor())
//                                        : themeHelper.getAccentColor(),
//                                        PorterDuff.Mode.SRC_IN));

        findViewById(R.id.exoplayer_controller_background)
                .setBackgroundColor(themeHelper.getPrimaryColor());

    }


    public ExoPlayer getPlayer(){
        return player;
    }

    public void setPlayer(ExoPlayer player){
        if(this.player == player){
            return;
        }
        if(this.player != null){
            this.player.removeListener(componentListener);
        }
        this.player = player;
        if(player != null){
            player.addListener(componentListener);
        }
        updateAll();
    }

    public void setVisibilityListener(VisibilityListener listener){
        this.visibilityListener = listener;
    }

    public void setRewindIncrementMs(int rewindMs){
        this.rewindMs = rewindMs;
        updateNavigation();
    }

    public void setFastForwardIncrementMs(int fastForwardMs){
        this.fastForwardMs = fastForwardMs;
        updateNavigation();
    }

    public int getShowTimeoutMs(){
        return showTimeoutMs;
    }


    public void setShowTimeoutMs(int showTimeoutMs){
        this.showTimeoutMs = showTimeoutMs;
    }


    public void show(){
        if(isVisible()){
            setVisibility(VISIBLE);
            if(visibilityListener != null){
                visibilityListener.onVisibilityChange(getVisibility());
            }
            updateAll();
        }
        hideAfterTimeout();
    }

    public void hide(){
        if(isVisible()){
            setVisibility(GONE);
            if(visibilityListener != null){
                visibilityListener
                        .onVisibilityChange(getVisibility());
            }
            removeCallbacks(updateProgressAction);
            removeCallbacks(hideAction);
            hideAtMs = C.TIME_UNSET;
        }
    }

    public boolean isVisible(){
        return getVisibility() == VISIBLE;
    }

    private void hideAfterTimeout(){
        removeCallbacks(hideAction);
        if(showTimeoutMs > 0 ){
            hideAtMs = SystemClock.uptimeMillis() +
                    showTimeoutMs;
            if(isAttachedToWindow){
                postDelayed(hideAction,showTimeoutMs);
            }
        }else{
            hideAtMs = C.TIME_UNSET;
        }
    }

    private void updateAll(){
        updatePlayPauseButton();
        updateNavigation();
        updateProgress();
    }


    private void updatePlayPauseButton(){

        if(!isVisible() || !isAttachedToWindow){
            return;
        }
        boolean playing = player != null && player
                .getPlayWhenReady();
        String contentDescription = getResources()
                .getString(playing ?
                        R.string.exo_controls_pause_description :
                        R.string.exo_controls_play_description );
        playButton.setContentDescription(
                contentDescription);
        playButton.setImageResource(playing ? R.drawable
        .exo_controls_pause : R.drawable
        .exo_controls_play);
    }

    private void updateNavigation(){
        if(!isVisible() || !isAttachedToWindow){
            return;
        }
        Timeline currentTimeline = player != null ?
                player.getCurrentTimeline() : null;
        boolean haveTimeline = currentTimeline != null;
        boolean isSeekable = false;
        boolean enablePrevious = false;
        boolean enableNext = false;
        if(haveTimeline){
            int currentWindowIndex = player
                    .getCurrentWindowIndex();
            currentTimeline.getWindow(currentWindowIndex
            ,currentWindow);
            isSeekable = currentWindow.isSeekable;
            enablePrevious = currentWindowIndex > 0
                    || isSeekable || !currentWindow
                    .isDynamic;
            enableNext = (currentWindowIndex < currentTimeline
            .getWindowCount() - 1)|| currentWindow.isDynamic;
        }
        setButtonEnabled(enablePrevious,previousButton);
        setButtonEnabled(enableNext,nextButton);
        setButtonEnabled(fastForwardMs > 0 && isSeekable,
                fastForwardButton);
        setButtonEnabled(rewindMs > 0 && isSeekable,
                rewindButton);
        progressBar.setEnabled(isSeekable);
    }

    private void updateProgress(){
        if(!isVisible() || !isAttachedToWindow){
            return;
        }
        long duration = player == null ? 0 :player
                .getDuration();
        long position = player == null ? 0 :
                player.getCurrentPosition();
        time.setText(stringForTime(duration));
        if(!dragging){
            timeCurrent.setText(stringForTime(position));
        }
        if(!dragging){
            progressBar.setProgress(progressBarValue(position));
        }
        long bufferedPosition = player == null ? 0
                : player.getBufferedPosition();
        progressBar.setSecondaryProgress(
                progressBarValue(bufferedPosition));
        removeCallbacks(updateProgressAction);
        int playbackState = player == null ? ExoPlayer.STATE_IDLE :
                player.getPlaybackState();
        if(playbackState != ExoPlayer.STATE_IDLE && playbackState
                != ExoPlayer.STATE_ENDED){
            long delayMs;
            if(player.getPlayWhenReady() && playbackState ==
                    ExoPlayer.STATE_READY){
                delayMs = 1000 - (position % 1000);
                if(delayMs < 200){
                    delayMs += 1000;
                }
            }else{
                delayMs = 1000;
            }
            postDelayed(updateProgressAction,
                    delayMs);
        }
    }

    private void setButtonEnabled(boolean enabled,View view){
        view.setEnabled(enabled);
        if(Util.SDK_INT >= 11){
            setViewAlphaV11(view,enabled ? 1f : 0.3f);
            view.setVisibility(VISIBLE);
        }else{
            view.setVisibility(enabled ? VISIBLE :
            INVISIBLE);
        }
    }

    @TargetApi(11)
    private void setViewAlphaV11(View view,float alphat){
        view.setAlpha(alphat);
    }


    private String stringForTime(long timeMs){
        if(timeMs == C.TIME_UNSET){
            timeMs = 0;
        }
        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60 ) % 60;
        long hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        return hours > 0 ? formatter.format("%d:%02d:%02d",
                hours,minutes,seconds).toString() :
                formatter.format("%02d:%02d",
                        minutes,seconds).toString();
    }

    private int progressBarValue(long position){
        long duration = player == null ? C.TIME_UNSET :
                player.getDuration();
        return duration == C.TIME_UNSET || duration
                == 0 ? 0 : (int)((position * PROGRESS_BAR_MAX)
        / duration);
    }

    private long positionValue(int progress){
        long duration = player == null ? C
                .TIME_UNSET : player.getDuration();
        return duration == C.TIME_UNSET ? 0 : ((duration * progress)
        / PROGRESS_BAR_MAX);
    }

    private void previouse(){
        Timeline currentTimeline = player.getCurrentTimeline();
        if(currentTimeline == null){
            return;
        }
        int currentWindowIndex = player.getCurrentWindowIndex();
        currentTimeline.getWindow(currentWindowIndex,
                currentWindow);
        if(currentWindowIndex > 0 && (player
        .getCurrentPosition() <= MAX_POSITION_FOR_SEEK_TO_PREVIOUS ||
                (currentWindow.isSeekable))){
            player.seekToDefaultPosition(currentWindowIndex - 1);
        }else{
            player.seekTo(0);
        }
    }


    private void next(){
        Timeline currentTimeline = player
                .getCurrentTimeline();
        if(currentTimeline ==  null){
            return;
        }
        int currentWindowIndex = player
                .getCurrentWindowIndex();
        if(currentWindowIndex < currentTimeline
                .getWindowCount() - 1){
            player.seekToDefaultPosition(
                    currentWindowIndex + 1);
        }else if(currentTimeline.getWindow(
                currentWindowIndex,currentWindow,false
        ).isDynamic){
            player.seekToDefaultPosition();
        }
    }


    private void rewind(){
        if(rewindMs <= 0){
            return;
        }
        player.seekTo(Math.max(player
        .getCurrentPosition() - rewindMs ,0));
    }


    private void fastForward(){
        if(fastForwardMs <= 0){
            return;
        }
        player.seekTo(Math.min(player
        .getCurrentPosition() + fastForwardMs,
                player.getDuration()));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        if(hideAtMs != C.TIME_UNSET){
            long delayMs = hideAtMs - SystemClock.uptimeMillis();
            if(delayMs <= 0){
                hide();
            }else{
                postDelayed(hideAction,delayMs);
            }
        }
        updateAll();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
        removeCallbacks(updateProgressAction);
        removeCallbacks(hideAction);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(player == null || event.getAction() != KeyEvent.ACTION_DOWN){
            return super.dispatchKeyEvent(event);
        }
        switch (event.getKeyCode()){
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                fastForward();
                break;
            case KeyEvent.KEYCODE_MEDIA_REWIND:
            case KeyEvent.KEYCODE_DPAD_LEFT:
                rewind();
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            player.setPlayWhenReady(!player.getPlayWhenReady());
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                player.setPlayWhenReady(true);
                break;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                player.setPlayWhenReady(false);
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                next();
                break;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                previouse();
                break;
            default:
                return false;

        }
        show();
        return true;
    }

    private final class ComponentListener implements
            ExoPlayer.EventListener,SeekBar.OnSeekBarChangeListener
    ,OnClickListener{
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            removeCallbacks(hideAction);
            dragging = true;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(b){
                timeCurrent.setText(stringForTime(positionValue(i)));
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            dragging = false;
            player.seekTo(positionValue(seekBar
            .getProgress()));
            hideAfterTimeout();
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            updatePlayPauseButton();
            updateProgress();
        }

        @Override
        public void onPositionDiscontinuity() {
            updateNavigation();
            updateProgress();
        }

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            updateNavigation();
            updateProgress();
        }




        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }


        @Override
        public void onClick(View view) {
            Timeline currentTimeLine = player
                    .getCurrentTimeline();
            if(nextButton == view){
                next();
            }else if(previousButton == view){
                previouse();
            }else if(fastForwardButton == view){
                fastForward();
            }else if(rewindButton == view &&
                    currentTimeLine != null){
                rewind();
            }else if(playButton == view){
                player.setPlayWhenReady(!player
                .getPlayWhenReady());
            }
            hideAfterTimeout();

        }
    }

}
