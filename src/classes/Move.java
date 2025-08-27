package classes;

import interfaces.ICommand;
import interfaces.IMovable;

public class Move implements ICommand {
    private IMovable target;

    public Move(IMovable target) {
        this.target = target;
    }

    public void execute() {
        Vector location = target.getLocation();
        Vector velocity = target.getVelocity();
        Vector newLocation = location.add(velocity);
        System.out.println("Start: " + location.toString() + ", end: " + newLocation.toString());
        target.setLocation(newLocation);
    }
}