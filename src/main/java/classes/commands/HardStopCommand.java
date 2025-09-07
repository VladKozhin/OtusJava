package classes.commands;

import classes.CommandExecutor;
import interfaces.ICommand;

/**
 * Жёстко прерывает работу потока, не дожидаясь окончания выполнения оставшихся в очереди команд
 */
public class HardStopCommand implements ICommand {

    private final CommandExecutor executor;
    private final Thread thread;

    public HardStopCommand(CommandExecutor executor, Thread thread) {
        this.executor = executor;
        this.thread = thread;
    }

    @Override
    public void execute() {
        executor.hardStop();
        thread.interrupt();
        System.out.println("Инициирована жёсткая остановка потока " + thread.getName());
    }
}