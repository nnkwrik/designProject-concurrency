package concurrency.dinner.philosopher;

import concurrency.dinner.swing.Diners;
import concurrency.dinner.Fork;
import concurrency.utils.Logger;

public class ForkValuePhilosopher extends Philosopher {

    boolean leftFirst;

    public ForkValuePhilosopher(Diners controller, int identity, Fork left, Fork right) {
        super(controller, identity, left, right);
        Fork.shuffleValue(left,right);
        leftFirst = left.getValue() > right.getValue() ? true : false;
        System.out.println(("LEFT FIRST? " + leftFirst));
    }


    public void run() {
        try {
            while (true) {

                //thinking
                view.setPhil(identity, view.THINKING);
                sleep(controller.sleepTime());
                //hungry
                view.setPhil(identity, view.HUNGRY);
                //get forks
                if (leftFirst) {
                    left.get();
                    view.setPhil(identity, view.GOTLEFT);
                } else {
                    right.get();
                    view.setPhil(identity, view.GOTRIGHT);
                }

                sleep(500);

                if (leftFirst)
                    right.get();
                else
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
