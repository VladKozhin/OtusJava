package interfaces;

/**
 * Интерфейс фабрики для создания объектов
 */
@FunctionalInterface
public interface IFactory<T> {
    T create(Object... args);
}
