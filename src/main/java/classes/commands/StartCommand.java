package classes.commands;

import classes.CommandExecutor;
import interfaces.ICommand;

/**
 * Запускает поток
 */
public class StartCommand implements ICommand {

    private final CommandExecutor executor;
    private Thread thread;

    public StartCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute() {
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(executor, "CommandExecutorThread");
            thread.start();
            System.out.println("CommandExecutor запущен и готов к работе.");
        }
    }

    public Thread getThread() {
        return thread;
    }
}