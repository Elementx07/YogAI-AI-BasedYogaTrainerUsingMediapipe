package com.yogai.attempt5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PoseAdapter extends ArrayAdapter<Pose> {

    private final Context context;
    private final List<Pose> poseList;
    int type;

    public PoseAdapter(Context context, List<Pose> poseList, int type) {
        super(context, -1, poseList);
        this.context = context;
        this.poseList = poseList;
        this.type=type;
    }

    @Override
    public int getCount() {
        return poseList.size();
    }

    @Override
    public Pose getItem(int position) {
        return poseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Pose getPose(int position) {
        return poseList.get(position);
    }

    public String getPoseName(int position) {
        return poseList.get(position).getPoseName();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        Pose pose = getPose(position);
        if (listitemView == null) {
            if(type==1)
            // Layout Inflater inflates each item to be displayed in GridView.
                listitemView = LayoutInflater.from(getContext()).inflate(R.layout.itemcard, parent, false);
            else if (type==2)
                listitemView = LayoutInflater.from(getContext()).inflate(R.layout.progress_card, parent, false);
        }
        Animation anime= AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_right);
        AnimationSet aset =new AnimationSet(true);
        anime.setStartOffset(position*200);
        aset.addAnimation(anime);
        listitemView.startAnimation(aset);


        TextView poseLabel = listitemView.findViewById(R.id.idPoseName);
        ImageView poseImage = listitemView.findViewById(R.id.idPoseImage);

        poseLabel.setText(pose.getPoseName());
        poseImage.setImageResource(pose.getImageResourceId());
        return listitemView;
    }
}
