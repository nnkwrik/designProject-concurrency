package concurrency.dinner.philosopher;

import concurrency.dinner.Diners;
import concurrency.dinner.Fork;
import concurrency.utils.Logger;
import concurrency.utils.Semaphore;

public class Max4Philosopher extends Philosopher {

    private static Semaphore semaphore  = new Semaphore(4);

    public Max4Philosopher(Diners controller, int identity, Fork left, Fork right) {
        super(controller, identity, left, right);
    }

    public void run() {
        try {
            while (true) {
                //thinking
                view.setPhil(identity, view.THINKING);
                sleep(controller.sleepTime());

                semaphore.down();
                //hungry
                view.setPhil(identity, view.HUNGRY);
                right.get();
                //gotright chopstick
                view.setPhil(identity, view.GOTRIGHT);
                sleep(500);
                left.get();
                //eating
                Logger.logout("EATING");
                view.setPhil(identity, view.EATING);
                sleep(controller.eatTime());
                right.put();
                left.put();
                semaphore.up();

            }
        } catch (InterruptedException e) {
        }
    }
}
