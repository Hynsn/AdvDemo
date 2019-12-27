package com.example.databinding;

import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.databinding.databinding.LoginBinding;
import com.example.databinding.entity.UserInfo;
import com.example.databinding.entity.YnoteInfo;

import java.util.List;

public class MainActivity extends FragmentActivity {
    final static String TAG = "Main";

    private UserInfo user;
    private LoginBinding binding;
    private Time mTime;


    RecyclerView noteListRV;
    NoteAdapter noteAdapter;
    List<YnoteInfo> ynoteInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        noteListRV = findViewById(R.id.main_notelist_rv);

        if(noteAdapter==null) {
            noteAdapter = new NoteAdapter(ynoteInfoList);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                NoteDatabase db = Room.databaseBuilder(getApplicationContext(), NoteDatabase.class, "database-name").build();

                db.ynoteInfoDao().insertAll(new YnoteInfo("20190907","20170924","开发日记","简介","内容"));
                db.ynoteInfoDao().insertAll(new YnoteInfo("20190907","20170924","开发日记","简介","内容"));

                noteAdapter.setDataSet(db.ynoteInfoDao().getAll());
//                ynoteInfoList = db.ynoteInfoDao().getAll();
//                for (int i = 0; i < ynoteInfoList.size(); i++) {
//                    Log.i(TAG, "ynoteInfoList: "+ynoteInfoList.get(i).toString());
//                }

            }
        }).start();


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        noteListRV.setLayoutManager(manager);
        noteListRV.setAdapter(noteAdapter);
/*        mTime = new Time();
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.login);
        user = new UserInfo("","");
        binding.setUser(user);
        binding.setActivity(this);


        */

    }

    public void userLogin(View v){
        mTime.setToNow();
        int year = mTime.year;
        int month = mTime.month+1;
        int day = mTime.monthDay;
        int hour = mTime.hour;
        int minute = mTime.minute;
        // 双向绑定
        Log.d(TAG, "userLogin: "+user.getName()+","+user.getPwd());
        Toast.makeText(getApplicationContext(),user.getName()+"登录中",Toast.LENGTH_SHORT).show();
        // 单向绑定
        user.setLoginTime("上次登录时间:"+year+"年"+month+"月"+day+"日"+hour+":"+minute);
    }
}
