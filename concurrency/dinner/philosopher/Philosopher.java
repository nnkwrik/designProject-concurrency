package concurrency.dinner.philosopher;

import concurrency.dinner.Diners;
import concurrency.dinner.Fork;
import concurrency.dinner.PhilCanvas;

public abstract class Philosopher extends Thread {

    protected int identity;
    protected PhilCanvas view;
    protected Diners controller;
    protected Fork left;
    protected Fork right;

    public Philosopher(Diners controller, int identity, Fork left, Fork right) {
        this.controller = controller;
        this.view = controller.display;
        this.identity = identity;
        this.left = left;
        this.right = right;
    }

    @Override
    public abstract void run();
}
