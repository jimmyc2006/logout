package juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程交替输出
 */
public class LockDemoThreeTurn {

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        Condition condition3 = lock.newCondition();

        new Thread(() -> {
            for (char i = 'a'; i <= 'z'; i++) {
                try {
                    lock.lock();
                    condition2.await();
                    System.out.println(Thread.currentThread().getName() + " -> " + i);
                    condition3.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }, "t2").start();

        new Thread(() -> {
            for (int i = 0; i < 26; i++) {
                try {
                    lock.lock();
                    condition3.await();
                    System.out.println(Thread.currentThread().getName() + " -> " + i);
                    condition1.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }, "t3").start();

        new Thread(() -> {
            for (char i = 'A'; i <= 'Z'; i++) {
                try {
                    lock.lock();
                    condition1.await();
                    System.out.println(Thread.currentThread().getName() + " -> " + i);
                    condition2.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }, "t1").start();

        Thread.sleep(1000 * 1);
        try {
            lock.lock();
            condition2.signal();
        } finally {
            lock.unlock();
        }
    }
}
