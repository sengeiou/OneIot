package com.coband.common.tools;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import com.coband.App;
import com.coband.common.utils.Logger;
import com.coband.watchassistant.R;
import com.plattysoft.leonids.ParticleSystem;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tgc on 17-5-12.
 */

public class FireWork {

    public static void show(View view, AppCompatActivity activity) {

        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        final int width = outMetrics.widthPixels;
        final int height = outMetrics.heightPixels;

        Drawable drawableBlue = App.getContext().getResources()
                .getDrawable(R.drawable.medal_animation_flowers_blue);

        Drawable drawableGreen = App.getContext().getResources()
                .getDrawable(R.drawable.medal_animation_flowers_green);

        Drawable drawablePink = App.getContext().getResources()
                .getDrawable(R.drawable.medal_animation_flowers_pink);

        Drawable drawablePurple = App.getContext().getResources()
                .getDrawable(R.drawable.medal_animation_flowers_purple);

        Drawable drawableRed = App.getContext().getResources()
                .getDrawable(R.drawable.medal_animation_flowers_red);

        Drawable drawableYellow = App.getContext().getResources()
                .getDrawable(R.drawable.medal_animation_flowers_yellow);


        final ParticleSystem psBlue1 = new ParticleSystem((ViewGroup) view
                .findViewById(R.id.dialog_fragment_fl), 20, drawableBlue, 1100);

        final ParticleSystem psGreen1 = new ParticleSystem((ViewGroup) view
                .findViewById(R.id.dialog_fragment_fl), 20, drawableGreen, 1100);

        final ParticleSystem psPink1 = new ParticleSystem((ViewGroup) view
                .findViewById(R.id.dialog_fragment_fl), 20, drawablePink, 1100);

        final ParticleSystem psPurple1 = new ParticleSystem((ViewGroup) view
                .findViewById(R.id.dialog_fragment_fl), 20, drawablePurple, 1100);

        final ParticleSystem psRed1 = new ParticleSystem((ViewGroup) view
                .findViewById(R.id.dialog_fragment_fl), 20, drawableRed, 1100);

        final ParticleSystem psYellow1 = new ParticleSystem((ViewGroup) view
                .findViewById(R.id.dialog_fragment_fl), 20, drawableYellow, 1100);

        //new add
        final ParticleSystem psGreen2 = new ParticleSystem((ViewGroup) view
                .findViewById(R.id.dialog_fragment_fl), 20, drawableGreen, 1100);

        final ParticleSystem psGreen3 = new ParticleSystem((ViewGroup) view
                .findViewById(R.id.dialog_fragment_fl), 20, drawableGreen, 1100);

        final ParticleSystem psYellow2 = new ParticleSystem((ViewGroup) view
                .findViewById(R.id.dialog_fragment_fl), 20, drawableYellow, 1100);


        Observable.timer(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        psBlue1.setScaleRange(0.5f, 1.2f)
                                .setSpeedRange(0.05f, 0.1f)
                                .setRotationSpeedRange(90, 180)
                                .setFadeOut(200, new AccelerateInterpolator())
                                .emit(width / 108 * 70, height / 192 * 46, 20);
                        psPink1.setScaleRange(0.5f, 1.2f)
                                .setSpeedRange(0.05f, 0.1f)
                                .setRotationSpeedRange(90, 180)
                                .setFadeOut(200, new AccelerateInterpolator())
                                .emit(width / 108 * 70, height / 192 * 46, 20);
                        psGreen2.setScaleRange(0.5f, 1.2f)
                                .setSpeedRange(0.05f, 0.1f)
                                .setRotationSpeedRange(90, 180)
                                .setFadeOut(200, new AccelerateInterpolator())
                                .emit(width / 108 * 70, height / 192 * 46, 20);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e(this, throwable.getMessage());
                    }
                });


        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   psPurple1.setScaleRange(0.5f, 1.2f)
                                           .setSpeedRange(0.05f, 0.1f)
                                           .setRotationSpeedRange(90, 180)
                                           .setFadeOut(200, new AccelerateInterpolator())
                                           .emit(width / 108 * 26, height / 192 * 51, 20);

                                   psYellow1.setScaleRange(0.5f, 1.2f)
                                           .setSpeedRange(0.05f, 0.1f)
                                           .setRotationSpeedRange(90, 180)
                                           .setFadeOut(200, new AccelerateInterpolator())
                                           .emit(width / 108 * 26, height / 192 * 51, 20);

                                   psGreen3.setScaleRange(0.5f, 1.2f)
                                           .setSpeedRange(0.05f, 0.1f)
                                           .setRotationSpeedRange(90, 180)
                                           .setFadeOut(200, new AccelerateInterpolator())
                                           .emit(width / 108 * 26, height / 192 * 51, 20);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Logger.e(this, throwable.getMessage());
                            }
                        });

        Observable.timer(2210, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        psRed1.setScaleRange(0.5f, 1.2f)
                                .setSpeedRange(0.05f, 0.1f)
                                .setRotationSpeedRange(90, 180)
                                .setFadeOut(200, new AccelerateInterpolator())
                                .emit(width / 108 * 61, height / 192 * 76, 20);

                        psGreen1.setScaleRange(0.5f, 1.2f)
                                .setSpeedRange(0.05f, 0.1f)
                                .setRotationSpeedRange(90, 180)
                                .setFadeOut(200, new AccelerateInterpolator())
                                .emit(width / 108 * 61, height / 192 * 76, 20);

                        psYellow2.setScaleRange(0.5f, 1.2f)
                                .setSpeedRange(0.05f, 0.1f)
                                .setRotationSpeedRange(90, 180)
                                .setFadeOut(200, new AccelerateInterpolator())
                                .emit(width / 108 * 61, height / 192 * 76, 20);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e(this, throwable.getMessage());
                    }
                });


        Observable.timer(1100, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   psBlue1.stopEmitting();
                                   psPink1.stopEmitting();
                                   psGreen2.stopEmitting();
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Logger.e(this, throwable.getMessage());
                            }
                        });

        Observable.timer(2100, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        psPurple1.stopEmitting();
                        psYellow1.stopEmitting();
                        psGreen3.stopEmitting();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e(this, throwable.getMessage());
                    }
                });


        Observable.timer(3310, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        psRed1.stopEmitting();
                        psGreen1.stopEmitting();
                        psYellow2.stopEmitting();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e(this, throwable.getMessage());
                    }
                });
    }
}
