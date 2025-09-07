package interfaces;

import classes.Vector;

public interface IMovable {
    Vector getLocation();
    void setLocation(Vector vector);
    Vector getVelocity();
    void setVelocity(Vector vector);
}