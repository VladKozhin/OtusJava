import classes.*;
import interfaces.*;

import java.util.HashMap;
import java.util.Map;

// Main.java
public class Otus {

    public static void main(String[] args) {

        Map<String, Object> params = new HashMap<>();
        params.put("Location", new Vector(0, 0));
        params.put("Direction", 0.0); // угол в градусах
        params.put("Velocity", new Vector(10, 5));
        params.put("DirectionsNumber", 8);
        params.put("AngularVelocity", 90.0);

        AbstractObject obj = new AbstractObject(params);



        /*IoC.writeLog();
        IoC.resolve("Scopes.New", "gameSession1");
        IoC.resolve("Scopes.Current", "gameSession1");

        IoC.resolve("IoC.Register", "MoveMe", (IFactory<ICommand>) (parameters) -> new Move((IMovable)parameters[0]));
        IoC.resolve("IoC.Register", "RotateMe", (IFactory<ICommand>) (parameters) -> new Rotate((IRotatable)parameters[0]));

        IoC.resolve("IoC.Register", "GetAbstractObject", (IFactory<AbstractObject>) (parameters) -> new AbstractObject((Map<String, Object>) parameters[0]));
        IoC.resolve("IoC.Register", "GetAbstractMovableObject", (IFactory<AbstractMovableObject>) (parameters) -> new AbstractMovableObject((AbstractObject) parameters[0]));

        AbstractObject abstractObject = IoC.resolve("GetAbstractObject", params);
        AbstractMovableObject spaceShip = IoC.resolve("GetAbstractMovableObject", abstractObject);

        ICommand move = IoC.resolve("MoveMe", spaceShip);
        ICommand rotate = IoC.resolve("RotateMe", spaceShip);
        Queue gameQueue = new Queue();
        gameQueue.enqueue(move);
        gameQueue.enqueue(rotate);
        gameQueue.enqueue(() -> spaceShip.setVelocity(spaceShip.clarificateDirection()));
        gameQueue.enqueue(move);
        gameQueue.execute();*/
    }
}