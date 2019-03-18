package com.example.twitching;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.twitching.Utils.TwitchApiUtils;

import java.util.ArrayList;
import java.util.List;

public class TopGamesAdapter extends RecyclerView.Adapter<TopGamesAdapter.TopGameItemViewHolder> {
    private List<TwitchApiUtils.Game> mGames;

    public void updateSearchResults(List<TwitchApiUtils.Game> games) {
        mGames = games;
        notifyDataSetChanged();
    }

    private OnTopGameItemClickListener mOnTopGameItemClickListener;

    public TopGamesAdapter(OnTopGameItemClickListener onTopGameItemClickListener) {
        mOnTopGameItemClickListener = onTopGameItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (mGames != null) {
            return mGames.size();
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
        holder.bind(mGames.get(position));
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
                    mOnTopGameItemClickListener.onTopGameItemClick(mGames.get(getAdapterPosition()));
                }
            });
        }

        public void bind(TwitchApiUtils.Game repo) {
            mTopGameTextView.setText(repo.name);
        }
    }
}
