package com.gtappdevelopers.netflixapp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.gtappdevelopers.netflixapp.Models.VideoRVModal;
import com.gtappdevelopers.netflixapp.R;
import com.gtappdevelopers.netflixapp.VideoDisplayActivity;
import com.gtappdevelopers.netflixapp.VideoPlayerActivity;
import com.gtappdevelopers.netflixapp.ViewHolder.VideoViewHolder;

public class VideoRVAdapter extends FirestorePagingAdapter<VideoRVModal, VideoViewHolder> {
    private ImageView videoIV;

    /**
     * Construct a new FirestorePagingAdapter from the given {@link FirestorePagingOptions}.
     *
     * @param options
     */
    public VideoRVAdapter(@NonNull FirestorePagingOptions<VideoRVModal> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VideoViewHolder videoViewHolder, int i, @NonNull VideoRVModal videoRVModal) {
        videoIV = videoViewHolder.itemView.findViewById(R.id.idIVVideo);
        String thumbnailUrl = "http://img.youtube.com/vi/" + videoRVModal.getVideoID() + "/hqdefault.jpg";
        videoViewHolder.setVideoIV(thumbnailUrl);
        videoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(videoViewHolder.itemView.getContext(), VideoDisplayActivity.class);
                i.putExtra("videoTitle", videoRVModal.getVideoTitle());
                i.putExtra("videoCategory", videoRVModal.getVideoCategory());
                i.putExtra("videoDesc", videoRVModal.getVideoDesc());
                i.putExtra("videoID", videoRVModal.getVideoID());
                videoViewHolder.itemView.getContext().startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_rv_item, parent, false);
        return new VideoViewHolder(view);
    }
}
