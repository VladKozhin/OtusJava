package tests;

import interfaces.ICommand;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import classes.IoC;
import interfaces.IFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class IoCTest {

    /**
     * Перед любыми операциями выполняем сброс всех внутренных хранилищ
     */
    @BeforeEach
    void setup() {
        IoC.reset();
    }

    /**
     * Проверка, что зависимость c ICommand успешно регистрируется и возвращается
     */
    @Test
    void testRegisterAndResolveObject() {
        IoC.writeLog();
        IoC.resolve("Scopes.New", "test1");
        IoC.resolve("Scopes.Current", "test1");

        String key = "Ioc.Test";
        ICommand value = () -> System.out.println(key);

        IoC.resolve("IoC.Register", key, value);
        ICommand result = IoC.resolve(key);

        assertEquals(value, result);
    }

    /**
     * Аналогичная проверка для регистрации и получения фабрики IFactory
     */
    @Test
    void testRegisterAndResolveFactory() {
        IoC.writeLog();
        IoC.resolve("Scopes.New", "test2");
        IoC.resolve("Scopes.Current", "test2");

        String key = "factoryService";

        // Регистрация фабрики
        IoC.resolve("IoC.Register", key, new IFactory<String>() {
            @Override
            public String create(Object... args) {
                return "Created by factory";
            }
        });

        String result = IoC.resolve(key);
        assertEquals("Created by factory", result);
    }

    /**
     * Проверяем, что выбрасывается исключение при попытке обратиться к несуществующему контейнеру
     */
    @Test
    void testUnregisteredResolvingThrowsException() {
        IoC.writeLog();
        IoC.resolve("Scopes.New", "test3");
        IoC.resolve("Scopes.Current", "test3");
        assertThrows(RuntimeException.class, () -> {
            IoC.resolve("nonexistentKey");
        });
    }

    /**
     * Проверка, что созданный скоуп сохраняется в хранилище
     */
    @Test
    void testCreateAndUseScopes() {
        String scopeId = "testScope1";
        IoC.resolve("Scopes.New", scopeId);
        IoC.resolve("Scopes.Current", scopeId);
        assertNotNull(IoC.getScopeById(scopeId));
    }

    /**
     * Проверяем, что работа производится в разных скоупах корректно
     * В рамках теста создаётся два скоупа, происходит переключение между ними и регистрация зависимости в них
     */
    @Test
    void testScopeOverridesDependency() {
        IoC.writeLog();
        IoC.resolve("Scopes.New", "test4_1");
        IoC.resolve("Scopes.New", "test4_2");
        IoC.resolve("Scopes.Current", "test4_1");

        String key_1 = "Ioc.Test_1";
        ICommand value_1 = () -> System.out.println(key_1);

        String key_2 = "Ioc.Test_2";
        ICommand value_2 = () -> System.out.println(key_2);

        IoC.resolve("IoC.Register", key_1, value_1);

        IoC.resolve("Scopes.Current", "test4_2");
        IoC.resolve("IoC.Register", key_2, value_2);

        ICommand result_2 = IoC.resolve(key_2);
        assertEquals(value_2, result_2);

        IoC.resolve("Scopes.Current", "test4_1");
        ICommand result_1 = IoC.resolve(key_1);
        assertEquals(value_1, result_1);
    }

    /**
     * МНОГОПОТОЧНЫЕ ТЕСТЫ
     */

    /**
     * Регистрация зависимостей в несколько потоков.
     * В коде происходит сохранение номера потока и ID ICommand в Map с последующей проверкой значений
     * @throws InterruptedException
     */
    @Test
    void testConcurrentRegistrationAndResolution() throws InterruptedException {
        Map<Integer, String> idRepository = new HashMap<Integer, String>();
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        System.out.println("Регистрирую фабрику в " + threadCount + " потоков");
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.execute(() -> {
                try {
                    String key = "service" + index;
                    ICommand cmd = () -> System.out.println("this is service" + index);
                    idRepository.put(index, cmd.toString());
                    System.out.println("Работа в потоке №" + index + " : регистрирую функцию service" + index + " с ID " + cmd.toString());
                    IoC.resolve("IoC.Register", key, cmd);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        System.out.println("Получаю зависимости обратно.");
        for (int i = 0; i < idRepository.size(); i++) {
            String key = "service" + i;
            System.out.println("Получаю зависимость " + key);
            ICommand result = IoC.resolve(key);
            assertEquals(idRepository.get(i), result.toString());
        }
    }

    /**
     * Аналогичный тест, но уже для скоупов
     */
    @Test
    void testConcurrentScopes() throws InterruptedException {
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // Создаем разные скоупы в разных потоках
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.execute(() -> {
                try {
                    String scopeId = "scope" + index;
                    IoC.resolve("Scopes.New", scopeId);
                    IoC.resolve("Scopes.Current", scopeId);
                    System.out.println("Создаю и устанавливаю текущим скоуп с ID " + scopeId);
                    String key = "scopedService" + index;
                    ICommand value = () -> System.out.println("this is for scope" + index);
                    IoC.resolve("IoC.Register", key, value);
                    System.out.println("Проверяю, что для потока с индексом " + index + " создан скоуп " + scopeId);
                    ICommand result = IoC.resolve(key);
                    assertEquals(value, result);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

    /**
     * Проверка, что несколько потоков имеют доступ к одной и той же фабрике
     * @throws InterruptedException
     */
    @Test
    void testConcurrentAccessToSameKey() throws InterruptedException {
        int threadCount = 5;
        String key = "sharedService";

        IFactory<ICommand> factory = new IFactory<ICommand>() {
            @Override
            public ICommand create(Object... args) {
                return null;
            }
        };
        // Регистрация фабрики один раз
        IoC.resolve("IoC.Register", key, (IFactory<String>) args -> "SharedInstance");

        ExecutorService executor = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i=0; i<threadCount; i++) {
            executor.execute(() -> {
                try {
                    String result = IoC.resolve(key);
                    assertEquals("SharedInstance", result);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }
}