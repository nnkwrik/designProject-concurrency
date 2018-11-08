package concurrency.dinner.philosopher;

import concurrency.dinner.swing.Diners;
import concurrency.dinner.Fork;
import concurrency.utils.Logger;

public class DeadlockPhilosopher extends Philosopher {

    public DeadlockPhilosopher(Diners controller, int identity, Fork left, Fork right) {
        super(controller,identity,left,right);

    }


    public void run() {
        try {
            while (true) {
                //thinking
                view.setPhil(identity, view.THINKING);
                sleep(controller.sleepTime());
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
            }
        } catch (InterruptedException e) {
        }
    }
}
