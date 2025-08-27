package exceptionHandlers;

import classes.CommandQueue;
import interfaces.IExceptionHandler;

public class EnqueueCommandHandler implements IExceptionHandler {
    private int counter = 0;
    private CommandQueue queue;

    public EnqueueCommandHandler(CommandQueue queue) {
        this.queue = queue;
    }

    @Override
    public boolean handle(Object error, Runnable command) {
        if (command != null && this.counter == 0) {
            System.out.println("Повторная постановка в очередь.");
            this.queue.enqueue(command);
            this.counter++;
            return true;
        }
        if (this.counter > 0) {
            this.counter = 0;
        }
        return false;
    }
}