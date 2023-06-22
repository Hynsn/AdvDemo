package com.hynson.ble;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hynson.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A on 2017/7/4.
 */

public class LogAdapter extends BaseAdapter{

    private List<String> tagList;
    private List<String> contentList;

    private Context context;

    public LogAdapter(Context context){
        this.context = context;
        tagList = new ArrayList<>();
        contentList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return tagList.size();
    }

    @Override
    public Object getItem(int i) {
        return tagList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void addLog(String tag,String content){
        tagList.add(tag);
        contentList.add(content);
        this.notifyDataSetChanged();
    }

    public void clearLog(){
        tagList.clear();
        contentList.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(convertView == null){//如果还没有创建
            convertView = View.inflate(context, R.layout.ble_log_item,null);
            viewHolder = new ViewHolder();
            viewHolder.tag = (TextView) convertView.findViewById(R.id.tag);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(viewHolder);
        }else{//如果已经创建了，则复用
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tag.setText(tagList.get(i));
        viewHolder.content.setText(contentList.get(i));
        return convertView;
    }

    class ViewHolder{
        public TextView tag;
        public TextView content;
    }
}
