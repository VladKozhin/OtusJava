import java.util.HashMap;
import java.util.Map;

import classes.*;
import interfaces.IAbstractObject;

public class Otus {
    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<>();
        params.put("Location", new Vector(5, 5));
        params.put("Direction", 90.0); // угол в градусах
        params.put("Velocity", new Vector(5, 5));
        params.put("DirectionsNumber", 8);
        params.put("AngularVelocity", 90.0);

        IAbstractObject abstractObject = new AbstractObject(params);
        AbstractMovableObject spaceShip = new AbstractMovableObject(abstractObject);
        Move moveExecutor = new Move(spaceShip);
        Rotate rotateExecutor = new Rotate(spaceShip);

        CommandQueue queue = new CommandQueue();

        Runnable myCommand = moveExecutor::execute;
        System.out.println(spaceShip.getLocation().getX());
        queue.enqueue(myCommand);
        queue.executeAll();
        System.out.println(spaceShip.getLocation().getX());
    }
}
