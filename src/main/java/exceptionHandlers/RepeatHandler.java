package exceptionHandlers;

import interfaces.IExceptionHandler;

public class RepeatHandler implements IExceptionHandler {
    private Runnable func;
    private int count;

    public RepeatHandler(Runnable func, int count) {
        this.func = func;
        this.count = count;
    }

    @Override
    public boolean handle(Object error, Runnable command) {
        if (this.func == null) {
            return false;
        }

        int attempts = 0;
        while (attempts < this.count) {
            System.out.println("Повтор №" + (attempts + 1) + "...");
            try {
                this.func.run();
                return true; // успешно выполнено
            } catch (Exception e) {
                attempts++;
                if (attempts >= this.count) {
                    return false; // исчерпали попытки
                }
            }
        }
        return false; // на всякий случай
    }
}