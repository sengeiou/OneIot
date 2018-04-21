package com.coband.cocoband.me;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.coband.common.tools.FireWork;
import com.coband.common.utils.CocoUtils;
import com.coband.watchassistant.R;

/**
 * Created by tgc on 17-5-11.
 */

public class GetMedalDialogFragment extends DialogFragment implements View.OnClickListener {

    private ImageView medalImg;
    private TextView medalTitle;
    private TextView medalText;
    private TextView ok;
    private int imgRes, text;
    private String title;
    private View view;
    private boolean isGet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view = inflater.inflate(R.layout.dialog_fragment_medal_get, container);
        initView(view);
        setData();
        if (isGet) {
            FireWork.show(view, (AppCompatActivity) getActivity());
        }

        return view;
    }

    private void initView(View view) {
        medalImg = (ImageView) view.findViewById(R.id.dialog_medal_img);
        medalTitle = (TextView) view.findViewById(R.id.dialog_medal_title);
        medalText = (TextView) view.findViewById(R.id.dialog_medal_text);
        ok = (TextView) view.findViewById(R.id.dialog_medal_ok);
        ok.setOnClickListener(this);

    }

    private void setData() {
        imgRes = getArguments().getInt("imgRes");
        title = getArguments().getString("title");
        text = getArguments().getInt("text");
        isGet = getArguments().getBoolean("isGet");
        medalImg.setImageResource(imgRes);
        medalTitle.setText(CocoUtils.getMedalTitleLocalLangFromTitle(title));
        medalText.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_medal_ok:
                this.dismiss();
                break;
            default:
                break;
        }
    }
}
