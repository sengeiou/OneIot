package com.coband.cocoband.widget.widget;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.R;

/**
 * Created by mqh on 7/19/16.
 */
public class UserAvatarDialog extends DialogFragment {
    private static final String TAG = "UserAvatarDialog";

    public static UserAvatarDialog newInstance(String urlPath, Rect rect) {
        UserAvatarDialog dialog = new UserAvatarDialog();
        Bundle bundle = new Bundle();
        bundle.putString("path", urlPath);
        bundle.putParcelable("rect", rect);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String path = getArguments().getString("path");
        final Rect ori = getArguments().getParcelable("rect");

        final View content = getActivity().getLayoutInflater()
                .inflate(R.layout.useravatardialog_layout, null);

        final ImageView avatar = ((ImageView) content.findViewById(R.id.imageview));

        Glide.with(this.getContext())
                .load(path)
                .placeholder(R.drawable.profile_men)
                .error(R.drawable.profile_men)
                .into(avatar);

        avatar.setClickable(true);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateClose(avatar, ori);
                Logger.d(TAG, "onClick");
            }
        });

        Dialog dialog = new Dialog(getActivity(), R.style.UserAvatarDialog) {
            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

                    animateClose(avatar, ori);
                    Logger.d(TAG, "onKeyDown");
                    return true;
                }
                return super.onKeyDown(keyCode, event);
            }
        };

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(content);

        content.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        content.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        if (ori == null) {
                            return;
                        }

                        int[] avatarLocation = new int[2];
                        avatar.getLocationOnScreen(avatarLocation);

                        final int transX = ori.left - avatarLocation[0];
                        final int transY = ori.top - avatarLocation[1];

                        final float scaleX = (float) ori.width() / (float) avatar.getWidth();
                        final float scaleY = (float) ori.height() / (float) avatar.getHeight();

                        avatar.setTranslationX(transX);
                        avatar.setTranslationY(transY);

                        avatar.setPivotX(0);
                        avatar.setPivotY(0);

                        avatar.setScaleX(scaleX);
                        avatar.setScaleY(scaleY);

                        avatar.animate().translationX(0).translationY(0).scaleY(1)
                                .scaleX(1).alpha(1.0f).setDuration(300)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                    }
                });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void animateClose(ImageView avatar, Rect ori) {
        if (ori == null) {
            Logger.d(TAG, "ori == null");

            return;
        }

        int[] avatarLocation = new int[2];
        avatar.getLocationOnScreen(avatarLocation);

        final int transX = ori.left - avatarLocation[0];
        final int transY = ori.top - avatarLocation[1];

        final float scaleX = (float) ori.width() / (float) avatar.getWidth();
        final float scaleY = (float) ori.height() / (float) avatar.getHeight();

        avatar.animate().translationX(transX).translationY(transY).scaleY(scaleY)
                .scaleX(scaleX).alpha(0.7f).rotationY(0f).setDuration(300)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        dismissAllowingStateLoss();
                    }
                });
    }


}
