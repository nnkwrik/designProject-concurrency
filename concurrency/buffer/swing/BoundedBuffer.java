/*
@author  j.n.magee 
//updated: daniel.sykes 2013
//updated: kozo okano 2015
*/
package concurrency.buffer.swing;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import concurrency.buffer.Buffer;
import concurrency.buffer.Consumer;
import concurrency.buffer.Producer;
import concurrency.buffer.workStealing.WorkStealingBuffer;
import concurrency.buffer.workStealing.WorkStealingConsumer;
import concurrency.display.*;

import javax.swing.*;

public class BoundedBuffer {

    //    public ThreadPanel prod;
//    public ThreadPanel cons;
    public BufferCanvas buffDisplay;
    public List<ThreadPanel> producerList;
    public List<ThreadPanel> consumerList;

    public static final int CONSUMER = 1;
    public static final int PRODUCER = 1;
    public static final int SLOT = 5;


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


    public static BoundedBuffer create() {
        return create(CONSUMER,PRODUCER,SLOT);
    }

    public static BoundedBuffer create(int consumerNum, int producerNum) {
        return create(consumerNum,producerNum,SLOT);
    }

    public static BoundedBuffer create(int consumerNum, int producerNum, int slot) {
        BoundedBuffer boundedBuffer = new BoundedBuffer();
        JFrame frame = new JFrame("BoundedBuffer");
        JComponent contents = boundedBuffer.createComponents(consumerNum, producerNum, slot);
        frame.getContentPane().add(contents);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        return boundedBuffer;
    }

    public BufferCanvas getBuffDisplay() {
        return buffDisplay;
    }

    public void start(Buffer<Character> buffer) {
        producerList.stream()
                .forEach(prod -> prod.start(new Producer(buffer)));
        consumerList.stream()
                .forEach(prod -> prod.start(new Consumer(buffer)));
    }

    public void startWorkStealing(WorkStealingBuffer<Character> buffer) {
        producerList.stream()
                .forEach(prod -> prod.start(new Producer(buffer)));
        IntStream.range(0, consumerList.size())
                .forEach(i -> consumerList.get(i).start(new WorkStealingConsumer(buffer,i)));
    }

    public void stop() {
        producerList.stream()
                .forEach(prod -> prod.stop());
        consumerList.stream()
                .forEach(prod -> prod.stop());
    }
}
