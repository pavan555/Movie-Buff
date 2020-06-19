package com.pavan.moviebuff.Utils.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.pavan.moviebuff.R;
import com.pavan.moviebuff.Utils.DTO.Result.video;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private ArrayList<video> videoArrayList;
    private Lifecycle lifecycle;

    public VideosAdapter(ArrayList<video> videos, Lifecycle lifecycle) {
        this.videoArrayList = videos;
        this.lifecycle = lifecycle;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layout, parent, false);
        lifecycle.addObserver(youTubePlayerView);
        return new ViewHolder(youTubePlayerView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.i("VIDEO BIND", "KEY " + videoArrayList.get(position).getKey() + " URL " + videoArrayList.get(position).getUrl());
        holder.cueVideo(videoArrayList.get(position).getKey());
    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }


    public void clearAndUpdateFiles(ArrayList<video> videos) {
        this.videoArrayList.clear();
        this.videoArrayList = videos;
        notifyDataSetChanged();
    }

    public void updateFiles(ArrayList<video> videos) {
        this.videoArrayList.addAll(videos);
        notifyDataSetChanged();
    }

    public String getYouTubeVideoKey(int pos) {
        return videoArrayList.get(pos).getKey();
    }


    public video getVideo(int position) {
        return videoArrayList.get(position);
    }


    /**
     * ViewHolder Class
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private YouTubePlayerView youTubePlayerView;
        private YouTubePlayer youTubePlayer;
        private String currentVideoId;


        ViewHolder(YouTubePlayerView playerView) {
            super(playerView);
            youTubePlayerView = playerView;
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer initializedYouTubePlayer) {
                    youTubePlayer = initializedYouTubePlayer;
                    youTubePlayer.cueVideo(currentVideoId, 0);
                }
            });
        }

        void cueVideo(String videoId) {
            currentVideoId = videoId;

            if (youTubePlayer == null)
                return;

            youTubePlayer.cueVideo(videoId, 0);
        }
    }


}
