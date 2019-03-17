package com.example.twitching;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.twitching.Utils.TwitchApiUtils;

import java.util.ArrayList;

public class TopGamesAdapter extends RecyclerView.Adapter<TopGamesAdapter.TopGameItemViewHolder> {
    private TwitchApiUtils.Game[] mRepos;

    public void updateSearchResults(TwitchApiUtils.Game[] repos) {
        mRepos = repos;
        notifyDataSetChanged();
    }

    private OnTopGameItemClickListener mOnTopGameItemClickListener;

    public TopGamesAdapter(OnTopGameItemClickListener onTopGameItemClickListener) {
        mOnTopGameItemClickListener = onTopGameItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (mRepos != null) {
            return mRepos.length;
        } else {
            return 0;
        }
    }
    @NonNull
    @Override
    public TopGameItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new TopGameItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopGameItemViewHolder holder, int position) {
        holder.bind(mRepos[position]);
    }

    public interface OnTopGameItemClickListener {
        void onTopGameItemClick(TwitchApiUtils.Game detailedTopGame);
    }

    class TopGameItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTopGameTextView;

        public TopGameItemViewHolder(View itemView) {
            super(itemView);
            mTopGameTextView = itemView.findViewById(R.id.tv_forecast_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnTopGameItemClickListener.onTopGameItemClick(mRepos[getAdapterPosition()]);
                }
            });
        }

        public void bind(TwitchApiUtils.Game repo) {
            mTopGameTextView.setText(repo.name);
        }
    }
}
