package concurrency.dinner.philosopher;

import concurrency.dinner.Diners;
import concurrency.dinner.Fork;
import concurrency.utils.Logger;

import java.util.concurrent.TimeUnit;

public class TimeoutPhilosopher extends Philosopher {

    public TimeoutPhilosopher(Diners controller, int identity, Fork left, Fork right) {
        super(controller, identity, left, right);

    }


    public void run() {
        try {
            while (true) {

                //thinking
                view.setPhil(identity, view.THINKING);
                sleep( controller.sleepTime());
                //hungry
                view.setPhil(identity, view.HUNGRY);
                right.get();
                //gotright chopstick
                view.setPhil(identity, view.GOTRIGHT);
                sleep(500);

                if (!left.get( controller.sleepTime(), TimeUnit.MILLISECONDS)){
                    //timeout
                    right.put();
                    continue;
                }
                //eating
                //if take slider to left,almost can't EATING .but it's not deadlock
                Logger.logout("EATING");
                view.setPhil(identity, view.EATING);
                sleep(controller.eatTime());
                right.put();
                left.put();
            }
        } catch (InterruptedException e) {
        }
    }
}
