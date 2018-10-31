package concurrency.dinner;

class FixedPhilosopher extends Thread {

    int identity;
    boolean stopRequested = false;
    PhilCanvas view;
    Diners controller;
    Fork left;
    Fork right;

    FixedPhilosopher(Diners controller, int identity, Fork left, Fork right) {
        this.controller = controller;
        this.view = controller.display;
        this.identity = identity;
        this.left = left;
        this.right = right;
    }

    public void run() {
        while (!stopRequested) {
             try {
                //thinking
                view.setPhil(identity,view.THINKING);
                sleep(controller.sleepTime());
                //hungry
                view.setPhil(identity,view.HUNGRY);
                //get forks
                if (identity%2 == 0) {
                    left.get();
                    view.setPhil(identity,view.GOTLEFT);
                } else {
                    right.get();
                    view.setPhil(identity,view.GOTRIGHT);
                }
                sleep(500);
                if (identity%2 == 0)
                    right.get();
                else
                    left.get();
                //eating
                view.setPhil(identity,view.EATING);
                sleep(controller.eatTime());
                right.put();
                left.put();
             } catch (InterruptedException e) {}
        }
    }


    public void stopRequested() {
        stopRequested = true;
    }
}
