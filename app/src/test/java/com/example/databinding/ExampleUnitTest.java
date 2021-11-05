package com.example.databinding;

import com.example.codeorder.A;
import com.example.codeorder.B;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void test(){
        B t = new B();
        A a = t;
        System.out.println("B:"+System.identityHashCode(t)+'\n');
        System.out.println("A:"+System.identityHashCode(a)+'\n');

        a.find();
    }

    @Test
    public void deathLock() {
        MyThread t = new MyThread();
        new Thread(t).start();
        new Thread(t).start();
    }

    @Test
    public void syncLock(){
        Runnable run = new SynchronizedYesAndNo();
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);
        t1.start();
        t2.start();
        while (t1.isAlive() || t2.isAlive()) {

        }
        System.out.println("finished");
    }
}

class SynchronizedYesAndNo implements Runnable {

    @Override
    public void run() {
        if(Thread.currentThread().getName().equals("Thread-0")){
            method1();
        }else {
            method2();
        }
    }

    public synchronized void method1() {
        System.out.println("我是静态加锁的方法1，我叫：" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "运行结束。");

    }

    public synchronized void method2() {
        System.out.println("我是非静态加锁的方法2，我叫：" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "运行结束。");

    }
}

class MyThread implements Runnable {
    Object o1 = new Object();
    Object o2 = new Object();

    @Override
    public void run() {
        synchronized (o1) {
            System.out.println("我叫：" + Thread.currentThread().getName());
            synchronized (o2) {
                System.out.println(Thread.currentThread().getName() + "运行中1。");

                /*try {
                    Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + "运行中2。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
            System.out.println(Thread.currentThread().getName() + "运行结束。");

            /*synchronized (o2) {
                System.out.println("我叫：" + Thread.currentThread().getName());
                synchronized (o1) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + "运行结束。");
            }*/
        }
    }
}