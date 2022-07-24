package com.hynson.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hynson.R;
import com.hynson.databinding.entity.NoteInfo;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    private List<NoteInfo> mDeviceList;

    public NoteAdapter(List<NoteInfo> list) {
        if(mDeviceList==null)
            mDeviceList = new ArrayList<>();

        if(list!=null)
            mDeviceList.addAll(list);
    }

    public void setDataSet(List<NoteInfo> list){
        if(list!=null) {
            mDeviceList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        NoteInfo info = mDeviceList.get(position);

        holder.nameTV.setText(info.noteName);
        holder.updateTimeTV.setText(info.updateTime);
        holder.introTV.setText(info.noteIntro);
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameTV,introTV,updateTimeTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.noteitem_name_tv);
            introTV = itemView.findViewById(R.id.noteitem_intro_tv);
            updateTimeTV = itemView.findViewById(R.id.noteitem_time_tv);
        }
    }
}
