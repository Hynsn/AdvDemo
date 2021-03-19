package com.example.threadlocal;

import com.example.cachepool.CachePool;

import org.junit.Test;

import java.util.Arrays;

public class ThreadLocalTest {
    class Bank {
        private ThreadLocal<Integer> account = new ThreadLocal<Integer>() {
            protected Integer initialValue() {
                return 100;
            };
        };
        public int getAccount() {
            return account.get();
        }

        /**
         * 用同步方法实现
         *
         * @param money
         */

        public void save(int money) {
            account.set(account.get()+money);
        }
    }
    class NewThread implements Runnable {
        private Bank bank;
        public NewThread(Bank bank) {
            this.bank = bank;
        }
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                bank.save(10);
                System.out.println(Thread.currentThread().getName() + ":" + i + "账户余额为：" + bank.getAccount());
            }
            System.out.print('\n');
        }
    }



    /**
     * 建立线程，调用内部类
     */

    public void useThread() {

        Bank bank = new Bank();

        NewThread new_thread = new NewThread(bank);
        Thread thread1 = new Thread(new_thread , "线程1");
        thread1.start();

        Thread thread3 = new Thread(new_thread , "线程3");
        thread3.start();

        Thread thread2 = new Thread(new_thread , "线程2");
        thread2.start();
    }

    @Test
    public void test() {
        ThreadLocalTest st = new ThreadLocalTest();
        st.useThread();
    }
    @Test
    public void binarySearchTest(){
        int[] arry = {1,4,6,8,10};
        int ret = Arrays.binarySearch(arry,5);
        System.out.println("ret: "+ret);


        Rect[] d = new Rect[5];
        for (int i = 0; i < d.length; i++) {
            d[i] = new Rect(i*10,i*10);
        }
        for (int i = 0; i < d.length; i++) {
            System.out.println("d: "+d[i].toString());
        }
    }

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
