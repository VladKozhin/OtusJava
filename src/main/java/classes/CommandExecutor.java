package classes;

import interfaces.ICommand;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandExecutor implements Runnable {

    private final BlockingQueue<ICommand> queue = new LinkedBlockingQueue<>();

    //флаг, показывающий, что поток запущен; если значение равно false, то поток будет мягко остановлен
    private final AtomicBoolean running = new AtomicBoolean(false);

    //флаг, показывающий, что поток будет жёстко остановлен
    private final AtomicBoolean hardStop = new AtomicBoolean(false);

    public void addCommand(ICommand command) {
        if(!running.get()){
            throw new IllegalStateException("CommandExecutor находится в состоянии остановки и не принимает команды в очередь");
        }
        queue.offer(command);
    }

    /**
     * Жёстко прерываем поток
     */
    public void hardStop() {
        hardStop.set(true);
    }

    /**
     * Мягкая остановка потока
     */
    public void softStop() {
        running.set(false);
    }

    @Override
    public void run() {
        //Устанавливаю флаг
        running.set(true);
        while (running.get() && !hardStop.get()) {
            try {
                ICommand command = queue.take();
                try {
                    command.execute();
                } catch (Exception e) {
                    //сюда бы ExceptionHandler прикрутить
                    System.err.println("Exception in command: " + e.getMessage());
                }
            } catch (InterruptedException e) {
                // логи
                if (hardStop.get()) {
                    System.out.println("Hard stop.");
                    break;
                }
                if (!running.get()) {
                    if (queue.isEmpty()) {
                        System.out.println("Soft stop: successful.");
                        break;
                    } else {
                        System.out.println("Soft stop: awaiting...");
                    }
                }
            }
        }
        Thread.currentThread().interrupt();
        System.out.println("CommandExecutor остановлен.");
    }
}