package concurrency.dinner.philosopher;

import concurrency.dinner.swing.Diners;
import concurrency.dinner.Fork;
import concurrency.utils.Logger;

/**
 * this Philosopher will make program to deadlock
 */
public class EvenOddPhilosopher extends Philosopher {

    public EvenOddPhilosopher(Diners controller, int identity, Fork left, Fork right) {
        super(controller,identity,left,right);
    }

    public void run() {
        while (true) {
            try {
                //thinking
                view.setPhil(identity, view.THINKING);
                sleep(controller.sleepTime());
                //hungry
                view.setPhil(identity, view.HUNGRY);
                //get forks
                if (identity % 2 == 0) {
                    left.get();
                    view.setPhil(identity, view.GOTLEFT);
                } else {
                    right.get();
                    view.setPhil(identity, view.GOTRIGHT);
                }
                sleep(500);
                if (identity % 2 == 0)
                    right.get();
                else
                    left.get();
                //eating
                view.setPhil(identity, view.EATING);
                Logger.logout("EATING");
                sleep(controller.eatTime());
                right.put();
                left.put();
            } catch (InterruptedException e) {
            }
        }
    }

}
