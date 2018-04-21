package com.coband.cocoband.mvp.iview;

import com.coband.cocoband.mvp.BaseView;
import com.coband.cocoband.mvp.model.entity.Module;

import java.util.List;

/**
 * Created by ivan on 17-4-19.
 */

public interface ModuleManageView extends BaseView{
    void showAppearance(int appearance);

    void showModule(List<Module> modules);

}
