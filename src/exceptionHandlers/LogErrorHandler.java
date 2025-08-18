package exceptionHandlers;

import interfaces.IExceptionHandler;

public class LogErrorHandler implements IExceptionHandler {

    @Override
    public boolean handle(Object error, Runnable command) {
        System.out.println("При выполнении кода возникла ошибка: " + error);
        return true;
    }
}