package com.coband.common.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.coband.App;
import com.coband.cocoband.mvp.model.IMCOCallback;
import com.coband.cocoband.mvp.model.IMCOCallback2;
import com.coband.cocoband.mvp.model.bean.SignUpInfo;
import com.coband.cocoband.mvp.model.local.prefrences.PreferencesHelper;
import com.coband.cocoband.mvp.model.remote.server.NetworkOperation;
import com.coband.cocoband.mvp.presenter.SetUserInfoPresenter;
import com.coband.cocoband.widget.widget.GuideProgressDialog;
import com.coband.watchassistant.R;
import com.meituan.android.walle.WalleChannelReader;

import java.io.File;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by mai on 17-2-17.
 */

public class DialogUtils {
    private static final String TAG = "DialogUtils";
    public static final int SELECT_PIC = 0;
    public static final int TAKE_PIC = 1;
    public static final int METRIC = 0;
    public static final int INCH = 1;

    public static GuideProgressDialog showGuideProgressDialog(Activity activity, String message) {
        GuideProgressDialog dialog = new GuideProgressDialog(activity, message);
        dialog.setCancelable(false);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();
        return dialog;
    }

    public static GuideProgressDialog showGuideProgressDialogCanExit(Activity activity, String message) {
        GuideProgressDialog dialog = new GuideProgressDialog(activity, message);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
        return dialog;
    }

    /**************Nick Name Dialog****************/

    public static void setNickName(Activity activity, final IMCOCallback<String> callback) {
        LayoutInflater inflater = activity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_input_nickname,
                (ViewGroup) activity.findViewById(R.id.dialog_input_nickname_ll));
        new AlertDialog.Builder(activity, R.style.DialogTheme).setView(layout)
                .setPositiveButton(activity.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText nickName = (EditText) layout.findViewById(R.id.nick_name_edit);
                        callback.done(nickName.getText().toString());
                    }
                })
                .setNegativeButton(activity.getString(R.string.cancel), null)
                .show();

    }


    /*************Gender Dialog*****************/

    public static void showSingleChoiceDialog(Activity activity, String str,
                                              final IMCOCallback<String> callback) {
        final String[] sexArray = new String[]{activity.getString(R.string.male), activity.getString(R.string.female)};
        int index;
        if (sexArray[0].equals(str)) {
            index = 0;
        } else {
            index = 1;
        }
        new AlertDialog.Builder(activity, R.style.DialogTheme).setSingleChoiceItems(sexArray,
                index, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sexStr = sexArray[which];
                        callback.done(sexStr);
                        dialog.dismiss();
                    }
                }).show();
    }

    public static void showSingleChoiceDialog(Activity activity, final String[] items, int currentIndex,
                                              final IMCOCallback<String> callback) {
        new AlertDialog.Builder(activity, R.style.DialogTheme).setSingleChoiceItems(items,
                currentIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sexStr = items[which];
                        callback.done(sexStr);
                        dialog.dismiss();
                    }
                }).show();
    }


    /*************Date Dialog*****************/
    /**
     * @param activity
     * @param callback
     * @deprecated add by ivan.
     */

    public static void showDatePickerDialog(String year, String month, String day,
                                            Activity activity, final IMCOCallback<String> callback) {

        LayoutInflater inflater = activity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.birth_num_picker_layout,
                (ViewGroup) activity.findViewById(R.id.birth_num_picker_layout_ll));

        final DatePicker picker = (DatePicker) layout.findViewById(R.id.birth_choice_picker);

        Date date = new Date();
        long time = date.getTime();
        picker.setMaxDate(time);
        picker.init(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day), null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);
        builder.setView(layout)
                .setPositiveButton(activity.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        DatePicker picker= (DatePicker) layout.findViewById(R.id.birth_choice_picker);
                        int year = picker.getYear();
                        int month = picker.getMonth();
                        int day = picker.getDayOfMonth();

                        String dayStr;
                        String monthStr;
                        if (month + 1 < 10) {
                            monthStr = "0" + (month + 1);
                        } else {
                            monthStr = month + 1 + "";
                        }
                        if (day < 10) {
                            dayStr = "0" + day;
                        } else {
                            dayStr = day + "";
                        }

                        callback.done(year + "-" + monthStr + "-" + dayStr);

                    }
                }).setNegativeButton(activity.getString(R.string.cancel), null).show();

    }

    /**
     * add by ivan.
     *
     * @param activity
     * @param callback
     */
    public static void showDatePickerDialogCompat(Activity activity, final IMCOCallback2<Long> callback) {

        LayoutInflater inflater = activity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.birth_num_picker_layout,
                (ViewGroup) activity.findViewById(R.id.birth_num_picker_layout_ll));

        final DatePicker picker = (DatePicker) layout.findViewById(R.id.birth_choice_picker);

        Date date = new Date();
        long time = date.getTime();
        picker.setMaxDate(time);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);
        builder.setView(layout)
                .setPositiveButton(activity.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        DatePicker picker= (DatePicker) layout.findViewById(R.id.birth_choice_picker);
                        int year = picker.getYear();
                        int month = picker.getMonth();
                        int day = picker.getDayOfMonth();

                        Calendar calendar = Calendar.getInstance();
                        calendar.clear();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        Logger.d(TAG, "milliseconds >>>>> " + calendar.getTimeInMillis());
                        callback.done(calendar.getTimeInMillis());

                    }
                }).setNegativeButton(activity.getString(R.string.cancel), null).show();

    }

    /*************Custom Picker Dialog*****************/

    public static void showPickerDialogCompat(String[] unit, int layoutID,
                                              int viewGroupID, final int integerMax1,
                                              final int integerMin1,
                                              final int integerMax2, final int integerMin2,
                                              final int decimalMax1, final int decimalMin1,
                                              final int decimalMax2, final int decimalMin2,
                                              final String str1, final String str2,
                                              final Activity activity,
                                              final IMCOCallback2<String> callback) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(layoutID, (ViewGroup) activity.findViewById(viewGroupID));

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);
        builder.setView(layout);

        final NumberPicker integerNumPicker = (NumberPicker) layout.findViewById(R.id.integerNum);
        final NumberPicker decimalNumPicker = (NumberPicker) layout.findViewById(R.id.decimalNum);
        final NumberPicker heightPicker = (NumberPicker) layout.findViewById(R.id.height_unit);

        heightPicker.setDisplayedValues(unit);
        heightPicker.setMinValue(0);
        heightPicker.setMaxValue(unit.length - 1);

        integerNumPicker.setMinValue(integerMin1);
        integerNumPicker.setMaxValue(integerMax1);

        decimalNumPicker.setMinValue(decimalMin1);
        decimalNumPicker.setMaxValue(decimalMax1);


        heightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (heightPicker.getValue() == 0) {
                    integerNumPicker.setMinValue(integerMin1);
                    integerNumPicker.setMaxValue(integerMax1);

                    decimalNumPicker.setMinValue(decimalMin1);
                    decimalNumPicker.setMaxValue(decimalMax1);

                    integerNumPicker.setValue(integerMin1);
                    decimalNumPicker.setValue(decimalMin1);

                } else if (heightPicker.getValue() == 1) {
                    integerNumPicker.setMinValue(integerMin2);
                    integerNumPicker.setMaxValue(integerMax2);

                    decimalNumPicker.setMinValue(decimalMin2);
                    decimalNumPicker.setMaxValue(decimalMax2);

                    integerNumPicker.setValue(integerMin2);
                    decimalNumPicker.setValue(decimalMin2);

                }
            }
        });

        builder.setPositiveButton(activity.getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int integer = integerNumPicker.getValue();
                int decimal = decimalNumPicker.getValue();
                int unitNum = heightPicker.getValue();
                if (unitNum == 0) {
                    callback.done(integer + "." + decimal, str1);
                } else if (unitNum == 1) {
                    if (str2.equals(App.getContext().getString(R.string.foot))) {
                        callback.done(integer + App.getContext().getString(R.string.foot) + decimal + App.getContext().getString(R.string.inch_unit));
                    } else {
                        callback.done(integer + "." + decimal, str2);
                    }


                }
            }
        });

        builder.setNegativeButton(activity.getString(R.string.cancel), null);

        builder.show();
    }


    public static void showUserInfoSettingsDialog(String unit, String ok, String cancel, int integerMax,
                                                  int integerMin, int decimalMax, int decimalMin,
                                                  Activity context, final IMCOCallback<Double> callback) {
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_user_info_settings_picker, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
        builder.setView(layout);

        final NumberPicker integerNumPicker = (NumberPicker) layout.findViewById(R.id.picker_integer);
        final NumberPicker decimalNumPicker = (NumberPicker) layout.findViewById(R.id.picker_decimal);
        final TextView tvUnit = (TextView) layout.findViewById(R.id.tv_unit);

        tvUnit.setText(unit);

        integerNumPicker.setMaxValue(integerMax);
        integerNumPicker.setMinValue(integerMin);

        decimalNumPicker.setMaxValue(decimalMax);
        decimalNumPicker.setMinValue(decimalMin);


        builder.setNegativeButton(cancel, null);
        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int integer = integerNumPicker.getValue();
                int decimal = decimalNumPicker.getValue();
                callback.done((integer * 10 + decimal) / 10d);
            }
        });

        builder.show();

    }

    /*************Custom Picker Dialog Has Different**************/
    public static void showPickerDifferentDialog(String[] inputUnit, int layoutID, int viewGroupID,
                                                 final int integerMax1, final int integerMin1,
                                                 final int integerMax2, final int integerMin2,
                                                 final int decimalMax1, final int decimalMin1,
                                                 final int decimalMax2, final int decimalMin2,
                                                 final String str1, final String str2,
                                                 final Activity activity,
                                                 final IMCOCallback<String> callback,
                                                 final boolean isMetric, String inputStr, final boolean isSingle) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(layoutID, (ViewGroup) activity.findViewById(viewGroupID));

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);
        builder.setView(layout);

        final NumberPicker integerNumPicker = (NumberPicker) layout.findViewById(R.id.integerNum);
        final NumberPicker decimalNumPicker = (NumberPicker) layout.findViewById(R.id.decimalNum);
        final NumberPicker heightPicker = (NumberPicker) layout.findViewById(R.id.height_unit);

        final String[] unit;
        if (isSingle) {
            if (isMetric) {
                unit = new String[]{inputUnit[0]};
            } else {
                unit = new String[]{inputUnit[1]};
            }
        } else {
            unit = inputUnit;
        }

        heightPicker.setDisplayedValues(unit);
        heightPicker.setMinValue(0);
        heightPicker.setMaxValue(unit.length - 1);
        Log.i(TAG, "showPickerDifferentDialog: " + isMetric);
        if (isMetric) {
            integerNumPicker.setMinValue(integerMin1);
            integerNumPicker.setMaxValue(integerMax1);

            decimalNumPicker.setMinValue(decimalMin1);
            decimalNumPicker.setMaxValue(decimalMax1);
        } else {
            integerNumPicker.setMinValue(integerMin2);
            integerNumPicker.setMaxValue(integerMax2);

            decimalNumPicker.setMinValue(decimalMin2);
            decimalNumPicker.setMaxValue(decimalMax2);
        }

        if (isMetric) {
            String[] metric;
            if (isSingle) {
                metric = new String[]{unit[0]};
                heightPicker.setDisplayedValues(metric);
                heightPicker.setValue(0);
            } else {
                metric = unit;
                heightPicker.setDisplayedValues(metric);
                heightPicker.setValue(0);
            }
            if (inputStr.contains(App.getContext().getString(R.string.cm))) {
                inputStr = inputStr.substring(0, inputStr.lastIndexOf(App.getContext().
                        getString(R.string.cm)));
                String[] splitStrArray = inputStr.split("\\.");
                integerNumPicker.setValue(Integer.parseInt(splitStrArray[0]));
                decimalNumPicker.setValue(Integer.parseInt(splitStrArray[1]));
            }
            if (inputStr.contains(App.getContext().getString(R.string.kg))) {
                inputStr = inputStr.substring(0, inputStr.lastIndexOf(App.getContext().
                        getString(R.string.kg)));
                String[] splitStrArray = inputStr.split("\\.");
                integerNumPicker.setValue(Integer.parseInt(splitStrArray[0]));
                decimalNumPicker.setValue(Integer.parseInt(splitStrArray[1]));
            }
        } else {
            String[] inch;
            if (isSingle) {
                if (unit.length > 1) {
                    inch = new String[]{unit[1]};
                } else {
                    inch = new String[]{unit[0]};
                }
                heightPicker.setDisplayedValues(inch);
                heightPicker.setValue(0);
            } else {
                inch = unit;
                heightPicker.setDisplayedValues(inch);
                heightPicker.setValue(1);
            }
            if (inputStr.contains(App.getContext().getString(R.string.foot))) {
                String footStr = inputStr.substring(0, inputStr.lastIndexOf(App.getContext()
                        .getString(R.string.foot)));
                String temp = App.getContext().getString(R.string.foot);
                String inchStr = inputStr.substring(inputStr.lastIndexOf(temp) + temp.length(),
                        inputStr.lastIndexOf(App.getContext().getString(R.string.inch_unit)));
                integerNumPicker.setValue(Integer.parseInt(footStr));
                decimalNumPicker.setValue(Integer.parseInt(inchStr));
            }
            if (inputStr.contains(App.getContext().getString(R.string.lb))) {
                inputStr = inputStr.substring(0, inputStr.lastIndexOf(App.getContext().
                        getString(R.string.lb)));
                String[] splitStrArray = inputStr.split("\\.");
                integerNumPicker.setValue(Integer.parseInt(splitStrArray[0]));
                decimalNumPicker.setValue(Integer.parseInt(splitStrArray[1]));
            }
        }

        if (unit.length > 1) {
            heightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    if (heightPicker.getValue() == 0) {
                        integerNumPicker.setMinValue(integerMin1);
                        integerNumPicker.setMaxValue(integerMax1);

                        decimalNumPicker.setMinValue(decimalMin1);
                        decimalNumPicker.setMaxValue(decimalMax1);

                        integerNumPicker.setValue(integerMin1);
                        decimalNumPicker.setValue(decimalMin1);

                    } else if (heightPicker.getValue() == 1) {
                        integerNumPicker.setMinValue(integerMin2);
                        integerNumPicker.setMaxValue(integerMax2);

                        decimalNumPicker.setMinValue(decimalMin2);
                        decimalNumPicker.setMaxValue(decimalMax2);

                        integerNumPicker.setValue(integerMin2);
                        decimalNumPicker.setValue(decimalMin2);
                    }
                }
            });
        } else {
            heightPicker.setOnValueChangedListener(null);
        }

        builder.setPositiveButton(activity.getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int integer = integerNumPicker.getValue();
                int decimal = decimalNumPicker.getValue();
                if (isMetric && isSingle) {
                    // PersonalInfoFragment
//                    if (str1.equals(App.getContext().getString(R.string.cm)) || str1.equals(App.getContext().getString(R.string.kg))) {
                    // current unit is metric
//                        SetUserInfoPresenter.unitTemp = 0;
//                        if (unit.length < 2) {
//                            DBHelper.getInstance().updateUnitNoUpdateTime(METRIC);
//                        }
//                    }
                    callback.done(integer + "." + decimal + str1);

                } else if (!isMetric && isSingle) {

                    // current unit is inch
                    if (str2.contains(App.getContext().getString(R.string.foot)) || str2.equals(App.getContext().getString(R.string.lb))) {
                        SetUserInfoPresenter.unitTemp = 1;
//                        if (unit.length < 2) {
//                            DBHelper.getInstance().updateUnitNoUpdateTime(INCH);
//                        }
                    }
                    if (str2.equals(App.getContext().getString(R.string.foot))) {
                        callback.done(integer + App.getContext().getString(R.string.foot) + decimal + App.getContext().getString(R.string.inch_unit));
                    } else {
                        callback.done(integer + "." + decimal + str2);
                    }
                } else {
                    // SetUserInfoFragment
                    int unitNum = heightPicker.getValue();
                    if (unitNum == 0) {

                        if (str1.equals(App.getContext().getString(R.string.cm)) || str1.equals(App.getContext().getString(R.string.kg))) {
                            // current unit is metric
//                        DBHelper.getInstance().updateUnitNoUpdateTime(METRIC);
                            SetUserInfoPresenter.unitTemp = 0;
//                        UTESPUtil.putAndApply(GlobalVariable.IS_METRIC_UNIT_SP, true);
                        }
                        callback.done(integer + "." + decimal + str1);

                    } else if (unitNum == 1) {
                        // current unit is inch
                        if (str2.contains(App.getContext().getString(R.string.foot)) || str2.equals(App.getContext().getString(R.string.lb))) {
//                        DBHelper.getInstance().updateUnitNoUpdateTime(INCH);
                            SetUserInfoPresenter.unitTemp = 1;
//                        UTESPUtil.putAndApply(GlobalVariable.IS_METRIC_UNIT_SP, false);
                        }
                        if (str2.equals(App.getContext().getString(R.string.foot))) {
                            callback.done(integer + App.getContext().getString(R.string.foot) + decimal + App.getContext().getString(R.string.inch_unit));
                        } else {
                            callback.done(integer + "." + decimal + str2);
                        }
                    }
                }
            }
        });

        builder.setNegativeButton(activity.getString(R.string.cancel), null);

        builder.show();
    }

    public static void showHeightDifferentDialog(boolean isMetric, String str, boolean isSingle,
                                                 Activity activity,
                                                 final IMCOCallback<String> callback) {

        String[] unitHeight = new String[]{activity.getString(R.string.cm),
                activity.getString(R.string.foot) + "、" + activity.getString(R.string.inch_unit)};


        DialogUtils.showPickerDifferentDialog(unitHeight, R.layout.height_picker_dialog,
                R.id.height_picker_dialog_ll, 300, 20, 9, 1, 9, 0, 11, 0,
                activity.getString(R.string.cm), activity.getString(R.string.foot),
                activity, callback, isMetric, str, isSingle);
    }

    public static void showWeightDifferentDialog(boolean isMetric, String str, boolean isSingle,
                                                 Activity activity,
                                                 final IMCOCallback<String> callback) {

        String[] unitWeight = new String[]{activity.getString(R.string.kg),
                activity.getString(R.string.lb)};

        DialogUtils.showPickerDifferentDialog(unitWeight, R.layout.height_picker_dialog,
                R.id.height_picker_dialog_ll, 500, 2, 1100, 4, 9, 0, 9, 0,
                activity.getString(R.string.kg), activity.getString(R.string.lb),
                activity, callback, isMetric, str, isSingle);
    }


    public static void showWeightDialogCompat(String[] unitWeight, Activity activity,
                                              final IMCOCallback2<String> callback) {

        DialogUtils.showPickerDialogCompat(unitWeight, R.layout.height_picker_dialog,
                R.id.height_picker_dialog_ll, 500, 2, 1100, 4, 9, 0, 9, 0, activity.getString(R.string.kg), activity.getString(R.string.lb),
                activity, callback);
    }


    /************Modify Avatar Dialog************/
    public static void showImageSelector(Activity activity, final IMCOCallback<Integer> callback,
                                         @StringRes int titleRes) {
        final View dialogContentView = LayoutInflater.from(activity)
                .inflate(R.layout.custom_dialog_layout, null);
        final MaterialDialog materialDialog = Utils.showCustomViewDialog(activity, dialogContentView);
        TextView tvTitle = (TextView) dialogContentView.findViewById(R.id.tv_title);
        Button btnTakePhoto = (Button) dialogContentView.findViewById(R.id.btn_first);
        Button btnSelectImage = (Button) dialogContentView.findViewById(R.id.btn_second);
        tvTitle.setText(titleRes);
        btnTakePhoto.setText(R.string.take_photo_upload);
        btnSelectImage.setText(R.string.select_image_from_gallery);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (materialDialog != null) {
                    materialDialog.dismiss();
                }
                callback.done(SELECT_PIC);
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (materialDialog != null) {
                    materialDialog.dismiss();
                }
                callback.done(TAKE_PIC);
            }
        });


        materialDialog.show();
    }

    /************Get Picture From Camera************/
    public static Uri takePic(Fragment fragment, int take_pic) throws Exception {
        File picFile = BitmapUtils.createPicFile("avat.jpg");
        if (picFile != null) {
            Uri uri = Uri.fromFile(picFile);
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            fragment.startActivityForResult(camera, take_pic);
            return uri;
        } else {
            Logger.d(TAG, "picFile is null ");
            throw new Exception("picFile is null ");
        }
    }

    /************Get Picture From Gallery************/
    public static void selectPic(Fragment fragment, int select_pic) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        fragment.startActivityForResult(intent, select_pic);
    }


    public static void normalDialog(Activity mActivity, @StringRes int title,
                                    @StringRes int message, final IMCOCallback<Boolean> imcoCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.DialogTheme);
        if (title != 0) {
            builder.setMessage(title);
        }

        if (message != 0) {
            builder.setTitle(message);
        }
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imcoCallback.done(true);
            }
        });
        builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imcoCallback.done(false);
            }
        });
        builder.create().show();
    }

    public static void showDialog(Activity mActivity, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.DialogTheme);
        if (title != null) {
            builder.setTitle(title);
        }

        if (message != null) {
            builder.setMessage(message);
        }
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}