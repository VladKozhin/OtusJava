package components;

import classes.Vector;
import interfaces.IMovable;

public class MoveComponent implements IMovable {
    private Vector location;
    private Vector velocity;

    public MoveComponent(Vector location, Vector velocity) {
        this.location = location;
        this.velocity = velocity;
    }

    @Override
    public Vector getLocation() {
        return location;
    }

    @Override
    public void setLocation(Vector vector) {
        this.location = vector;
    }

    @Override
    public Vector getVelocity() {
        return velocity;
    }
}