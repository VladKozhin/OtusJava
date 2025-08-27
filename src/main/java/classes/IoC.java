package classes;// IoC.java
import interfaces.IFactory;
import interfaces.IoCInterface;

import java.util.concurrent.ConcurrentHashMap;

public class IoC  implements IoCInterface {
    /**
     * Мапа со всеми зависимостями
     */
    private static final ConcurrentHashMap<String, Object> register = new ConcurrentHashMap<>();

    /**
     * Все скоупы
     */
    private static final ConcurrentHashMap<String, Scope> scopes = new ConcurrentHashMap<>();

    /**
     * Текущий скоуп (в потоке)
     */
    private static final ThreadLocal<Scope> currentScope = ThreadLocal.withInitial(() -> null);

    public static Scope getScopeById(String key) {
        return scopes.get(key);
    }

    private static Boolean writeLogs = false;

    /**
     * Устанавливает флаг "writeLogs" в true, чтобы выводить логи
     */
    public static void writeLog(){
        writeLogs = true;
    }

    public static <T> void outputMap(ConcurrentHashMap<String, T> map){
        for (java.util.Map.Entry<String, T> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    /**
     * Для отладки. Выводит лог
     * @param msg Строка, которая выводится в лог
     */
    public static void log(String msg){
        if(writeLogs)System.out.println(msg);
    }

    /**
     * Основной метод Resolve
     * В зависимости от ключа и аргументов возвращает объект
     */
    @SuppressWarnings("unchecked")
    public static <T> T resolve(String key, Object... args) {
         // Обработка заранее определённых для работы со скоупами функций
         // Принимает два аргумента - ключ и фабрику
        if ("IoC.Register".equals(key)) {
            String regKey = (String) args[0];
            Object regValue = args[1];

            register(regKey, regValue);
            log("Зарегистрирована новая зависимость с ключом: " + regKey);
            return null;
        } else if ("Scopes.New".equals(key)) {
            // Создаёт новый скоуп с id из args[0]
            String scopeId = (String) args[0];
            log("Создал скоуп с ID: " + scopeId);
            scopes.put(scopeId, new Scope());
            //outputMap(scopes);
            return null;
        } else if ("Scopes.Current".equals(key)) {
            //Устанавливает текущий активный скоуп по id из args[0]
            String scopeId = (String) args[0];
            Scope scope = scopes.get(scopeId);
            currentScope.set(scope);
            log("Установил текущим скоуп с ID: " + scopeId);
            return null;
        }

        // Попытка получить из текущего скоупа
        Scope scope = currentScope.get();
        if (scope != null && scope.tryGet(key, scope::setLastResult)) {
            log("Возвращаю зависимость из текущего скоупа по ключу: " + key);
            return (T) scope.getLastResult();
        }

        // Если не нашли в скоупе — ищем глобально
        Object registration = register.get(key);
        if (registration == null) {
            throw new RuntimeException("Зависимость по ключу '" + key + "' не найдена");
        }

        // Если регистрация — фабрика IFactory<T>
        if (registration instanceof IFactory<?>) {
            IFactory<T> factory = (IFactory<T>) registration;
            T result = factory.create(args);
            if (scope != null) {
                scope.setLastResult(result);
            }
            log("Возвращаю фабрику по ключу (IFactory): " + key);
            return result;
        } else {
            // Просто значение или объект
            T result = (T) registration;
            if (scope != null) {
                scope.setLastResult(result);
            }
            log("Возвращаю объект по ключу: " + key + " с типом " + result.getClass().getName());
            return result;
        }
    }

    /**
     * Регистрация зависимости
     */
    public static void register(String key, Object value) {
        register.put(key, value);
    }

    /**
     * Очистка хранилищ
     */
    public static void reset() {
        register.clear();
        scopes.clear();
        currentScope.remove();
    }

}