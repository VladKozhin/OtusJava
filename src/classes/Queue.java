package classes;

import interfaces.ICommand;

import java.util.Stack;

public class Queue {
    private Stack<ICommand> commands = new Stack<>();

    public void enqueue(ICommand command) {
        commands.push(command);
    }

    public void execute() {
        while (!commands.isEmpty()) {
            ICommand cmd = commands.pop();
            if (cmd instanceof Queue) {
                ((Queue) cmd).execute();
            } else {
                cmd.execute();
            }
        }
    }

    public int count() {
        return commands.size();
    }

    public ICommand getCurrentFunc() {
        if (!commands.isEmpty()) {
            return commands.pop();
        }
        return null;
    }
}