package concurrency.dinner;

import concurrency.dinner.philosopher.DeadlockPhilosopher;
import concurrency.dinner.philosopher.FixedPhilosopher;

public class Starter {

    public static void main(String[] args) {
//        Diners.create(DeadlockPhilosopher.class).start();
        Diners.create(FixedPhilosopher.class).start();
    }
}
