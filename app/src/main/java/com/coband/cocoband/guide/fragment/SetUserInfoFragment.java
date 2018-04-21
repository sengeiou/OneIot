package com.coband.cocoband.guide.fragment;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.coband.App;
import com.coband.cocoband.BaseFragment;
import com.coband.cocoband.main.BandActivity;
import com.coband.cocoband.mvp.iview.SetUserInfoView;
import com.coband.cocoband.mvp.model.IMCOCallback;
import com.coband.cocoband.mvp.model.bean.HandleEvent;
import com.coband.cocoband.mvp.model.entity.request.UpdateAccountInfo;
import com.coband.cocoband.mvp.presenter.SetUserInfoPresenter;
import com.coband.cocoband.tools.AppComponent;
import com.coband.cocoband.widget.widget.GuideProgressDialog;
import com.coband.common.utils.Config;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.DialogUtils;
import com.coband.common.utils.SaveLoginInfoUtils;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetUserInfoFragment extends BaseFragment implements View.OnClickListener,
        View.OnFocusChangeListener, SetUserInfoView {

    public static final String TAG = "SetUserInfoFragment";
    public static final int INCH = 1;
    public static final int METRIC = 0;

    @BindView(R.id.why_ask_icon)
    ImageView whyAskIcon;
    @BindView(R.id.nick_name_edit)
    EditText nickNameEdit;
    @BindView(R.id.sex_edit)
    EditText sexEdit;
    @BindView(R.id.birth_edit)
    EditText birthEdit;
    @BindView(R.id.height_edit)
    EditText heightEdit;
    @BindView(R.id.weight_edit)
    EditText weightEdit;
    @BindView(R.id.user_profile_save_btn)
    Button userProfileSaveBtn;

    @Inject
    SetUserInfoPresenter presenter;
    private int unit;

    private static final boolean IS_SINGLE = false;

    @Override
    protected int getLayout() {
        return R.layout.fragment_guide_set_user_info;
    }

    @Override
    protected void init() {
        nickNameEdit.setInputType(InputType.TYPE_NULL);
        nickNameEdit.setOnFocusChangeListener(this);
        nickNameEdit.setOnClickListener(this);
        nickNameEdit.setText("CoBand");

        sexEdit.setInputType(InputType.TYPE_NULL);
        sexEdit.setOnFocusChangeListener(this);
        sexEdit.setOnClickListener(this);
        sexEdit.setText(getString(R.string.male));

        birthEdit.setInputType(InputType.TYPE_NULL);
        birthEdit.setOnFocusChangeListener(this);
        birthEdit.setOnClickListener(this);
        birthEdit.setText("1990-08-01");

        heightEdit.setInputType(InputType.TYPE_NULL);
        heightEdit.setOnFocusChangeListener(this);
        heightEdit.setOnClickListener(this);
        heightEdit.setText("170.0" + getString(R.string.cm));

        weightEdit.setInputType(InputType.TYPE_NULL);
        weightEdit.setOnFocusChangeListener(this);
        weightEdit.setOnClickListener(this);
        weightEdit.setText("60.0" + getString(R.string.kg));

        userProfileSaveBtn.setOnClickListener(this);
    }


    @Override
    protected void setupView() {
        AppComponent.getInstance().inject(this);
        setPresenter(presenter);
        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onClick(View v) {
        clickHandle(v);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            clickHandle(v);
        }
    }

    private void clickHandle(View v) {
        switch (v.getId()) {
            case R.id.why_ask_icon:
                Utils.showDialog(getActivity(), getString(R.string.why_ask_info),
                        getString(R.string.why_personal_info));
                break;
            case R.id.nick_name_edit:
                DialogUtils.setNickName(getActivity(), new IMCOCallback<String>() {
                    @Override
                    protected void done0(String obj) {
                        nickNameEdit.setText(obj);
                    }
                });
                break;
            case R.id.sex_edit:
                String str = sexEdit.getText().toString();
                DialogUtils.showSingleChoiceDialog(getActivity(), str, new IMCOCallback<String>() {
                    @Override
                    protected void done0(String obj) {
                        sexEdit.setText(obj);
                    }
                });
                break;
            case R.id.birth_edit:
                String[] array = DateUtils.getDateArray(birthEdit.getText().toString());
                DialogUtils.showDatePickerDialog(array[0], array[1], array[2],
                        getActivity(), new IMCOCallback<String>() {
                            @Override
                            protected void done0(String obj) {
                                birthEdit.setText(obj);
                            }
                        });
                break;
            case R.id.height_edit:
                String heightStr = heightEdit.getText().toString();
                showHeightDialog(SetUserInfoPresenter.unitTemp == 0, heightStr, getActivity(),
                        new IMCOCallback<String>() {
                            @Override
                            protected void done0(String obj) {
                                heightEdit.setText(obj);
                                String weight = weightEdit.getText().toString();
                                if (weight.contains(App.getContext().getString(R.string.kg))) {
                                    if (SetUserInfoPresenter.unitTemp == 1) {
                                        // metric to inch
                                        unit = INCH;

                                        presenter.showWeight(weight);
                                        presenter.updateUnit(INCH);
                                    }
                                } else {
                                    if (SetUserInfoPresenter.unitTemp == 0) {
                                        // inch to metric
                                        unit = METRIC;

                                        presenter.showWeight(weight);
                                        presenter.updateUnit(METRIC);
                                    }
                                }
                            }
                        });
                break;
            case R.id.weight_edit:
                String weightStr = weightEdit.getText().toString();
                showWeightDialog(SetUserInfoPresenter.unitTemp == 0, weightStr, getActivity(),
                        new IMCOCallback<String>() {
                            @Override
                            protected void done0(String obj) {
                                weightEdit.setText(obj);
//                        obj.contains(App.getContext().getString(R.string.kg));
                                String height = heightEdit.getText().toString();
                                if (height.contains(App.getContext().getString(R.string.cm))) {
                                    if (SetUserInfoPresenter.unitTemp == 1) {
                                        unit = INCH;
                                        presenter.showHeight(height);
                                        presenter.updateUnit(INCH);

                                    }
                                } else {
                                    if (SetUserInfoPresenter.unitTemp == 0) {
                                        unit = METRIC;
                                        presenter.showHeight(height);
                                        presenter.updateUnit(METRIC);

                                    }
                                }
                            }
                        });

                break;
            case R.id.user_profile_save_btn:
                if (nickNameEdit.getText().toString().isEmpty()) {
                    nickNameEdit.setError(getString(R.string.input_nickname_tip));
                    Observable.timer(2500, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    if (null != nickNameEdit) {
                                        nickNameEdit.setError(null);
                                    }
                                }
                            });
                    return;
                }


                showLoadingDialog();

                UpdateAccountInfo info = new UpdateAccountInfo();
                /****** fuck change 垃圾代码 ******/
                double heightValue = presenter.getHeightInGuide(getETText(heightEdit));
                if (unit == Config.INCH) {
                    int metricHeightValue = (int) Utils.changeFtToCm((float) heightValue);
                    info.setHeight(metricHeightValue);
                } else {
                    info.setHeight((int) heightValue);
                }
                /****** end by ivan ******/

                /************** modify by ivan ********/
                double weightValue = SaveLoginInfoUtils.getWeight(getETText(weightEdit));
                if (unit == Config.INCH) {
                    double metricWeightValue = Utils.changeLbToKg((float) heightValue);
                    info.setWeight(metricWeightValue);
                } else {
                    info.setWeight(weightValue);
                }
                /*************** end ******************/

                info.setNickName(getETText(nickNameEdit));
                info.setGender(getETSex(sexEdit) == 0 ? Config.MALE : Config.FEMALE);
                info.setBirthday(getETText(birthEdit));
                info.setUnitSystem(unit == Config.METRIC ? Config.METRIC_STRING : Config.BRITISH_STRING);
                presenter.updateInfo(info);
            default:
                break;
        }
    }

    private String getETText(EditText editText) {
        return editText.getText().toString();
    }

    private int getETSex(EditText editText) {
        if (getETText(editText).equals(getString(R.string.male))) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void setWeight(String weight) {
        presenter.showWeight(weight);

    }

    @Override
    public void showWeightToEditText(float finalNum) {
        if (SetUserInfoPresenter.unitTemp == 1) {
            weightEdit.setText(finalNum + App.getContext().getString(R.string.lb));
        } else {
            weightEdit.setText(finalNum + App.getContext().getString(R.string.kg));
        }
    }

    @Override
    public void setHeight(String height) {
        presenter.showHeight(height);
    }

    @Override
    public void showHeightInchToEditText(List<Integer> dataList) {
        heightEdit.setText(dataList.get(0) + App.getContext().getString(R.string.foot) +
                dataList.get(1) + App.getContext().getString(R.string.inch_unit));
    }

    @Override
    public void showHeightMetricToEditText(float inchFinal) {
        heightEdit.setText(inchFinal + App.getContext().getString(R.string.cm));
    }

    @Override
    public void jumpToHome() {
        getActivity().finish();
        Intent intent = new Intent(getActivity(), BandActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isFirstReg", true);
        startActivity(intent);
    }

    @Override
    public void showPostAccountInfoSuccess() {
        dismissLoadingDialog();

        getActivity().finish();
        Intent intent = new Intent(getActivity(), BandActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isFirstReg", true);
        startActivity(intent);
    }

    @Override
    public void showPostAccountInfoFailed(int code) {
        switch (code) {
            default:
                showShortToast("error code >>>>>>>>>> " + code);
        }
    }

    @Override
    public void showPostAccountInfoFailed(String errorMsg) {
        showShortToast("post account info error ---> " + errorMsg);
    }

    @Override
    public void showCustomToast(String message) {
        showShortToast(message);
    }

    public static void showHeightDialog(boolean isMetric, String str, Activity activity,
                                        final IMCOCallback<String> callback) {

        String[] unitHeight = new String[]{activity.getString(R.string.cm),
                activity.getString(R.string.foot) + "、" + activity.getString(R.string.inch_unit)};

        DialogUtils.showPickerDifferentDialog(unitHeight, R.layout.height_picker_dialog,
                R.id.height_picker_dialog_ll, 300, 20, 9,
                1, 9, 0, 11, 0,
                activity.getString(R.string.cm), activity.getString(R.string.foot),
                activity, callback, isMetric, str, IS_SINGLE);
    }

    public static void showWeightDialog(boolean isMetric, String str, Activity activity,
                                        final IMCOCallback<String> callback) {
        String[] unitWeight = new String[]{activity.getString(R.string.kg),
                activity.getString(R.string.lb)};

        DialogUtils.showPickerDifferentDialog(unitWeight, R.layout.height_picker_dialog,
                R.id.height_picker_dialog_ll, 500, 2, 1100,
                4, 9, 0, 9, 0,
                activity.getString(R.string.kg), activity.getString(R.string.lb),
                activity, callback, isMetric, str, IS_SINGLE);
    }


    @Override
    public void showNetworkUnavailable() {
        showShortToast(R.string.network_error);
    }
}
