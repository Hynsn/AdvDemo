package com.hynson.databinding;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.fastdroid.base.BaseActivity;
import com.hynson.R;
import com.hynson.databinding.entity.NoteInfo;

import java.util.List;

public class DBNoteActivity extends BaseActivity<ActivityDataBindingBinding> {
    final static String TAG = DBNoteActivity.class.getSimpleName();
    NoteAdapter noteAdapter;
    List<NoteInfo> ynoteInfoList;

    @Override
    protected int getLayout() {
        return R.layout.activity_data_binding;
    }

    @Override
    protected void bindView() {
        if(noteAdapter==null) {
            noteAdapter = new NoteAdapter(ynoteInfoList);
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        binding.notelistRv.setLayoutManager(manager);
        binding.notelistRv.setAdapter(noteAdapter);
    }

    public void addNote(View v){
        switch (v.getId()){
            case R.id.fbtn_add:
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final NoteDatabase db = Room.databaseBuilder(getApplicationContext(), NoteDatabase.class, "database-name").build();

                        db.ynoteInfoDao().insertAll(new NoteInfo("20190907","20170924","开发日记","简介","内容"));
                        db.ynoteInfoDao().insertAll(new NoteInfo("20190907","20170924","开发日记","简介","内容"));

                        final List<NoteInfo> infos = db.ynoteInfoDao().getAll();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                noteAdapter.setDataSet(infos);
                            }
                        });

//                ynoteInfoList = db.ynoteInfoDao().getAll();
//                for (int i = 0; i < ynoteInfoList.size(); i++) {
//                    Log.i(TAG, "ynoteInfoList: "+ynoteInfoList.get(i).toString());
//                }

                    }
                }).start();
            }
                break;
        }
    }
}
