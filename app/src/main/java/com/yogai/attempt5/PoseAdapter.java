package com.yogai.attempt5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PoseAdapter extends ArrayAdapter<Pose> {

    private final Context context;
    private final List<Pose> poseList;

    public PoseAdapter(Context context, List<Pose> poseList) {
        super(context, -1, poseList);
        this.context = context;
        this.poseList = poseList;
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
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.dd, parent, false);
        }
        Pose pose = getPose(position);

        TextView poseLabel = listitemView.findViewById(R.id.idPoseName);
        ImageView poseImage = listitemView.findViewById(R.id.idPoseImage);

        poseLabel.setText(pose.getPoseName());
        poseImage.setImageResource(pose.getImageResourceId());
        return listitemView;
    }
}
