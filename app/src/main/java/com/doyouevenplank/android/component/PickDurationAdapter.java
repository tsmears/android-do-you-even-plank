package com.doyouevenplank.android.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.PickDurationActivity;
import com.doyouevenplank.android.activity.PreviewVideoActivity;
import com.doyouevenplank.android.app.Config;
import com.doyouevenplank.android.app.SessionManager;
import com.doyouevenplank.android.util.StringUtils;

public class PickDurationAdapter extends RecyclerView.Adapter<PickDurationAdapter.TextViewItemViewHolder> {

    private static int TYPE_HEADER_ITEM = 0;
    private static int TYPE_DURATION_ITEM = 1;

    private PickDurationActivity mActivity;

    public PickDurationAdapter(PickDurationActivity activity) {
        mActivity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER_ITEM;
        } else {
            return TYPE_DURATION_ITEM;
        }
    }

    @Override
    public PickDurationAdapter.TextViewItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == TYPE_HEADER_ITEM) {
            TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.pick_duration_header_item, parent, false);
            return new TextViewItemViewHolder(textView);
        } else { // assume viewType == TYPE_DURATION_ITEM) {
            TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.pick_duration_duration_item, parent, false);
            return new TextViewItemViewHolder(textView);
        }
    }

    @Override
    public void onBindViewHolder(PickDurationAdapter.TextViewItemViewHolder holder, int position) {
        final Context context = holder.textView.getContext();
        int viewType = this.getItemViewType(position);
        if (viewType == TYPE_HEADER_ITEM) {
            // do nothing; the header is static
        } else { // assume viewType == TYPE_DURATION_ITEM) {
            final int duration = Config.PLANK_CHOICE_DURATIONS[position - 1];
            holder.textView.setText(StringUtils.getTimeStringFromIntDuration(duration));
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SessionManager.getInstance().isReady()) {
                        PreviewVideoActivity.start(context, duration);
                    } else {
                        Toast.makeText(mActivity, R.string.error_fetching_video_metadata, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Config.PLANK_CHOICE_DURATIONS.length;
    }

    public static class TextViewItemViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public TextViewItemViewHolder(TextView textView) {
            super(textView);
            this.textView = textView;
        }

    }

}