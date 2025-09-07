package interfaces;

public interface IExceptionProcessor {
    /**
     * Добавляет обработчик исключений.
     * @param handler Объект, реализующий IExceptionHandler.
     */
    void addHandler(IExceptionHandler handler);

    /**
     * Обрабатывает ошибку, вызывая все зарегистрированные обработчики.
     * @param error Объект ошибки.
     */
    void execute(Object error);
}