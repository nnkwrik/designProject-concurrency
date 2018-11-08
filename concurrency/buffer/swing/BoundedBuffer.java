/*
@author  j.n.magee 
//updated: daniel.sykes 2013
//updated: kozo okano 2015
*/
package concurrency.buffer.swing;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import concurrency.buffer.Buffer;
import concurrency.buffer.BufferEnum;
import concurrency.buffer.Consumer;
import concurrency.buffer.Producer;
import concurrency.buffer.badSema.DisplaySemaBuffer;
import concurrency.buffer.blockingQueue.DisplayBlockingQueueBuffer;
import concurrency.buffer.fixedSema.DisplayFixedSemaBuffer;
import concurrency.buffer.lock.DisplayLockBuffer;
import concurrency.buffer.waitnotifyAll.DisplayWaitBuffer;
import concurrency.buffer.workStealing.DisplayWorkStealingBuffer;
import concurrency.buffer.workStealing.WorkStealingBuffer;
import concurrency.buffer.workStealing.WorkStealingConsumer;
import concurrency.display.*;

import javax.swing.*;

public class BoundedBuffer {


    private BufferCanvas buffDisplay;
    private List<ThreadPanel> producerList;
    private List<ThreadPanel> consumerList;

    public static final int CONSUMER = 1;
    public static final int PRODUCER = 1;
    public static final int SLOT = 5;

    private Buffer buffer;
    private BufferEnum bufferEnum;

    private BoundedBuffer() {
    }


    private JComponent createComponents(int consumerNum, int producerNum, int slotNum) {
        JPanel panel = new JPanel();

        // Set up Display
        producerList = IntStream.range(0, producerNum)
                .mapToObj(i -> new ThreadPanel("Producer" + i, Color.blue))
                .collect(Collectors.toCollection(ArrayList::new));

        consumerList = IntStream.range(0, consumerNum)
                .mapToObj(i -> new ThreadPanel("Consumer" + i, Color.yellow))
                .collect(Collectors.toCollection(ArrayList::new));

        buffDisplay = new BufferCanvas("Buffer", slotNum);
        GridBagLayout gridbag = new GridBagLayout();
        panel.setLayout(gridbag);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTH;
        gridbag.setConstraints(buffDisplay, gc);

        producerList.stream().forEach(prod -> {
            gridbag.setConstraints(prod, gc);
            panel.add(prod);

        });
        panel.add(buffDisplay);
        consumerList.stream().forEach(cons -> {
            gridbag.setConstraints(cons, gc);
            panel.add(cons);
        });


        panel.setBackground(Color.lightGray);
        return panel;
    }


    public static BoundedBuffer create(BufferEnum bufferEnum) {
        return create(bufferEnum, CONSUMER, PRODUCER, SLOT);
    }

    public static BoundedBuffer create(BufferEnum bufferEnum, int consumerSize, int producerSize) {
        return create(bufferEnum, consumerSize, producerSize, SLOT);
    }

    public static BoundedBuffer create(BufferEnum bufferEnum, int consumerSize, int producerSize, int slot) {


        BoundedBuffer app = new BoundedBuffer();
        JFrame frame = new JFrame("BoundedBuffer");
        JComponent contents = app.createComponents(consumerSize, producerSize, slot);
        frame.getContentPane().add(contents);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        app.bufferEnum = bufferEnum;
        switch (bufferEnum) {
            case WAIT_NOTIFYALL:
                app.buffer = new DisplayWaitBuffer(app.buffDisplay, slot);
                break;
            case BAD_SEMAPHORE:
                app.buffer = new DisplaySemaBuffer(app.buffDisplay, slot);
                break;
            case FIXED_SEMAPHORE:
                app.buffer = new DisplayFixedSemaBuffer(app.buffDisplay, slot);
                break;
            case LOCK:
                app.buffer = new DisplayLockBuffer(app.buffDisplay, slot);
                break;
            case BLOCKING_QUEUE:
                app.buffer = new DisplayBlockingQueueBuffer(app.buffDisplay, slot);
                break;
            case WORK_STEALING:
                app.buffer = new DisplayWorkStealingBuffer(app.buffDisplay, slot, consumerSize);
                break;
        }


        return app;
    }


    public void start() {
        switch (bufferEnum) {
            case WORK_STEALING:
                workStealingStart();
                break;
            default:
                defaultStart();
                break;
        }
    }

    private void defaultStart(){
        producerList.stream()
                .forEach(prod -> prod.start(new Producer(buffer)));
        consumerList.stream()
                .forEach(prod -> prod.start(new Consumer(buffer)));
    }

    private void workStealingStart(){
        DisplayWorkStealingBuffer workStealingBuffer = (DisplayWorkStealingBuffer) buffer;
        producerList.stream()
                .forEach(prod -> prod.start(new Producer(workStealingBuffer)));
        IntStream.range(0, consumerList.size())
                .forEach(i -> consumerList.get(i).start(new WorkStealingConsumer(workStealingBuffer, i)));
    }

    public void stop() {
        producerList.stream()
                .forEach(prod -> prod.stop());
        consumerList.stream()
                .forEach(prod -> prod.stop());
    }
}
