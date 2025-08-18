package classes;

import interfaces.IMovable;

public class Move {
    private IMovable target;

    public Move(IMovable target) {
        this.target = target;
    }

    public void execute() {
        Vector location = target.getLocation();
        Vector velocity = target.getVelocity();
        Vector newLocation = location.add(velocity);
        target.setLocation(newLocation);
    }
}