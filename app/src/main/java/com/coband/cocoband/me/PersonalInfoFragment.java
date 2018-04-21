package com.coband.cocoband.me;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.coband.App;
import com.coband.cocoband.BaseFragment;
import com.coband.cocoband.event.me.MeItemClickEvent;
import com.coband.cocoband.event.me.UpdateInfoEvent;
import com.coband.cocoband.me.viewholder.PersonalInfoViewHolder;
import com.coband.cocoband.mvp.iview.PersonalInfoView;
import com.coband.cocoband.mvp.model.DataManager;
import com.coband.cocoband.mvp.model.IMCOCallback;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.bean.SignUpUserInfo;
import com.coband.cocoband.mvp.model.bean.message.FileBean;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.cocoband.mvp.presenter.PersonalInfoPresenter;
import com.coband.cocoband.tools.AppComponent;
import com.coband.cocoband.widget.adapter.BaseRecyclerAdapter;
import com.coband.cocoband.widget.widget.GuideProgressDialog;
import com.coband.common.utils.CocoUtils;
import com.coband.common.utils.Config;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.DialogUtils;
import com.coband.common.utils.Logger;
import com.coband.common.utils.NetWorkUtil;
import com.coband.common.utils.SaveDialogDataUtils;
import com.coband.common.utils.SaveLoginInfoUtils;
import com.coband.common.utils.TimeUtils;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.MultiItemAdapter;
import com.soundcloud.android.crop.Crop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.cache.LCIMProfileCache;
import de.hdodenhof.circleimageview.CircleImageView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


/**
 * Created by mai on 17-4-18.
 * modify by ivan on 3/20/18.
 */
@RuntimePermissions
public class PersonalInfoFragment extends BaseFragment implements PersonalInfoView {

    public final static String TAG = "PersonalInfoFragment";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @Inject
    PersonalInfoPresenter presenter;

    private MultiItemAdapter adapter;

    private Uri mPicUri;
    private CircleImageView civAvat;

    private RecyclerView mRecyclerView;

    private int mSelectPicType;
    private int picType = PIC_IS_NULL;

    private static final boolean IS_SINGLE = true;
    private static final int PIC_IS_COVER = 1;
    private static final int PIC_IS_AVATAR = 2;
    private static final int PIC_IS_NULL = 0;


    public static PersonalInfoFragment newInstance() {
        return new PersonalInfoFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_head;
    }

    @Override
    protected void setupView() {
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        mRecyclerView = new RecyclerView(App.getContext());
        mRecyclerView.setBackgroundColor(Color.WHITE);
        ((CoordinatorLayout) mRootView).addView(mRecyclerView, layoutParams);
        setupToolbar(getString(R.string.user_profile), toolbar);
        AppComponent.getInstance().inject(this);
        presenter.attachView(this);
        setPresenter(presenter);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void init() {
        adapter = new MultiItemAdapter(PersonalInfoViewHolder.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        List<String> strings = Arrays.asList(getResources().getStringArray(R.array.personalInformation_fragment));
        adapter.setDataList(strings);
        mRecyclerView.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessgeEventOnMainThread(MeItemClickEvent event) {
        TextView tv_title = (TextView) event.view.findViewById(R.id.tv_title);
        final TextView tv_status = (TextView) event.view.findViewById(R.id.text_status);
        String title = tv_title.getText().toString();

        if (title.equals(getString(R.string.modify_avatar))) {
            civAvat = (CircleImageView) event.view.findViewById(R.id.civ_avat);
            picType = PIC_IS_AVATAR;
            showSelectPicDialog(R.string.modify_avatar);
        } else if (title.equals(getString(R.string.sufaceImage))) {
            picType = PIC_IS_COVER;
            showSelectPicDialog(R.string.select_image);
        } else if (title.equals(getString(R.string.nickname))) {
            DialogUtils.setNickName(mActivity, new IMCOCallback<String>() {
                @Override
                protected void done0(String nickname) {
                    if (nickname.isEmpty()) {
                        showShortToast(R.string.nickname_empty);
                    } else {
                        presenter.updateAccountNickname(nickname);
                    }

                }
            });
        } else if (title.equals(getString(R.string.gender))) {
            String str = tv_status.getText().toString();
            DialogUtils.showSingleChoiceDialog(mActivity, str, new IMCOCallback<String>() {
                @Override
                protected void done0(String obj) {
                    if (obj.equals(getString(R.string.male))) {
                        presenter.updateAccountGender(Config.MALE);
                    } else {
                        presenter.updateAccountGender(Config.FEMALE);
                    }

                }
            });
        } else if (title.equals(getString(R.string.birthday))) {
            String[] array = DateUtils.getDateArray(tv_status.getText().toString());
            DialogUtils.showDatePickerDialog(array[0], array[1], array[2],
                    mActivity, new IMCOCallback<String>() {
                        @Override
                        protected void done0(String birthday) {
                            presenter.updateAccountBirthday(birthday);
                        }
                    });
        } else if (title.equals(getString(R.string.height))) {
            String heightStr = tv_status.getText().toString();
            DialogUtils.showHeightDifferentDialog(DataManager.getInstance().getUnitSystem() == Config.METRIC,
                    heightStr, IS_SINGLE,
                    mActivity, new IMCOCallback<String>() {
                        @Override
                        protected void done0(String obj) {
                            double height = SaveLoginInfoUtils.getHeight(obj);
                            presenter.updateAccountHeight(height);
                        }
                    });
        } else if (title.equals(getString(R.string.weight))) {
            String weightStr = tv_status.getText().toString();
            DialogUtils.showWeightDifferentDialog(DataManager.getInstance().getUnitSystem() == Config.METRIC,
                    weightStr, IS_SINGLE,
                    mActivity, new IMCOCallback<String>() {
                        @Override
                        protected void done0(String obj) {
                            double weight = SaveLoginInfoUtils.getWeight(obj);
                            presenter.updateAccountWeight(weight);
                        }
                    });
        }
    }


    private void showSelectPicDialog(@StringRes int sr) {
        mSelectPicType = sr;
        DialogUtils.showImageSelector(mActivity, new IMCOCallback<Integer>() {
            @Override
            protected void done0(Integer obj) {
                if (obj == DialogUtils.SELECT_PIC) {
                    PersonalInfoFragmentPermissionsDispatcher.selectPicWithCheck(PersonalInfoFragment.this, obj);
                } else if (obj == DialogUtils.TAKE_PIC) {
                    PersonalInfoFragmentPermissionsDispatcher.takePicWithCheck(PersonalInfoFragment.this);
                }
            }
        }, sr);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void selectPic(int picType) {
        DialogUtils.selectPic(PersonalInfoFragment.this, picType);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void takePic() {
        try {
            mPicUri = DialogUtils.takePic(PersonalInfoFragment.this, DialogUtils.TAKE_PIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showRationale(PermissionRequest request) {
        showRationaleDialog(R.string.request_camera_storage_permission, request);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied() {
        showShortToast(R.string.not_permission);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == DialogUtils.SELECT_PIC) {
                if (picType == PIC_IS_COVER) {
                    beginCropRect(data.getData());
                } else {
                    beginCrop(data.getData());
                }
            } else if (requestCode == DialogUtils.TAKE_PIC) {
                if (picType == PIC_IS_COVER) {
                    beginCropRect(mPicUri);
                } else {
                    beginCrop(mPicUri);
                }
            } else if (requestCode == Crop.REQUEST_CROP) {
                Uri output = Crop.getOutput(data);

                if (null != civAvat) {
                    if (mSelectPicType == R.string.modify_avatar) {
                        presenter.saveImg(output, 1);

                    }
                }

                if (mSelectPicType == R.string.select_image) {
                    presenter.saveImg(output, 2);
                }
            }
        } else {
            Logger.d(this, "result code >>>>>> " + resultCode);
        }
    }

    private void beginCrop(Uri source) {
        Logger.d(this, "take pic uri >>>>>>>> " + source);
        Uri destination = Uri.fromFile(new File(mActivity.getCacheDir(), "cropped"));
        Crop.of(source, destination).withAspect(320, 480).start(App.getContext(), this);
    }

    private void beginCropRect(Uri source) {
        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);

        Uri destination = Uri.fromFile(new File(mActivity.getCacheDir(), "cropped"));
        Crop.of(source, destination).withAspect(128, 128).start(App.getContext(), this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showLocalHeadPic(String path) {
        Glide.with(App.getContext()).load(path)
                .signature(new StringSignature(new Date().toString()))
                .error(R.drawable.profile_men)
                .fitCenter().centerCrop().into(civAvat);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PersonalInfoFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void showNetworkUnavailable() {
        showShortToast(R.string.network_error);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void showUpdateAccountSuccess(int type) {
        dismissLoadingDialog();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showUpdateAccountFailed(int type, String errorMsg) {
        dismissLoadingDialog();
        switch (type) {
            case HandleEvent.UPDATE_NICKNAME_FAILED:
                showLongToast(getString(R.string.update_nickname_failed) + " cause by " + errorMsg);
                break;
            case HandleEvent.UPDATE_GENDER_FAILED:
                showLongToast(getString(R.string.update_gender_failed) + " cause by " + errorMsg);
                break;
            case HandleEvent.UPDATE_HEIGHT_FAILED:
                showLongToast(getString(R.string.update_height_failed) + " cause by " + errorMsg);
                break;
            case HandleEvent.UPDATE_WEIGHT_FAILED:
                showLongToast(getString(R.string.update_weight_failed) + " cause by " + errorMsg);
                break;
            case HandleEvent.UPDATE_BIRTHDAY_FAILED:
                showLongToast(getString(R.string.update_birthday_failed) + " cause by " + errorMsg);
                break;
            case HandleEvent.UPLOAD_AVATAR_FAILED:
                showLongToast(getString(R.string.upload_avatar_failed) + " cause by " + errorMsg);
                break;
            case HandleEvent.UPLOAD_BACKGROUND_FAILED:
                showLongToast(getString(R.string.upload_background_failed) + " cause by " + errorMsg);
                break;
            default:
                break;
        }
    }

    @Override
    public void showUpdateAccountFailed(int type, int code) {
        dismissLoadingDialog();
        switch (type) {
            case HandleEvent.UPDATE_NICKNAME_FAILED:
                showShortToast(R.string.update_nickname_failed);
                break;
            case HandleEvent.UPDATE_GENDER_FAILED:
                showShortToast(R.string.update_gender_failed);
                break;
            case HandleEvent.UPDATE_HEIGHT_FAILED:
                showShortToast(R.string.update_height_failed);
                break;
            case HandleEvent.UPDATE_WEIGHT_FAILED:
                showShortToast(R.string.update_weight_failed);
                break;
            case HandleEvent.UPDATE_BIRTHDAY_FAILED:
                showShortToast(R.string.update_birthday_failed);
                break;
            case HandleEvent.UPLOAD_AVATAR_FAILED:
                showShortToast(R.string.upload_avatar_failed);
                break;
            case HandleEvent.UPLOAD_BACKGROUND_FAILED:
                showShortToast(R.string.upload_background_failed);
                break;
            default:
                break;
        }
    }
}
