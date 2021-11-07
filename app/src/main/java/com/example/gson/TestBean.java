package com.example.gson;

import androidx.core.util.Pools;

public class TestBean {
    String title;
    String url;
    String keywords;

    //声明对象池的大小
    private static final Pools.SynchronizedPool<TestBean> sPool =
            new Pools.SynchronizedPool<TestBean>(10);

    //从对象池中获取数据，如果为null,则创建
    public static TestBean obtain() {
        TestBean instance = sPool.acquire();
        return (instance != null) ? instance : new TestBean();
    }

    //回收对象到对象池中。当然你也可以清除对象的状态
    public void recycle() {
        // 清除对象的状态，如果你自己需要的话，
        sPool.release(this);
    }
}
