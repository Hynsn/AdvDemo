package com.example.gson;

import android.os.Message;
import android.view.View;

import com.base.base.BaseActivity;
import com.example.R;
import com.example.databinding.ActivityCoroutineBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonActivity extends BaseActivity<ActivityCoroutineBinding> {
    @Override
    protected int getLayout() {
        return R.layout.activity_coroutine;
    }

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(TestBean.class,new TestTypeAdapter())
            .create();

    @Override
    protected void bindView() {

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        gsonTest1();
                    }
                }).start();
            }
        });
    }
    public void gsonTest1(){

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            TestBean bean = gson.fromJson(strsb,TestBean.class);
            bean.recycle();
            //System.out.println(bean);
        }
        System.out.println("gsonTest: "+(System.currentTimeMillis()-start));
    }
    public void gsonTest2(){
        Gson gson = new Gson();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            TestBean bean = gson.fromJson(strsb,TestBean.class);
            //System.out.println(bean);
        }
        System.out.println("gsonTest: "+(System.currentTimeMillis()-start));
    }

    final String strsb = "{\"title\":\"json在线解析（简版） -JSON在线解析\",\"url\":\"https://www.sojson.com/simple_json.html\",\"keywords\":\"json在线解析\"}";
}
