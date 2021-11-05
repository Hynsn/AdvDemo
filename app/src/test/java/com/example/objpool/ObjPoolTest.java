package com.example.objpool;

import com.example.threadlocal.Rect;

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
}
