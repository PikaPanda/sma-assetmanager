package fr.smartapps.lib.todo;

import android.animation.Animator;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.io.IOException;


/**
 *
 */
public class VideoActivity extends BaseActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControlListener, MediaPlayer.OnVideoSizeChangedListener {


    /*
    Attributes
     */
    protected ResizeSurfaceView mVideoSurface;
    protected MediaPlayer mMediaPlayer;
    protected VideoControllerView videoController;
    protected int mVideoWidth;
    protected int mVideoHeight;
    protected View mContentView;

    private String TAG = "VideoActivity";
    protected FrameLayout animateLayout;
    protected Bundle bundle;
    protected String urlVideo;
    protected String title;
    protected ClassStyleDescription classStyleDescription;

    /*
    Callbacks : constructor & destroy
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initContentViews();
        initDesign(getClass().getName());
        startAnimation();
    }

    @Override
    public void onBackPressed() {
        resetPlayer();
        closeAnimation();
    }

    @Override
    protected void initContentViews() {
        initBackNavBar();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            urlVideo = bundle.getString(InitConfig.INTENT_EXTRA_URL);
            title = bundle.getString(InitConfig.INTENT_EXTRA_TITLE);
        }
        switchTitleView = (SwitchTitleView) findViewById(R.id.title);
        switchTitleView.initData(title, assetManager);

        mVideoSurface = (ResizeSurfaceView) findViewById(R.id.videoSurface);
        mContentView = findViewById(R.id.video_container);
        SurfaceHolder videoHolder = mVideoSurface.getHolder();
        videoHolder.addCallback(this);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        videoController = new VideoControllerView(this, assetManager);

        if (urlVideo != null) {
            try {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                Log.e(TAG, "urlVideo : " + urlVideo);
                if (urlVideo.startsWith("file:///android_asset/")) {
                     urlVideo = urlVideo.replace("file:///android_asset/", "");
                }

                if (assetManager.getStorageType() == FilePath.EXTERNAL) {
                    mMediaPlayer.setDataSource(this, Uri.parse(urlVideo));
                    mMediaPlayer.setOnPreparedListener(this);
                }
                else {
                    AssetFileDescriptor afd = getAssets().openFd(urlVideo);
                    mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mMediaPlayer.setOnPreparedListener(this);
                }

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mVideoSurface.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    videoController.toggleContollerView();
                    return false;
                }
            });
        }

    }

    @Override
    protected void initDesign(String classPath) {
        initDesignNavBar();
        classStyleDescription = appStructure.getClassStyleDescription(classPath);

        if (classStyleDescription != null) {
            if (videoController != null) {
                if (classStyleDescription.getDescription(RString(R.string.video_color_background_player)) != null)
                    videoController.BACKGROUND_PLAYER = (String) classStyleDescription.getDescription(RString(R.string.video_color_background_player));
                if (classStyleDescription.getDescription(RString(R.string.video_color_progress_bar_finished)) != null)
                    videoController.COLOR_PROGRESS_BAR_FINISHED = (String) classStyleDescription.getDescription(RString(R.string.video_color_progress_bar_finished));
                if (classStyleDescription.getDescription(RString(R.string.video_color_progress_bar_unfinished)) != null)
                    videoController.COLOR_PROGRESS_BAR_UNFINISHED = (String) classStyleDescription.getDescription(RString(R.string.video_color_progress_bar_unfinished));
                if (classStyleDescription.getDescription(RString(R.string.video_color_progress_thumb)) != null)
                    videoController.COLOR_PROGRESS_THUMB = (String) classStyleDescription.getDescription(RString(R.string.video_color_progress_thumb));
                if (classStyleDescription.getDescription(RString(R.string.video_color_progress_time)) != null)
                    videoController.COLOR_PROGRESS_TIME = (String) classStyleDescription.getDescription(RString(R.string.video_color_progress_time));
                if (classStyleDescription.getDescription(RString(R.string.video_color_button_player)) != null)
                    videoController.COLOR_BUTTON_PLAY = (String) classStyleDescription.getDescription(RString(R.string.video_color_button_player));
            }
        }
    }

    private boolean closeAnimationProcessing = false;
    @Override
    protected void closeAnimation() {
        if(!closeAnimationProcessing) {
            closeAnimationProcessing = true;
            mVideoSurface.setVisibility(View.GONE);
            animateLayout = (FrameLayout) findViewById(R.id.animate_layout);
            if (animateLayout != null) {
                animateLayout.animate().alpha(0).setDuration(getResources().getInteger(R.integer.animation_base_time))
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                animateLayout.setVisibility(View.INVISIBLE);
                                finish();
                                overridePendingTransition(0, 0);
                            }
                        })
                        .start();
                closeNavBarAnimation();
            }
        }
    }

    @Override
    protected void startAnimation() {
        animateLayout = (FrameLayout) findViewById(R.id.animate_layout);
        animateLayout.setAlpha(0);
        animateLayout.animate().alpha(1).setDuration(getResources().getInteger(R.integer.animation_base_time)).start();
        startNavBarAnimation();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        mVideoHeight = mp.getVideoHeight();
        mVideoWidth = mp.getVideoWidth();
        if (mVideoHeight > 0 && mVideoWidth > 0)
            mVideoSurface.adjustSize(mContentView.getWidth(), mContentView.getHeight(), mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mVideoWidth > 0 && mVideoHeight > 0)
            mVideoSurface.adjustSize(getDeviceWidth(this), getDeviceHeight(this), mVideoSurface.getWidth(), mVideoSurface.getHeight());
    }

    private void resetPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }


    /*
    Surface Holder callbacks
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mMediaPlayer.setDisplay(holder);
        mMediaPlayer.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        resetPlayer();
    }


    // Media Player callbacks
    @Override
    public void onPrepared(MediaPlayer mp) {
        // setup video videoController view
        videoController.setMediaPlayerControlListener(this);
        mVideoSurface.setVisibility(View.VISIBLE);
        videoController.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        videoController.setGestureListener(this);
        mMediaPlayer.start();
    }

    /**
     * Implement VideoMediaController.MediaPlayerControl
     */
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(null != mMediaPlayer)
            return mMediaPlayer.getCurrentPosition();
        else
            return 0;
    }

    @Override
    public int getDuration() {
        if(null != mMediaPlayer)
            return mMediaPlayer.getDuration();
        else
            return 0;
    }

    @Override
    public boolean isPlaying() {
        if(null != mMediaPlayer)
            return mMediaPlayer.isPlaying();
        else
            return false;
    }

    @Override
    public void pause() {
        if(null != mMediaPlayer) {
            mMediaPlayer.pause();
        }

    }

    @Override
    public void seekTo(int i) {
        if(null != mMediaPlayer) {
            mMediaPlayer.seekTo(i);
        }
    }

    @Override
    public void start() {
        if(null != mMediaPlayer) {
            mMediaPlayer.start();
        }
    }

    @Override
    public boolean isFullScreen() {
        return getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ? true : false;
    }

    @Override
    public void toggleFullScreen() {
        if (isFullScreen()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public void onTap(boolean showing) {
        navBar = (NavBar) findViewById(R.id.toolbar);
        if (isFullScreen())
            if (showing)
                fadeOutNabBarAnimation();
            else
                fadeInNabBarAnimation();
    }
}
