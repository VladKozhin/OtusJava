package interfaces;

@FunctionalInterface
public interface IExceptionHandler {
    /**
     * Обработка ошибки.
     * @param error Объект ошибки.
     * @param command Необязательный командный вызов (может быть null).
     * @return true, если ошибка обработана успешно; иначе false.
     */
    boolean handle(Object error, Runnable command);
}