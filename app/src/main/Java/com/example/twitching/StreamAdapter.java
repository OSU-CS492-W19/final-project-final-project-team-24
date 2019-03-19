package com.example.twitching;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.twitching.Utils.TwitchApiUtils;

import java.util.List;

public class StreamAdapter extends RecyclerView.Adapter<StreamAdapter.StreamItemViewHolder> {
    private List<TwitchApiUtils.Stream> mStreams;

    public void updateAdapter(List<TwitchApiUtils.Stream> streams) {
        mStreams = streams;
        notifyDataSetChanged();
    }

    private OnStreamClickListener mOnStreamsClickListener;

    public StreamAdapter(OnStreamClickListener onStreamsClickListener){
        mOnStreamsClickListener = onStreamsClickListener;
    }

    public List<TwitchApiUtils.Stream> getStreams(){
        return mStreams;
    }

    @NonNull
    @Override
    public StreamItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.forecast_list_item, viewGroup, false);
        return new StreamItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StreamItemViewHolder streamItemViewHolder, int i) {
        streamItemViewHolder.bind(mStreams.get(i));
    }

    @Override
    public int getItemCount() {
        if (mStreams != null) {
            return mStreams.size();
        } else {
            return 0;
        }
    }

    public interface OnStreamClickListener {
        void onStreamClick(TwitchApiUtils.Stream detailedTopGame);
    }

    class StreamItemViewHolder extends RecyclerView.ViewHolder{
        private TextView mStreamTextView;
        public StreamItemViewHolder(View itemView){
            super(itemView);
            mStreamTextView = itemView.findViewById(R.id.tv_forecast_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnStreamsClickListener.onStreamClick(mStreams.get(getAdapterPosition()));
                }
            });
        }

        public void bind(TwitchApiUtils.Stream repo){mStreamTextView.setText(repo.title);}
    }
}
