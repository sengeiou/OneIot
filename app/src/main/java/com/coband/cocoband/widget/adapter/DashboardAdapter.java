package com.coband.cocoband.widget.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.coband.App;
import com.coband.cocoband.mvp.model.entity.DashboardData;
import com.coband.cocoband.mvp.model.entity.LastRate;
import com.coband.cocoband.mvp.model.entity.LastSleepData;
import com.coband.cocoband.mvp.model.entity.SingleBloodPressure;
import com.coband.cocoband.mvp.model.local.db.DBHelper;
import com.coband.cocoband.widget.widget.LineChart;
import com.coband.cocoband.widget.widget.SleepBarChart;
import com.coband.common.utils.C;
import com.coband.common.utils.Config;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Utils;
import com.coband.interactivelayer.bean.BloodPressurePacket;
import com.coband.watchassistant.BloodPressure;
import com.coband.watchassistant.DBWeight;
import com.coband.watchassistant.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ivan on 17-5-11.
 */

public class DashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "DashboardAdapter";

    public static final int SPORT = 16;
    public static final int MANAGER = -1;
    public static final int SLEEP = 1;
    public static final int HEART_RATE = 2;
    public static final int WEIGHT = 4;
    public static final int PRESSURE = 8;
    private int mModule = C.MODULE_ALL;


    private DashboardData mDashboardData;


    private OnModuleManageClickListener mModuleManageClickListener;
    private OnModuleSportClickListener mModuleSportClickListener;
    private OnModuleRateClickListener mModuleRateClickListener;
    private OnModuleSleepClickListener mModuleSleepClickListener;
    private OnModuleWeightClickListener mModuleWeightClickListener;
    private OnModulePressureClickListener mModulePressureClickListener;

    public interface OnModuleManageClickListener {
        void onModuleManageClick();
    }

    public interface OnModuleSportClickListener {
        void onModuleSportManageClick();
    }

    public interface OnModuleSleepClickListener {
        void onModuleManageClick();
    }

    public interface OnModuleRateClickListener {
        void onModuleManageClick();
    }

    public interface OnModuleWeightClickListener {
        void onModuleWeightClick();
    }

    public interface OnModulePressureClickListener {
        void onModulePressureClick();
    }

    public void setOnModuleManageClickListener(OnModuleManageClickListener listener) {
        this.mModuleManageClickListener = listener;
    }

    public void setOnModuleSportClickListener(OnModuleSportClickListener listener) {
        this.mModuleSportClickListener = listener;
    }

    public void setOnModuleSleepClickListener(OnModuleSleepClickListener listener) {
        this.mModuleSleepClickListener = listener;
    }

    public void setOnModuleRateClickListener(OnModuleRateClickListener listener) {
        this.mModuleRateClickListener = listener;
    }

    public void setOnModuleWeightClickListener(OnModuleWeightClickListener listener) {
        this.mModuleWeightClickListener = listener;
    }

    public void setOnModulePressureClickListener(OnModulePressureClickListener listener) {
        this.mModulePressureClickListener = listener;
    }

    public void setModule(int module) {
        if (module != mModule) {
            mModule = module;
            notifyDataSetChanged();
        }
    }

    public void setData(DashboardData data) {
        mDashboardData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SPORT:
                return new SportVH(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.dashboard_sport, parent, false));
            case SLEEP:
                return new SleepVH(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.dashboard_sleep, parent, false));
            case HEART_RATE:
                return new HeartRateVH(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.dashboard_heartrate, parent, false));
            case WEIGHT:
                return new WeightVH(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.dashboard_weight, parent, false));
            case PRESSURE:
                return new PressureVH(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.dashboard_pressure, parent, false));
            default:
                return new ModuleManageVH(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.dashboard_module_manage, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SportVH) {
            SportVH sportVH = (SportVH) holder;
            sportVH.mTvStep.setText(String.valueOf(mDashboardData.getStep()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mModuleSportClickListener.onModuleSportManageClick();
                }
            });

            sportVH.mTvCalories.setText(Utils.format2OneDecimalPlaces(mDashboardData.getCalories()));
            sportVH.mTvDistance.setText(Utils.format2OneDecimalPlaces(mDashboardData.getDistance()));
            sportVH.mTvDistance.setText(Utils.format2OneDecimalPlaces(mDashboardData.getDistance()));
            sportVH.mTvDistanceUnit.setText(mDashboardData.getUnitSystem() == Config.METRIC ?
                    sportVH.km : sportVH.miles);

        } else if (holder instanceof SleepVH) {
            SleepVH sleepVH = (SleepVH) holder;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mModuleSleepClickListener.onModuleManageClick();
                }
            });

//            sleepVH.mTvSleepDate.setText(DateUtils.getDateBySeconds("yyyy-M-d",
//                    DateUtils.getToday()));

            sleepVH.setLastDayData(mDashboardData.getLastSleepData());

//            sleepVH.mTvSleep.setText(DateUtils.formatSleepTime(mDashboardData.getSleepTotalTime(),
//                    sleepVH.hourUnit, sleepVH.minuteUnit));


//            sleepVH.setSleepData(mDashboardData.getWeekSleeps());


        } else if (holder instanceof HeartRateVH) {
            HeartRateVH heartRateVH = (HeartRateVH) holder;
//            String heartRateText = String.valueOf(mDashboardData.getAveHeartRate());
//            heartRateVH.mTvHeartRate.setText(heartRateText);
//            if (mDashboardData.getAveHeartRate() != 0) {
//                heartRateVH.mVLine.setVisibility(View.VISIBLE);
//                heartRateVH.mTvAVEHeartRate.setVisibility(View.VISIBLE);
//                heartRateVH.mTvAVEHeartRate.setText("" + mDashboardData.getAveHeartRate());
//            }
//            heartRateVH.setRightAxis(mDashboardData.getAveHeartRate());

            heartRateVH.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mModuleRateClickListener.onModuleManageClick();
                }
            });

            heartRateVH.setLastRate(mDashboardData.getLastRate());

//            heartRateVH.setHeartRateData(mDashboardData.getDayHeartRates());


//            List<SingleRate> dayRateList = mDashboardData.getDayHeartRates();
////            heartRateVH.setHeartRateData(dayRateList);
//            if (dayRateList.isEmpty()) {
//                heartRateVH.mTvHeartRateDate.setText(DateUtils.getDateBySeconds(
//                        "yyyy-M-d", DateUtils.getToday()));
//            } else {
//                long time = DateUtils.getToday() + dayRateList.get(dayRateList.size() - 1).getTime() * 60 * 1000;
//
//                heartRateVH.mTvHeartRateDate.setText(DateUtils.getDateBySeconds(
//                        "yyyy-M-d HH:mm", time));
//            }


        } else if (holder instanceof WeightVH) {
            WeightVH weightHolder = (WeightVH) holder;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mModuleWeightClickListener.onModuleWeightClick();
                }
            });

            weightHolder.setWeightData(mDashboardData.getLastSevenDayWeight(), mDashboardData.getUnitSystem());

        } else if (holder instanceof ModuleManageVH) {
            ((ModuleManageVH) holder).mLinearManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mModuleManageClickListener.onModuleManageClick();

                }
            });
        } else if (holder instanceof PressureVH) {
            PressureVH pressureHolder = (PressureVH) holder;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mModulePressureClickListener.onModulePressureClick();
                }
            });
            long date = mDashboardData.getPressureDate();
            BloodPressure pressure = mDashboardData.getPressure();
            pressureHolder.setPressureValue(pressure, date);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
//        if (holder instanceof SportVH) {
////            ((SportVH) holder).mLottieAnimationView.pauseAnimation();
//            ((SportVH) holder).mLottieAnimationView.cancelAnimation();
//        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//        if (holder instanceof SportVH) {
//            ((SportVH) holder).mLottieAnimationView.playAnimation();
//        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        SportVH sportVH = (SportVH) recyclerView.getChildViewHolder(LayoutInflater.
                from(App.getContext()).inflate(R.layout.dashboard_sport,
                recyclerView, false));
        sportVH.mLottieAnimationView = null;
    }

    @Override
    public int getItemCount() {
        int count = 1;
        count += mModule & C.MODULE_SLEEP;
        count += (mModule & C.MODULE_HEART_RATE) >> 1;
        count += (mModule & C.MODULE_WEIGHT) >> 2;
//        if (DataManager.getInstance().isSupportBloodPressure()) {
        count += (mModule & C.MODULE_PRESSURE) >> 3;
//        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return SPORT;
        }

        while (position < 5) {
            int type = mModule & (int) Math.pow(2, position - 1);
            if (type == 0) {
                position++;
            } else {
                return type;
            }
        }

        return MANAGER;
    }

    static class SportVH extends RecyclerView.ViewHolder {

        @BindView(R.id.lottie_animation_view)
        ImageView mLottieAnimationView;

        @BindView(R.id.tv_step)
        TextView mTvStep;

        @BindView(R.id.tv_distance)
        TextView mTvDistance;

        @BindView(R.id.tv_distance_unit)
        TextView mTvDistanceUnit;

        @BindView(R.id.tv_calories)
        TextView mTvCalories;

        @BindView(R.id.tv_calories_unit)
        TextView mTvCaloriesUnit;

        @BindString(R.string.km)
        String km;

        @BindString(R.string.miles)
        String miles;

        public SportVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            mLottieAnimationView.setImageAssetsFolder("images_day");
//            mLottieAnimationView.setAnimation("data_day.json");

//            mLottieAnimationView.playAnimation();

        }
    }

    class SleepVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_hour)
        TextView mTvHour;

        @BindView(R.id.tv_hour_unit)
        TextView mTvHourUnit;

        @BindView(R.id.tv_minute)
        TextView mTvMinute;

        @BindView(R.id.tv_minute_unit)
        TextView mTvMinuteUnit;

        @BindView(R.id.tv_sleep_date)
        TextView mTvSleepDate;

        @BindView(R.id.sleep_chart)
        SleepBarChart mSleepBarChart;

        @BindString(R.string.minute_short)
        String minuteUnit;

        @BindString(R.string.hour_short)
        String hourUnit;

        @BindColor(R.color.color_a0a0a0)
        int labelColor;

        @BindString(R.string.no_sleep_data)
        String noData;

        @BindDimen(R.dimen.font_12)
        int labelSize;

        SleepVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            setupSleepBarChart(mSleepBarChart);
            Typeface typeface = Typeface.createFromAsset(App.getInstance().getContext().getAssets(),
                    "fonts/RobotoCondensed-Regular.ttf");
            mSleepBarChart.setLabelAppearance(labelSize, labelColor, typeface);
        }

        private void setLastDayData(LastSleepData data) {
            if (data == null) {
                mTvHour.setVisibility(View.GONE);
                mTvHourUnit.setVisibility(View.GONE);
                mTvMinute.setText(String.valueOf(0));
                mTvSleepDate.setText(DateUtils.getDateBySeconds("yyyy-M-d",
                        DateUtils.getToday()));
                return;
            }


            mTvSleepDate.setText(DateUtils.getDateBySeconds("yyyy-M-d", data.getDate()));
            int totalTime = data.getTotalTime();
            if (totalTime <= 60) {
                mTvHour.setVisibility(View.GONE);
                mTvHourUnit.setVisibility(View.GONE);
                mTvMinute.setText(String.valueOf(totalTime));
            } else {
                mTvHourUnit.setVisibility(View.VISIBLE);
                mTvHour.setVisibility(View.VISIBLE);
                int hour = (totalTime - (totalTime % 60)) / 60;
                int minute = totalTime % 60;
                mTvHour.setText(String.valueOf(hour));
                mTvMinute.setText(String.valueOf(minute));
            }

//            DaySleepInfo info = data.getDaySleepInfo();
//            if (info != null) {
//                mSleepBarChart.setVisibility(View.VISIBLE);
//                SleepDayDateBean bean = new SleepDayDateBean();
//                bean.setAwakeCount(info.getAwakeCount());
//                bean.setDate(info.getDate());
//                bean.setDeepTime(info.getDeepTime());
//                bean.setLightTime(info.getLightTime());
//                bean.setSleepStatusArray(info.getStatusArray());
//                bean.setSleepTime(info.getSleepTotalTime());
//                bean.setTimePointArray(info.getTimeOfStatus());
//                bean.setSleepQuality(info.getQuality());
//                bean.setDurationTimeArray(info.getDurationTimeArray());
//                mSleepBarChart.setSleepDataBean(bean);
//            } else {
//                mSleepBarChart.setVisibility(View.GONE);
//            }
        }
    }

    static class HeartRateVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_heart_rate)
        TextView mTvHeartRate;
        @BindView(R.id.tv_heart_rate_date)
        TextView mTvHeartRateDate;

        @BindString(R.string.heart_rate_frequency)
        String mHeartRateFrequency;

        @BindColor(R.color.color_ff6464)
        int lineColor;

        @BindColor(R.color.color_ffefef)
        int fillColor;

        @BindColor(R.color.color_c8c8c8)
        int labelColor;

        @BindString(R.string.no_heart_rate_data)
        String noData;

        HeartRateVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        private void setLastRate(LastRate lastRate) {
            if (lastRate == null || lastRate.getSingleRate() == null) {
                mTvHeartRateDate.setText(DateUtils.getDateBySeconds(
                        "yyyy-M-d", DateUtils.getToday()));
                mTvHeartRate.setText(String.valueOf(0));
            } else {
                long time = lastRate.getDate() + lastRate.getSingleRate().getTime() * 60;
                mTvHeartRateDate.setText(DateUtils.getDateBySeconds(
                        "yyyy-M-d HH:mm", time));
                mTvHeartRate.setText(String.valueOf(lastRate.getSingleRate().getRate()));
            }
        }
    }

    static class WeightVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_weight)
        TextView mTvWeight;

        @BindView(R.id.tv_weight_unit)
        TextView mTvWeightUnit;

        @BindView(R.id.tv_weight_date)
        TextView mTvWeightDate;

        @BindView(R.id.line_chart_weight)
        LineChart mLineChartWeight;

        @BindColor(R.color.color_fff7e5)
        int fillColor;

        @BindColor(R.color.color_ffb400)
        int lineColor;

        @BindString(R.string.no_weight_data)
        String noData;

        @BindColor(R.color.color_a0a0a0)
        int labelColor;


        @BindString(R.string.kg)
        String kg;

        @BindString(R.string.lb)
        String lb;

        @BindDimen(R.dimen.font_12)
        float labelSize;

        WeightVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Typeface typeface = Typeface.createFromAsset(App.getContext().getAssets(),
                    "fonts/RobotoCondensed-Regular.ttf");
            mLineChartWeight.setLabelAppearance(labelSize, labelColor, typeface);
        }

        private void setWeightData(List<DBWeight> dbWeights, int unit) {
            if (dbWeights.isEmpty()) {
                mTvWeight.setText(Utils.format2OneDecimalPlaces(0));
                mTvWeightUnit.setText(unit == Config.METRIC ? kg : lb);
                mTvWeightDate.setText(DateUtils.getDateBySeconds("yyyy-M-d",
                        DateUtils.getToday()));
            } else {
                DBWeight lastWeight = dbWeights.get(dbWeights.size() - 1);
                mTvWeight.setText(Utils.format2OneDecimalPlaces(lastWeight.getWeight()));
                mTvWeightUnit.setText(unit == Config.METRIC ? kg : lb);
                mTvWeightDate.setText(DateUtils.getDateBySeconds("yyyy-M-d",
                        lastWeight.getDate()));

            }

            setupChart(dbWeights);
        }


        private void setupChart(List<DBWeight> weights) {
            if (weights.isEmpty()) {
                mLineChartWeight.setVisibility(View.GONE);
            } else {
                mLineChartWeight.setVisibility(View.VISIBLE);
            }

            List<LineChart.Data> dataList = new ArrayList<>();
            for (int i = 0; i < weights.size(); i++) {
                LineChart.Data data = new LineChart.Data();
                data.setValue(weights.get(i).getWeight());
                data.setLabel(DateUtils.getDateBySeconds("M/d", weights.get(i).getDate()));
                dataList.add(data);
            }

            mLineChartWeight.setData(dataList);
        }
    }

    static class PressureVH extends RecyclerView.ViewHolder {
        @BindView(R.id.time_value_tv)
        TextView timeValueTv;
        @BindView(R.id.pressure_value_tv)
        TextView pressureValueTv;

        PressureVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setPressureValue(BloodPressure pressure, long date) {
            if (null != pressure && date > 0) {

//                List<Long> timeList = new ArrayList<>();
                Gson gson = new Gson();
                ArrayList<SingleBloodPressure> list = gson.fromJson(pressure.getBloodPressures(),
                        new TypeToken<List<SingleBloodPressure>>() {
                        }.getType());
                ArrayList<BloodPressurePacket> bloodPressurePackets = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    BloodPressurePacket packet = new BloodPressurePacket();
                    packet.setHeartRate(list.get(i).getHeartRate());
                    packet.setBpHigh(list.get(i).getHighPressure());
                    packet.setBpLow(list.get(i).getLowPressure());
                    long min = (list.get(i).getTime() - date) / 60;
                    packet.setTimeStamp(min);
                    long sec = list.get(i).getTime() - date - min * 60;
                    packet.setSecond(new Long(sec).intValue());
                    bloodPressurePackets.add(packet);
//                    if (pressure.getDate() < DateUtils.getToday()) {
//                        timeList.add(pressure.getDate());
//                    }
                }
                int index = bloodPressurePackets.size() - 1;
                int bpHigh = bloodPressurePackets.get(index).getBpHigh();
                int bpLow = bloodPressurePackets.get(index).getBpLow();
                long timeStamp = bloodPressurePackets.get(index).getTimeStamp();

                long today = pressure.getDate();

                String timeStr = DateUtils.getTime((timeStamp / 60) + ":" + (timeStamp % 60));
                String dateStr = DateUtils.getDateBySeconds("yyyy-M-d", today);
                timeValueTv.setText(dateStr + " " + timeStr);
                pressureValueTv.setText(bpHigh + "/" + bpLow);
            } else {
                pressureValueTv.setText(R.string.no_blood_data);
                timeValueTv.setText(DateUtils.getDateBySeconds("yyyy-M-d", DateUtils.getToday()));
            }
        }
    }

    static class ModuleManageVH extends RecyclerView.ViewHolder {
        @BindView(R.id.linear_manage)
        LinearLayout mLinearManage;

        ModuleManageVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
