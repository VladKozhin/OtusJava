import classes.*;
import classes.commands.HardStopCommand;
import classes.commands.SoftStopCommand;
import classes.commands.StartCommand;
import interfaces.ICommand;
import interfaces.IFactory;
import interfaces.IMovable;
import interfaces.IRotatable;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class CommandExecutorTest {

    private CommandExecutor executor;
    private StartCommand startCommand;

    //переменные для теста очереди
    public static Map<String, Object> params = new HashMap<>();
    AbstractObject abstractObject = IoC.resolve("GetAbstractObject", params);
    AbstractMovableObject spaceShip = IoC.resolve("GetAbstractMovableObject", abstractObject);
    ICommand move = IoC.resolve("MoveMe", spaceShip);

    /**
     * Инициализация корабля для теста №1 (на работу очереди)
     */
    @BeforeAll
    static void startTest() {
        params.put("Location", new Vector(0, 0));
        params.put("Direction", 0.0); // угол в градусах
        params.put("Velocity", new Vector(5, 0));
        params.put("DirectionsNumber", 8);
        params.put("AngularVelocity", 90.0);

        IoC.resolve("Scopes.New", "gameSession1");
        IoC.resolve("Scopes.Current", "gameSession1");

        IoC.resolve("IoC.Register", "MoveMe", (IFactory<ICommand>) (parameters) -> new Move((IMovable) parameters[0]));
        IoC.resolve("IoC.Register", "RotateMe", (IFactory<ICommand>) (parameters) -> new Rotate((IRotatable) parameters[0]));

        IoC.resolve("IoC.Register", "GetAbstractObject", (IFactory<AbstractObject>) (parameters) -> new AbstractObject((Map<String, Object>) parameters[0]));
        IoC.resolve("IoC.Register", "GetAbstractMovableObject", (IFactory<AbstractMovableObject>) (parameters) -> new AbstractMovableObject((AbstractObject) parameters[0]));
    }

    /**
     * Сбрасываем состояние очереди и экземпляра StartCommand
     */
    @BeforeEach
    void setUp() {
        executor = new CommandExecutor();
        startCommand = new StartCommand(executor);
    }

    /**
     * Создаёт и запускает очередь команд, выбрасывая исключение в одной из них.
     * Ожидается, что процесс не будет прерван (согласно требованию из п. 1)
     * <p>
     * В этом тесте сначала происходит движение до x = 5., затем движение до координаты y = 5, затем выбрасывается исключение, затем движение до координаты y = 10.
     * Конечное положение должно быть (5, 10)
     */
    @Test
    void testStartAndExecute() throws InterruptedException {
        //запускаем поток
        startCommand.execute();

        //счётчик
        CountDownLatch latch = new CountDownLatch(4);

        executor.addCommand(() -> {
            System.out.println("Move to x = 5");
            move.execute();
            System.out.println(spaceShip.getLocation());
            latch.countDown();
        });

        executor.addCommand(() -> {
            System.out.println("Move to y = 5");
            spaceShip.setVelocity(new Vector(0, 5));
            move.execute();
            System.out.println(spaceShip.getLocation());
            latch.countDown();
        });

        executor.addCommand(() -> {
            latch.countDown();
            throw new RuntimeException("Error!");
        });

        executor.addCommand(() -> {
            System.out.println("Move to y = 5");
            spaceShip.setVelocity(new Vector(0, 5));
            move.execute();
            System.out.println(spaceShip.getLocation());
            latch.countDown();
        });

        //ждём, пока счётчик не обнулится
        latch.await(2, TimeUnit.SECONDS);

        // Останавливаем мягко
        SoftStopCommand softStop = new SoftStopCommand(executor, startCommand.getThread());
        softStop.execute();

        // Проверяем, что координаты объекта поменялись
        assertEquals(new Vector(5, 10), spaceShip.getLocation());
    }

    /**
     * В этом тесте проверяется работа команды для "мягкой" остановки потока.
     *
     * @throws InterruptedException
     */
    @Test
    void testSoftStop() throws InterruptedException {
        startCommand.execute();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger changeMeThread = new AtomicInteger(0);

        /**
         * Т. к. пока в программе нет функций, которые выполнялись бы длительное время, мы сымитируем длительную
         * работу через sleep
         */
        executor.addCommand(() -> {
            System.out.println("Команда начала выполнение: стартовое значение переменной changeMeThread = " + changeMeThread.get());
            Thread.sleep(2500);
            changeMeThread.set(1);
            latch.countDown();
            System.out.println("Команда завершена. Значение переменной changeMeThread = " + changeMeThread.get());
        });

        // отдельным потоком запускаем функцию для мягкой остановки главного потока
        Thread softStopThread = new Thread(() -> {
            SoftStopCommand softStop = new SoftStopCommand(executor, startCommand.getThread());
            softStop.execute();
        });
        softStopThread.start();

        // Ждем выполнения команды
        boolean done = latch.await(3, TimeUnit.SECONDS);
        assertTrue(done, "Счётчик равен нулю (команда выполнилась)");

        // Ждем завершения soft stop
        softStopThread.join(2000);
        assertEquals(1, changeMeThread.get());
    }

    /**
     * Тест жёсткой остановки. Аналог теста для мягкой.
     * Создаётся длительная операция, затем отдельный поток инициирует hardStop.
     * В конце выполняется проверка, что поток успел изменить значение и, в итоге, остановился.
     * @throws InterruptedException
     */
    @Test
    void testHardStop() throws InterruptedException {
        startCommand.execute();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean commandStarted = new AtomicBoolean(false);

        executor.addCommand(() -> {
            commandStarted.set(true);
            try {
                Thread.sleep(5000); // Длинная команда
            } catch (InterruptedException e) {
                System.out.println("Поток " + Thread.currentThread().getName() + " был принудительно прерван!");
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        });

        // Даем время команде стартовать
        Thread.sleep(100);

        Thread hardStopThread = new Thread(() -> {
            HardStopCommand hardStop = new HardStopCommand(executor, startCommand.getThread());
            hardStop.execute();
        });
        hardStopThread.start();


        // Ждем, что команда завершится (прерванная)
        boolean finished = latch.await(5, TimeUnit.SECONDS);
        assertTrue(finished, "Команда должна завершиться после жесткой остановки");

        // Ждем, что поток остановится
        Thread thread = startCommand.getThread();
        thread.join(1000);

        assertFalse(thread.isAlive(), "Поток должен остановиться после жесткой остановки");
        assertTrue(commandStarted.get(), "Команда должна была начаться");
    }
}