package classes;

import java.util.Objects;

public class Vector {
    private double x;
    private double y;

    // Конструктор с параметрами по умолчанию
    public Vector() {
        this.x = 0;
        this.y = 0;
    }

    // Конструктор с заданными значениями
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Геттеры и сеттеры (по желанию)
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    // Метод add, возвращающий новый вектор
    public Vector add(Vector vector) {
        return new Vector(this.x + vector.x, this.y + vector.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector)) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 &&
                Double.compare(vector.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
