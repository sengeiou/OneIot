package com.coband.cocoband.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coband.App;
import com.coband.common.utils.C;
import com.coband.common.utils.CocoUtils;
import com.coband.common.utils.DateUtils;
import com.coband.common.utils.Utils;
import com.coband.watchassistant.Card;
import com.coband.watchassistant.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imco on 4/6/16.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<Card> mCardList;

    public CardAdapter(List<Card> cardList) {
        this.mCardList = cardList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Card card = mCardList.get(position);
        if (card.getType() != C.TYPE_ACHIEVEMENT) {
            holder.mImageAchievementIcon.setVisibility(View.GONE);
            holder.mImageCardIcon.setVisibility(View.VISIBLE);
            holder.mTextMessage.setVisibility(View.VISIBLE);
            holder.mTextTime.setVisibility(View.VISIBLE);
            holder.mTextType.setVisibility(View.VISIBLE);

            setCardIconAndType(holder.mImageCardIcon, holder.mTextType, card);
            setCardMessage(holder.mTextMessage, card);
            holder.mTextTime.setText(DateUtils.getDateBySeconds("HH:mm", card.getTime()));
        } else {
            holder.mViewDivider.setVisibility(View.GONE);
            holder.mImageAchievementIcon.setVisibility(View.VISIBLE);
            holder.mImageCardIcon.setVisibility(View.GONE);
            holder.mTextMessage.setVisibility(View.GONE);
            holder.mTextTime.setVisibility(View.GONE);
            holder.mTextType.setVisibility(View.GONE);

            CocoUtils.setAchievementIcon_deprecated(holder.mImageAchievementIcon, card.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_card_icon)
        ImageView mImageCardIcon;

        @BindView(R.id.text_card_message)
        TextView mTextMessage;

        @BindView(R.id.text_card_time)
        TextView mTextTime;

        @BindView(R.id.image_achievement_card)
        ImageView mImageAchievementIcon;

        @BindView(R.id.view_time_line)
        View mViewTimeline;

        @BindView(R.id.view_divider)
        View mViewDivider;

        @BindView(R.id.text_card_type)
        TextView mTextType;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private void setCardIconAndType(ImageView icon, TextView textType, Card card) {
        switch (card.getType()) {
            case C.TYPE_NEVER_BOND:
                icon.setImageResource(R.drawable.card_no_band);
                break;
            case C.TYPE_UNBOND:
                icon.setImageResource(R.drawable.card_unbond);
                break;
            case C.TYPE_SPORT_NO_GOAL:
                icon.setImageResource(R.drawable.card_no_step_goal);
                break;
            case C.TYPE_SPORT_COMPLETED_GOAL_20000:
            case C.TYPE_SPORT_COMPLETED_GOAL_16000:
            case C.TYPE_SPORT_COMPLETED_GOAL_8000:
            case C.TYPE_SPORT_COMPLETED_GOAL_4000:
            case C.TYPE_SPORT_COMPLETED_GOAL_2000:
                icon.setImageResource(R.drawable.card_step);
                textType.setText(R.string.step);
                break;
            case C.TYPE_SLEEP_COMPLETED:
            case C.TYPE_SLEEP_QUALITY_GOOD:
            case C.TYPE_SLEEP_QUALITY_NORMAL:
            case C.TYPE_SLEEP_QUALITY_BAD:
                textType.setText(R.string.sleep);
                icon.setImageResource(R.drawable.card_sleep_tips);
                break;
            case C.TYPE_SLEEP_NO_GOAL:
                icon.setImageResource(R.drawable.card_no_sleep_goal);
                break;
            case C.TYPE_LEADERBOARD_FIRST:
                textType.setText(R.string.leaderboard);
                icon.setImageResource(R.drawable.card_leaderboard_first);
                break;
            case C.TYPE_LEADERBOARD_SECOND:
                textType.setText(R.string.leaderboard);
                icon.setImageResource(R.drawable.card_leaderboard_second);
                break;
            case C.TYPE_LEADERBOARD_THIRD:
                textType.setText(R.string.leaderboard);
                icon.setImageResource(R.drawable.card_leaderboard_third);
                break;
            case C.TYPE_LEADERBOARD_IN_100:
                textType.setText(R.string.leaderboard);
                icon.setImageResource(R.drawable.card_leaderboard_in_hundred);
                break;
            case C.TYPE_WELCOME:
                break;
            case C.TYPE_HEART_RATE_NORMAL:
                textType.setText(R.string.heart_rate);
                icon.setImageResource(R.drawable.card_heart_rate_normal);
                break;
            case C.TYPE_HEART_RATE_HIGH:
            case C.TYPE_HEART_RATE_TOO_HIGH:
            case C.TYPE_HEART_RATE_HIGHEST:
                textType.setText(R.string.heart_rate);
                icon.setImageResource(R.drawable.card_heart_rate_high);
                break;
            case C.TYPE_HEART_RATE_LOW:
            case C.TYPE_HEART_RATE_TOO_LOW:
                textType.setText(R.string.heart_rate);
                icon.setImageResource(R.drawable.card_heart_rate_low);
                break;
            case C.TYPE_SPORT_UNCOMPLETE_STEP:
                textType.setText(R.string.step);
                icon.setImageResource(R.drawable.card_step);
                break;
        }
    }

    private void setCardMessage(TextView mTextView, Card card) {
        switch (card.getType()) {
            case C.TYPE_NEVER_BOND:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_never_bond));
                break;
            case C.TYPE_UNBOND:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_unbond));
                break;
            case C.TYPE_SPORT_NO_GOAL:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_no_sport_goal));
                break;
            case C.TYPE_SPORT_COMPLETED_GOAL_20000:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_goal_20000));
                break;
            case C.TYPE_SPORT_COMPLETED_GOAL_16000:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_goal_16000));
                break;
            case C.TYPE_SPORT_COMPLETED_GOAL_8000:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_goal_8000));
                break;
            case C.TYPE_SPORT_COMPLETED_GOAL_4000:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_goal_4000));
                break;
            case C.TYPE_SPORT_COMPLETED_GOAL_2000:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_goal_2000));
                break;
            case C.TYPE_SLEEP_COMPLETED:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_sleep_completed));
                String sleepTime = Utils.formatMinute(card.getSleepTime());
                mTextView.append(Utils.getSpanString(sleepTime,
                        App.getInstance().getResources().getColor(R.color.medium_purple),
                        65, 0, sleepTime.length()));

                break;
            case C.TYPE_SLEEP_NO_GOAL:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_no_sleep_goal));
                break;
            case C.TYPE_SLEEP_QUALITY_GOOD:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_good_sleep));
                break;
            case C.TYPE_SLEEP_QUALITY_NORMAL:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_normal_sleep));
                break;
            case C.TYPE_SLEEP_QUALITY_BAD:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_bad_sleep));
                break;
            case C.TYPE_LEADERBOARD_FIRST:
            case C.TYPE_LEADERBOARD_SECOND:
            case C.TYPE_LEADERBOARD_THIRD:
            case C.TYPE_LEADERBOARD_IN_100:
                if (card.getMessage() != null) {
                    int rankingColor = App.getInstance().getResources().
                            getColor(R.color.theme_blue);
                    String ranking = card.getMessage();
                    String rankingMessage = App.getInstance().
                            getString(R.string.card_leadboard_in_100, ranking);
                    int rankingStartIndex = rankingMessage.indexOf(ranking);
                    mTextView.setText(Utils.getSpanString(rankingMessage, rankingColor, 65,
                            rankingStartIndex, rankingStartIndex + ranking.length()));
                }
                break;
            case C.TYPE_WELCOME:
                mTextView.setText(App.getInstance().
                        getString(R.string.card_welcome));
                break;
            case C.TYPE_HEART_RATE_NORMAL:
            case C.TYPE_HEART_RATE_HIGH:
            case C.TYPE_HEART_RATE_TOO_HIGH:
            case C.TYPE_HEART_RATE_LOW:
            case C.TYPE_HEART_RATE_TOO_LOW:
            case C.TYPE_HEART_RATE_HIGHEST:
                String heartRateValue = String.valueOf(card.getHeartRate());
                mTextView.setText(App.getInstance().
                        getString(R.string.heart_rate_result));
                mTextView.append(Utils.getSpanString(heartRateValue,
                        App.getInstance().getResources().getColor(R.color.theme_red),
                        65, 0, heartRateValue.length()));
                mTextView.append("BPM");
                break;

            case C.TYPE_SPORT_UNCOMPLETE_STEP:
                int color = App.getInstance().getResources().getColor(R.color.theme_blue);
                String stepValue = String.valueOf(card.getUncompleteStep());
                String unCompleteMessage = App.getInstance().
                        getString(R.string.card_sport_uncomplete_step, stepValue);
                int startIndex = unCompleteMessage.indexOf(stepValue);
                mTextView.setText(Utils.getSpanString(unCompleteMessage, color, 65, startIndex,
                        startIndex + stepValue.length()));

                break;
        }
    }
}