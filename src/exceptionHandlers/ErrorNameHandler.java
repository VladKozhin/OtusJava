package exceptionHandlers;

import interfaces.IExceptionHandler;

public class ErrorNameHandler implements IExceptionHandler {
    private String name;

    public ErrorNameHandler(String name) {
        this.name = name;
    }

    @Override
    public boolean handle(Object error, Runnable command) {
        if (error instanceof Exception) {
            Exception ex = (Exception) error;
            if (ex.getClass().getSimpleName().equals(this.name)) {
                System.out.println("Обработка ошибки с именем: \"" + this.name + "\"");
                return true;
            }
        }
        return false;
    }
}