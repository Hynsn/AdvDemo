package com.hynson.objpool;

import com.hynson.threadlocal.Rect;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

/**
 * Author: Hynsonhou
 * Date: 2021/11/5 16:37
 * Description: 对象池测试类
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2021/11/5   1.0       首次创建
 */
public class ObjPoolTest {
    @Test
    public void cachePoolTest(){
        CachePool.SimpleCachePool<Rect> cachePool = new CachePool.SimpleCachePool<>(10);
        Rect rect = cachePool.obtain();
        if(rect==null){
            rect = new Rect(10,10);
        }
        cachePool.recycle(rect);
    }
    @Test
    public void gsonTest(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(TestBean.class,new TestTypeAdapter())
                .create();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            TestBean bean = gson.fromJson("{\"title\":\"json在线解析（简版） -JSON在线解析\",\"url\":\"https://www.sojson.com/simple_json.html\",\"keywords\":\"json在线解析\"}",TestBean.class);
            //System.out.println(bean);
        }
        System.out.println("gsonTest: "+(System.currentTimeMillis()-start));
    }
    @Test
    public void gsonTest2(){
        Gson gson = new Gson();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            TestBean bean = gson.fromJson("{\"title\":\"json在线解析（简版） -JSON在线解析\",\"url\":\"https://www.sojson.com/simple_json.html\",\"keywords\":\"json在线解析\"}",TestBean.class);
            //System.out.println(bean);
        }
        System.out.println("gsonTest: "+(System.currentTimeMillis()-start));
    }
}
