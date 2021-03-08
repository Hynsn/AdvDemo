package com.example.threadlocal;

import org.junit.Test;

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
}
