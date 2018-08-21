package udacity.louco.com.radio.ui.CastomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.google.android.exoplayer.ExoPlayer;

import udacity.louco.com.radio.R;

public class PlayerButton extends android.support.v7.widget.AppCompatImageButton {

    private Context context;
    private Animation animationLoad;
    private int value;
    public PlayerButton(Context context) {
        super(context);
        this.context = context;

        createAnimation();

    }

    private void createAnimation() {
        animationLoad = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        animationLoad.setDuration(2000);

        animationLoad.setRepeatMode(Animation.REVERSE);
        animationLoad.setRepeatCount(Animation.INFINITE);
    }

    public PlayerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PlayerButton,
                0, 0);

        try{
            value = a.getInt(R.styleable.PlayerButton_StatePlayer, 0);
        }finally {
            a.recycle();
        }

        createAnimation();
    }

    public PlayerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        createAnimation();
    }

    public void showPlayerState(int state){
        if(state == ExoPlayer.STATE_READY){
            showStateStop();
        }else if(state == ExoPlayer.STATE_IDLE){
            showStatePlay();
        }else{
            showStateLoad();
        }
    }

    public void showStatePlay() {
        this.setImageDrawable(context.getDrawable(R.drawable.ic_play));
        animationLoad.cancel();
    }

    public void showStateStop() {
        this.setImageDrawable(context.getDrawable(R.drawable.ic_pause));
        animationLoad.cancel();
    }

    public void showStateLoad() {
        this.setImageDrawable(context.getDrawable(R.drawable.ic_loop));

        animationLoad.reset();
        this.startAnimation(animationLoad);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        showPlayerState(value);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }
}
