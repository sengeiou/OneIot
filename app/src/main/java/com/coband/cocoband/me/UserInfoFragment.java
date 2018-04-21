package com.coband.cocoband.me;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coband.App;
import com.coband.cocoband.BaseResumeFragment;
import com.coband.cocoband.event.me.AddFriendExceptionEvent;
import com.coband.cocoband.mvp.iview.IUserInfoView;
import com.coband.cocoband.mvp.presenter.UserInfoPresenter;
import com.coband.cocoband.tools.AppComponent;
import com.coband.cocoband.widget.widget.GuideProgressDialog;
import com.coband.cocoband.widget.widget.UserAvatarDialog;
import com.coband.common.utils.BitmapUtils;
import com.coband.common.utils.CocoUtils;
import com.coband.common.utils.Constants;
import com.coband.common.utils.DialogUtils;
import com.coband.common.utils.NetWorkUtil;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * Created by ivan on 12/10/15.
 */

public class UserInfoFragment extends BaseResumeFragment implements View.OnClickListener,
        IUserInfoView, View.OnTouchListener {

    public final static String TAG = "UserInfoFragment";
    public final static String OBJID = "object_id";
    public final static String POSITION = "position";
    public final static String IS_FRIEND = "is_friend";
    public final static String USER = "user";

    public final static String FROM_CHAT = "from_chat";

    private float downX = 0;
    private float downY = 0;
    private float moveX = 0;
    private float moveY = 0;

    @BindView(R.id.iv_surface)
    ImageView ivSurface;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_love)
    TextView tvLove;
    @BindView(R.id.civ_avat)
    CircleImageView civAvat;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.bt_edit)
    Button btEdit;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_day_best_steps)
    TextView tvDayBestSteps;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.rl_sport_info)
    RelativeLayout rlSportInfo;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.ll_gallery)
    LinearLayout llGallery;
    @BindView(R.id.rl_medal)
    RelativeLayout rlMedal;
    @BindView(R.id.iv_friend)
    ImageView ivFriend;
    @BindView(R.id.iv_add_friend)
    ImageView ivAddFriend;
    Unbinder unbinder;
    @BindView(R.id.iv_arrow_icon)
    ImageView ivArrowIcon;
    @BindView(R.id.medal_hsv)
    HorizontalScrollView medalHsv;
    @BindView(R.id.svBottom)
    ScrollView svBottom;
    @BindView(R.id.fl_bt)
    FrameLayout flButton;
    private GuideProgressDialog mProgressDialog;
    public List<String> mAchievementList;
    @Inject
    UserInfoPresenter mUserInfoPresenter;

    @OnClick(R.id.civ_avat)
    void showAvaterDialog() {
        Rect rect = BitmapUtils.getBitmapRectFromImageView(civAvat);
        UserAvatarDialog dialog = UserAvatarDialog.newInstance(mAvatar, rect);
        dialog.setCancelable(true);
        dialog.show(mActivity.getSupportFragmentManager(), "");
    }

    private String mAvatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    protected void setupView() {
        AppComponent.getInstance().inject(this);
        initToolBar();
        rlMedal.setOnClickListener(this);
        rlSportInfo.setOnClickListener(this);
        btEdit.setOnClickListener(this);
        ivAddFriend.setOnClickListener(this);
        ivFriend.setOnClickListener(this);
        medalHsv.setOnTouchListener(this);
        mUserInfoPresenter.attachView(this);
        svBottom.setVisibility(View.INVISIBLE);
        flButton.setVisibility(View.INVISIBLE);
        tvUsername.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_band_user;
    }

    @Override
    protected void init() {
        svBottom.setVisibility(View.VISIBLE);
        flButton.setVisibility(View.VISIBLE);
        tvUsername.setVisibility(View.VISIBLE);
        mUserInfoPresenter.init();

    }

    private void initToolBar() {
        if (toolbar == null) return;
        setupToolbar(" ", toolbar, R.drawable.ic_arrow_back_white);
        // 如果有参数则是显示其他用户的信息，没有参数则显示当前用户的信息
//        if (getArguments() != null && (getArguments().containsKey(OBJID) || getArguments().containsKey(USER))) {
//            if (getArguments().containsKey(FROM_CHAT)) return;
//            toolbar.inflateMenu(R.menu.band_me_menu);
//            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    switch (item.getItemId()) {
//                        case R.id.chat:
//                            if (!CocoUtils.singleClick(toolbar)) return false;
//                            if (mObjId != null) {
//                                if (mUser == null) {
//                                    com.coband.watchassistant.User user = DBHelper.getInstance().getUserByObjectId(mObjId);
//                                    mUser = new User();
//                                    mUser.setUsername(user.getUsername());
//                                    mUser.setNickName(user.getNick());
//                                    mUser.setAvatar(user.getAvatar());
//                                } else if (getArguments() != null && getArguments().containsKey(USER)) {
//                                    // 这是陌生人聊天，存储陌生人的信息
//                                    mUserInfoPresenter.saveUser(mUser);
//                                }
//
//                                String userName = mUser.getNickName() != null ? mUser.getNickName() : mUser.getUsername();
//                                if (!LCIMProfileCache.getInstance().hasCachedUser(mObjId))
//                                    LCIMProfileCache.getInstance().cacheUser(new LCChatKitUser(mObjId, userName, mAvatar));
//                                Bundle bundle = new Bundle();
//                                bundle.putString(LCIMConstants.PEER_ID, mObjId);
//                                addFragment(new IMCOConversationFragment(), IMCOConversationFragment.TAG, true, bundle);
//                            }
//                            break;
//                    }
//                    return false;
//                }
//            });
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit:
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.KEY_RESET_INFO, true);
                addFragment(PersonalInfoFragment.newInstance(), PersonalInfoFragment.TAG);
                break;
            case R.id.iv_add_friend:
                if (!NetWorkUtil.isNetConnected()) {
                    showShortToast(R.string.network_error);
                    return;
                }

                // If it is from the friend request page jump over, notify the friend request page refresh
                if (getArguments() != null && getArguments().containsKey(POSITION) && null != getTargetFragment()) {
                    mProgressDialog = DialogUtils.showGuideProgressDialog(mActivity, getString(R.string.processing));
                    int position = getArguments().getInt(POSITION);
                    Intent intent = new Intent();
                    intent.putExtra(IS_FRIEND, true);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), position, intent);
                    Utils.toast(R.string.message_when_agree_request);
                } else {
                    Utils.toast(R.string.contact_alreadyCreateAddRequest);
                }

                break;
            case R.id.iv_friend:
                if (!NetWorkUtil.isNetConnected()) {
                    showShortToast(R.string.network_error);
                    return;
                }

//                DialogUtils.normalDialog(mActivity, R.string.whether_delete_friends, 0, new IMCOCallback<Boolean>() {
//                    @Override
//                    protected void done0(Boolean obj) {
//                        if (obj) {
//                            mUserInfoPresenter.deleteFirends(mObjId);
//                            mProgressDialog = DialogUtils.showGuideProgressDialog(mActivity, getString(R.string.processing));
//                        }
//                    }
//                });
                break;
            case R.id.rl_sport_info:
                Bundle sportInfoBundle = new Bundle();
//                sportInfoBundle.putString(PersonalAchievementFragment.OBJECT_ID, mObjId);
//                addFragment(new PersonalAchievementFragment(), PersonalInfoFragment.TAG, true, sportInfoBundle);
                break;
            case R.id.rl_medal:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUserInfoPresenter.detachView();
    }

    @Override
    public void showNickName(String NickName) {
        tvUsername.setText(NickName);
    }

    @Override
    public void isFriends(boolean isFriends) {
        // Network disconnect will re-connect friends will update the list, this will be affected, if is master not update the interface
//        if (mObjId == null) return;
//        if (mObjId.equals(DBHelper.getInstance().getUser().getObjectId())) return;
        rlMedal.setVisibility(isFriends ? View.VISIBLE : View.INVISIBLE);
        rlSportInfo.setVisibility(isFriends ? View.VISIBLE : View.INVISIBLE);
        ivAddFriend.setVisibility(isFriends ? View.INVISIBLE : View.VISIBLE);
        ivFriend.setVisibility(isFriends ? View.VISIBLE : View.INVISIBLE);

        // If it is from the friend request page jump over, notify the friend request page refresh
        if (getArguments() != null && getArguments().containsKey(POSITION) && null != getTargetFragment()) {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

    @Override
    public void showAvatar(String avatar) {
        if (avatar != null)
            this.mAvatar = avatar;
        Glide.with(this.getContext())
                .load(avatar)
                .error(R.drawable.profile_men)
                .fitCenter().centerCrop()
                .into(civAvat);
    }

    @Override
    public void showSurfaceImage(String imagePath) {
        if (imagePath != null)
            Glide.with(App.getContext())
                    .load(imagePath).fitCenter().centerCrop()
                    .into(ivSurface);
    }

    @Override
    public void showAchievementList(List<String> split) {
        if (null != split)
            mAchievementList = split;
        Observable.fromArray(split.toArray(new String[0]))
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Exception {
                        return CocoUtils.getAchievementSmallIcon(CocoUtils.getMedalTitleLocalLangFromTitle(s));
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        ImageView imageView = new ImageView(App.getContext());
                        imageView.setImageResource(integer);
                        llGallery.addView(imageView);
                    }
                });
    }

    @Override
    public void showDayHighestSteps(int steps) {
        tvDayBestSteps.setText(String.format(getString(R.string.best_single_day), steps));
    }

    @Override
    public void deleteFriends(boolean success) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();

            // If it is from the friend request page jump over, notify the friend request page refresh
            if (getArguments() != null && getArguments().containsKey(POSITION) && null != getTargetFragment()) {
                int position = getArguments().getInt(POSITION);
                Intent intent = new Intent();
                intent.putExtra(IS_FRIEND, false);
                getTargetFragment().onActivityResult(getTargetRequestCode(), position, intent);
            }

            getActivity().onBackPressed();
        }
    }

    protected void onFragmentResume() {
//        if (null == mObjId) {
        List<String> userList = mUserInfoPresenter.getUserImgAndAvatar();
        String avatarNew = userList.get(0);
        String surfaceImgNew = userList.get(1);
        String nickNew = userList.get(2);
        if (null != avatarNew && null != civAvat) {
            Glide.with(this.getContext())
                    .load(avatarNew)
                    .error(R.drawable.profile_men)
                    .fitCenter().centerCrop()
                    .into(civAvat);
        }

        if (null != surfaceImgNew && null != ivSurface) {
            Glide.with(App.getContext())
                    .load(surfaceImgNew).fitCenter().centerCrop()
                    .into(ivSurface);
        }
        if (null != tvUsername) {
            tvUsername.setText(nickNew == null ? "CoBand" : nickNew);
        }
//        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                moveX = 0;
                moveY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                moveX += Math.abs(event.getX() - downX); //X轴距离
                moveY += Math.abs(event.getY() - downY); //y轴距离
                break;
            case MotionEvent.ACTION_UP:
                if (moveX > 20 || moveY > 20) {
                    moveX = 0;
                    moveY = 0;
                    return true;
                }
                rlMedal.performClick();
                moveX = 0;
                moveY = 0;
                break;
            default:
                return false;
        }
        return false;
    }

    // Monitor network data
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNoCacheFriendInfo(AddFriendExceptionEvent event) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        showShortToast(R.string.unkonw_error_tips);
        showShortToast(R.string.search_retry);
    }
}
