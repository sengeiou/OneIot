package com.coband;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.coband.common.utils.Utils;
import com.coband.watchassistant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.image_imco_icon)
    ImageView imageImcoIcon;

    @OnClick(R.id.btn_signin)
    void signIn() {
//        Intent intent = new Intent(MainActivity.this, SignUpAndSignInActivity.class);
//        intent.putExtra(SignUpAndSignInActivity.KEY_ACTION, SignUpAndSignInActivity.SIGN_IN);
//        startActivity(intent);
        finish();

    }

    @OnClick(R.id.btn_signup)
    void signUp() {
//        Intent intent = new Intent(MainActivity.this, SignUpAndSignInActivity.class);
//        intent.putExtra(SignUpAndSignInActivity.KEY_ACTION, SignUpAndSignInActivity.SIGN_UP);
//        startActivity(intent);
        finish();
    }

    @BindView(R.id.linear_bottom)
    LinearLayout linearBottom;


    private ValueAnimator animator;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder=ButterKnife.bind(this);
        startIconAnimation();
    }

    private void startIconAnimation() {
        animator = ValueAnimator.ofInt(0, 5);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                switch (value) {
                    case 0:
                        imageImcoIcon.setImageResource(R.drawable.imco_logo_01);
                        break;
                    case 1:
                        imageImcoIcon.setImageResource(R.drawable.imco_logo_02);
                        break;
                    case 2:
                        imageImcoIcon.setImageResource(R.drawable.imco_logo_03);
                        break;
                    case 3:
                        imageImcoIcon.setImageResource(R.drawable.imco_logo_04);
                        break;
                    case 4:
                        imageImcoIcon.setImageResource(R.drawable.imco_logo_05);
                        break;
                }
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (Utils.isChineseLanguage()) {
                    imageImcoIcon.setImageResource(R.drawable.imco_login_logo_cn);
                } else {
                    imageImcoIcon.setImageResource(R.drawable.imco_login_logo_en);
                }

                linearBottom.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        mUnbinder.unbind();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
