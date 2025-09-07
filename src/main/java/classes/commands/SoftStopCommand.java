package classes.commands;

import classes.CommandExecutor;
import interfaces.ICommand;

/**
 * Мягко прерывает работу потока, ожидая окончание выполнения оставшихся в очереди команд
 */

public class SoftStopCommand implements ICommand {

    private final CommandExecutor executor;
    private final Thread thread;

    public SoftStopCommand(CommandExecutor executor, Thread thread) {
        this.executor = executor;
        this.thread = thread;
    }

    @Override
    public void execute() {
        executor.softStop();
        Thread.currentThread().interrupt();
        try {
            thread.join(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Инициирована мягкая остановка потока " + this.thread.getName());
    }
}