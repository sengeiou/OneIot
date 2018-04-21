package com.coband.cocoband;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.coband.App;
import com.coband.cocoband.mvp.BasePresenter;
import com.coband.cocoband.widget.widget.LoadingDialog;
import com.coband.common.utils.SoftKeyBoardUtils;
import com.coband.watchassistant.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.PermissionRequest;

/**
 * Created by ivan on 17-4-11.
 */

public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;

    /**
     * override method to set fragment layout
     *
     * @return the layout id.
     */
    protected abstract int getLayout();

    /**
     * you can do something in this method, e.g init presenter load data etc.
     */
    protected abstract void init();

    /**
     * setup view in this method
     */
    protected abstract void setupView();

    protected View mRootView;

    private Unbinder mUnbinder;

    private BasePresenter mPresenter;

    private LoadingDialog mLoadingDialog;

    protected boolean isNeedOptionMenu() {
        return false;
    }

    protected void setPresenter(BasePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(isNeedOptionMenu());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayout(), container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        setupView();
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        SoftKeyBoardUtils.hideSoftInput(App.getContext(), BaseFragment.this.getView());
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    protected void showRationaleDialog(@StringRes int messageId,
                                       final PermissionRequest request) {
        new MaterialDialog.Builder(mActivity)
                .content(messageId)
                .cancelable(false)
                .negativeColor(mActivity.getResources().getColor(R.color.color_1E78FF))
                .positiveColor(mActivity.getResources().getColor(R.color.color_1E78FF))
                .positiveText(mActivity.getString(R.string.allow))
                .negativeText(mActivity.getString(R.string.deny))
                .onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        request.proceed();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        request.cancel();
                    }
                })
                .build().show();
    }


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation anim;
        if (enter) {
            anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
        } else {
            anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
        }

        anim.setDuration(50L);

        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                init();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        return anim;
//        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    public void addFragment(BaseFragment fragment, String tag) {
        addFragment(fragment, tag, true, null);
    }

    public void addFragment(Fragment fragment, String tag, boolean addToBackStack, Bundle bundle) {

        if (mActivity == null) {
            return;
        }

        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        transaction.add(R.id.content, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    public void addFragment(Fragment fragment, String tag, boolean addToBackStack) {
        addFragment(fragment, tag, addToBackStack, null);
//        if (mActivity == null) {
//            return;
//        }
//
//        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//        transaction.add(R.id.content, fragment, tag);
//        if (addToBackStack) {
//            transaction.addToBackStack(tag);
//        }
//        transaction.commit();
    }

    public void showFragment(Fragment oldFragment, Fragment newFragment) {
        if (mActivity == null) {
            return;
        }

        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.hide(oldFragment);
        transaction.show(newFragment);
        transaction.commit();
    }

    protected void popFragment() {
        if (mActivity == null) {
            return;
        }

        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    protected View getRootView() {
        return mRootView;
    }

    protected void setupToolbar(Toolbar toolbar, String title) {
        setupToolbar(title, toolbar);
    }

    protected void setupToolbar(@StringRes int titleRes, Toolbar toolbar) {
        setupToolbar(getString(titleRes), toolbar);
    }

    protected void setupToolbar(String title, Toolbar toolbar) {
        setupToolbar(title, toolbar, R.drawable.ic_arrow_back_black_24dp);
    }

    protected void setupToolbar(String title, Toolbar toolbar, @DrawableRes int icon) {
        toolbar.setTitle(title);
        if (icon != 0) toolbar.setNavigationIcon(icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    protected void showShortToast(String toast) {
        Toast.makeText(mActivity, toast, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String toast) {
        Toast.makeText(mActivity, toast, Toast.LENGTH_SHORT).show();
    }


    protected void showShortToast(@StringRes int stringRes) {
        showShortToast(getString(stringRes));
    }

    protected void showLongToast(@StringRes int stringRes) {
        showLongToast(getString(stringRes));
    }

    protected void showDialog(Context context, String title, String content,
                              MaterialDialog.SingleButtonCallback positiveCallback) {
        new MaterialDialog.Builder(context)
                .content(content)
                .title(title)
                .cancelable(true)
                .negativeColor(context.getResources().getColor(R.color.color_1E78FF))
                .positiveColor(context.getResources().getColor(R.color.color_1E78FF))
                .positiveText(context.getString(R.string.ok))
                .negativeText(context.getString(R.string.cancel))
                .onPositive(positiveCallback)
                .build().show();
    }

    protected void showSingleButtonDialog(Context context, String title, String content,
                                          MaterialDialog.SingleButtonCallback positiveCallback) {
        new MaterialDialog.Builder(context)
                .content(content)
                .title(title)
                .cancelable(true)
                .positiveColor(context.getResources().getColor(R.color.color_1E78FF))
                .positiveText(context.getString(R.string.ok))
                .onPositive(positiveCallback)
                .build().show();
    }


    protected void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mActivity);
            mLoadingDialog.show();
        } else {
            mLoadingDialog.show();
        }
    }

    protected void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }


    public void replaceFragment(Fragment fragment, String tag, boolean addToBackStack, Bundle bundle) {
        if (mActivity == null) {
            return;
        }

        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        transaction.replace(R.id.content, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, String tag, boolean addToBackStack) {
        if (mActivity == null) {
            return;
        }

        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.replace(R.id.content, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

}
