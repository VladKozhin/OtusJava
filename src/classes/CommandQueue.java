package classes;

import java.util.LinkedList;
import java.util.Queue;

public class CommandQueue {
    private Queue<Runnable> queue = new LinkedList<>();

    // Добавление команды в очередь
    public void enqueue(Runnable command) {
        queue.offer(command);
    }

    // Последовательное выполнение всех команд
    public void executeAll() {
        while (!queue.isEmpty()) {
            Runnable command = queue.poll();
            try {
                command.run();
            } catch (Exception e) {
                System.out.println("Ошибка при выполнении команды: " + e.getMessage());
            }
        }
    }
}