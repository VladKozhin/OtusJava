import classes.*;
import interfaces.*;

import java.util.HashMap;
import java.util.Map;

// Main.java
public class Otus {

    public static void main(String[] args) {

        Map<String, Object> params = new HashMap<>();
        params.put("Location", new Vector(5, 5));
        params.put("Direction", 90.0); // угол в градусах
        params.put("Velocity", new Vector(10, 0));
        params.put("DirectionsNumber", 8);
        params.put("AngularVelocity", 90.0);

        IAbstractObject abstractObject = new AbstractObject(params);
        AbstractMovableObject spaceShip = new AbstractMovableObject(abstractObject);

        IoC.writeLog();
        // Создаем новый скоуп "gameSession1"
        IoC.resolve("Scopes.New", "gameSession1");
        // Устанавливаем текущий активный скоуп на "gameSession1"
        IoC.resolve("Scopes.Current", "gameSession1");

        IoC.resolve("IoC.Register", "MoveMe", (IFactory<ICommand>) (parameters) -> new Move((IMovable)parameters[0]));
        IoC.resolve("IoC.Register", "RotateMe", (IFactory<ICommand>) (parameters) -> new Rotate((IRotatable)parameters[0]));

        ICommand move = IoC.resolve("MoveMe", spaceShip);
        ICommand rotate = IoC.resolve("RotateMe", spaceShip);
        Queue gameQueue = new Queue();
        gameQueue.enqueue(move);
        gameQueue.enqueue(rotate);
        gameQueue.enqueue(() -> spaceShip.setVelocity(spaceShip.clarificateDirection()));
        gameQueue.enqueue(move);
        gameQueue.execute();
    }
}