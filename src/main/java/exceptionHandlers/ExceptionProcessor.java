package exceptionHandlers;

import interfaces.IExceptionHandler;

import java.util.ArrayList;
import java.util.List;

public class ExceptionProcessor {
    private List<IExceptionHandler> handlers = new ArrayList<>();

    /**
     * Добавляет обработчик исключений.
     * @param handler Объект, реализующий IExceptionHandler.
     */
    public void addHandler(IExceptionHandler handler) {
        handlers.add(handler);
    }

    /**
     * Обрабатывает ошибку, вызывая обработчики по порядку.
     * Если один из обработчиков возвращает true, обработка прекращается.
     * @param error Объект ошибки.
     * @param command Команда для выполнения после обработки ошибки.
     */
    public void process(Object error, Runnable command) {
        for (IExceptionHandler handler : handlers) {
            if (handler.handle(error, command)) {
                break;
            }
        }
    }
}