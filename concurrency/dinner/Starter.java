package concurrency.dinner;

import concurrency.dinner.philosopher.*;

public class Starter {

    public static void main(String[] args) {

        //デッドロックになる
//        Diners.create(DeadlockPhilosopher.class).start();

        //Philosopherのidが偶数なら左のフォークから,奇数なら右のフォークから
//        Diners.create(EvenOddPhilosopher.class).start();

        //フォークを取れるのは最大4人まで
//        Diners.create(Max4Philosopher.class).start();

        //原子的に左右のフォークを取る
//        Diners.create(AtomicPhilosopher.class).start();

        //制限時間内にもう一つのフォーク獲得できなかったら、現在持っているフォークを一旦置く
//        Diners.create(TimeoutPhilosopher.class).start();

        //左右のフォークのうち,　value値の大きいフォークから取る
        Diners.create(ForkValuePhilosopher.class).start();
    }
}
