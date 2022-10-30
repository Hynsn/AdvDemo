package com.hynson.gson;

import android.view.View;

import com.base.base.BaseActivity;
import com.hynson.R;
import com.hynson.databinding.ActivityCoroutineBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class GsonActivity extends BaseActivity<ActivityCoroutineBinding> implements View.OnClickListener {
    @Override
    protected int getLayout() {
        return R.layout.activity_coroutine;
    }

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(TestBean.class, new TestTypeAdapter())
            .create();

    @Override
    protected void bindView() {

        binding.btnCoroutine.setOnClickListener(this);
        binding.btnRxjava.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_rxjava: {
                rxjavaSingleTest();
            }
            break;
            case R.id.btn_coroutine:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        gsonTest1();
                    }
                }).start();
                break;
        }
    }

    public void gsonTest1() {

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            TestBean bean = gson.fromJson(strsb, TestBean.class);
            bean.recycle();
            //System.out.println(bean);
        }
        System.out.println("gsonTest: " + (System.currentTimeMillis() - start));
    }

    public void gsonTest2() {
        Gson gson = new Gson();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            TestBean bean = gson.fromJson(strsb, TestBean.class);
            //System.out.println(bean);
        }
        System.out.println("gsonTest: " + (System.currentTimeMillis() - start));
    }

    final String strsb = "{\"title\":\"json在线解析（简版） -JSON在线解析\",\"url\":\"https://www.sojson.com/simple_json.html\",\"keywords\":\"json在线解析\"}";

    private void rxjavaSingleTest() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < 3; i++) {
                            System.out.println("发射线程:" + Thread.currentThread().getName() + "---->" + "发射:" + i);
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.single())//设置可观察对象在Schedulers.single()的线程中发射数据
                .observeOn(Schedulers.single())//指定map操作符在Schedulers.single()的线程中处理数据
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer i) throws Exception {
                        System.out.println("处理线程:" + Thread.currentThread().getName() + "---->" + "处理:" + i);
                        return i;
                    }
                })
                .observeOn(Schedulers.single())//设置观察者在Schedulers.single()的线程中j接收数据
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer i) throws Exception {
                        System.out.println("接收线程:" + Thread.currentThread().getName() + "---->" + "接收:" + i);
                    }
                }).isDisposed();

    }
}
