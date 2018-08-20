package udacity.louco.com.radio.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer.ExoPlayer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.louco.com.radio.R;
import udacity.louco.com.radio.mvp.model.object.Radio;
import udacity.louco.com.radio.mvp.presenter.RadioListPresenter;
import udacity.louco.com.radio.mvp.view.RadioListView;
import udacity.louco.com.radio.ui.adapter.RvAdapterRadioList;

public class RadioListActivity extends MvpAppCompatActivity implements RadioListView {

    @InjectPresenter
    RadioListPresenter presenter;

    @BindView(R.id.rv_radio_list)
    RecyclerView radioList;

    @BindView(R.id.bottom_sheet)
    ConstraintLayout layout;

    @BindView(R.id.cl_behavior_hideable)
    ConstraintLayout constraintLayout;

    @BindView(R.id.tv_name_radio_player)
    TextView nameRadio;

    @BindView(R.id.iv_logo_radio_behavior)
    ImageView logoRadio;

    @BindView(R.id.ib_like_behavior)
    ImageButton likeButton;

    @BindView(R.id.tv_name_radio_player_hide)
    TextView nameRadioHide;

    @BindView(R.id.ib_player_hide)
    ImageButton playHide;

    @BindView(R.id.ib_player_hide_behavior)
    ImageButton playHideBehavior;

    @BindView(R.id.fab_like)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.bnv)
    BottomNavigationView bottomNavigationView;

    private Animation animationLoad;
    private RvAdapterRadioList rvAdapterRadioList;
    private BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_list);
        ButterKnife.bind(this);

        animationLoad = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


        animationLoad.setDuration(2000);

        animationLoad.setRepeatMode(Animation.REVERSE);
        animationLoad.setRepeatCount(Animation.INFINITE);

        behavior = BottomSheetBehavior.from(layout);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    floatingActionButton.animate().scaleX(0).scaleY(0).setDuration(300).start();
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    constraintLayout.animate().scaleX(1).setDuration(200).start();
                    floatingActionButton.animate().scaleX(1).scaleY(1).setDuration(200).start();
                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    constraintLayout.animate().scaleX(0).setDuration(300).start();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        rvAdapterRadioList = new RvAdapterRadioList(radio -> {
            presenter.onClickRadio(radio);
        });

        radioList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        radioList.addItemDecoration(new DividerItemDecoration(radioList.getContext(), DividerItemDecoration.VERTICAL));
        radioList.setAdapter(rvAdapterRadioList);

        if (savedInstanceState == null) {
            presenter.startService(this);
        }

        playHide.setOnClickListener(v -> clickPlayer());
        playHideBehavior.setOnClickListener(v -> clickPlayer());

        floatingActionButton.setOnClickListener(v -> presenter.switchFavorite());
        likeButton.setOnClickListener(v -> presenter.switchFavorite());

        if(savedInstanceState == null){
            presenter.getInstanceDatabase(this);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            presenter.switchMenu(item.getItemId());
            item.setChecked(true);
            return true;});
    }

    private void clickPlayer() {
        presenter.clickPlayPlayer();
    }

    @Override
    public void showsRadio(List<Radio> radioList) {
        rvAdapterRadioList.addListRadio(radioList);
    }

    @Override
    public void addRadio(Radio radio) {
        rvAdapterRadioList.addRadio(radio);
    }

    @Override
    public void playRadio(Radio radio) {

    }

    @Override
    public void showInfoRadio(Radio radio) {
        Glide.with(this)
                .load(radio.getUrl_image())
                .into(logoRadio);

        nameRadio.setText(radio.getName());
        nameRadioHide.setText(radio.getName());

        showFavoriteState(radio);
    }

    @Override
    public void showFavoriteState(Radio radio) {
        Drawable drawable;
        if (radio.isLike()) {
            drawable = this.getDrawable(R.drawable.ic_favorite);
        } else {
            drawable = this.getDrawable(R.drawable.ic_favorite_border);
        }

        floatingActionButton.setImageDrawable(drawable);
        likeButton.setImageDrawable(drawable);
    }

    @Override
    public void showPlayerState(Integer state) {
        if(state == ExoPlayer.STATE_READY){
            showStateStop();
        }else if(state == ExoPlayer.STATE_IDLE){
            showStatePlay();
        }else{
            showStateLoad();
        }
    }

    public void showStatePlay() {
        playHideBehavior.setImageDrawable(this.getDrawable(R.drawable.ic_play));
        playHide.setImageDrawable(this.getDrawable(R.drawable.ic_play));
        animationLoad.cancel();
    }

    public void showStateStop() {
        playHideBehavior.setImageDrawable(this.getDrawable(R.drawable.ic_pause));
        playHide.setImageDrawable(this.getDrawable(R.drawable.ic_pause));
        animationLoad.cancel();
    }

    public void showStateLoad() {
        playHideBehavior.setImageDrawable(this.getDrawable(R.drawable.ic_loop));
        playHide.setImageDrawable(this.getDrawable(R.drawable.ic_loop));

        animationLoad.reset();
        playHide.startAnimation(animationLoad);

        animationLoad.reset();
        playHideBehavior.startAnimation(animationLoad);
    }

}
