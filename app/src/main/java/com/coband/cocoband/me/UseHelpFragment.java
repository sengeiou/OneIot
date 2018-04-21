package com.coband.cocoband.me;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.coband.App;
import com.coband.cocoband.BaseFragment;
import com.coband.cocoband.me.viewholder.UseHelpViewHolder;
import com.coband.cocoband.mvp.iview.UserHelpView;
import com.coband.cocoband.mvp.presenter.UserHelpPresenter;
import com.coband.cocoband.tools.AppComponent;
import com.coband.watchassistant.R;
import com.imcorecyclerviewlib.MultiItemAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UseHelpFragment extends BaseFragment implements UserHelpView {
    public static final String TAG = "UseHelpFragment";

    @BindView(R.id.rv_use_help_item)
    RecyclerView rvUHI;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MultiItemAdapter adapter;
    @Inject
    UserHelpPresenter presenter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_use_help_item;
    }

    @Override
    protected void setupView() {
        setupToolbar(R.string.fragment_help_title, toolbar);
        AppComponent.getInstance().inject(this);
        presenter.attachView(this);
        setPresenter(presenter);

    }

    @Override
    protected void init() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(getResources()
                .getStringArray(R.array.device_help_name)));
        adapter = new MultiItemAdapter(UseHelpViewHolder.class);
        adapter.setDataList(list);
        rvUHI.setLayoutManager(new LinearLayoutManager(App.getContext()));
        rvUHI.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    //click operation
    @Override
    public void showHelp(int position) {
        addFragment(new DeviceUseHelpFragment(), DeviceUseHelpFragment.TAG,true);
    }
}
