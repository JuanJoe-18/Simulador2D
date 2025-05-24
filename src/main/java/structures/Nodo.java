package structures;

import java.util.Objects;

public class Nodo {
    private final String nombre;
    private final double x;
    private final double y;

    public Nodo(String nombre, double x, double y) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
    }

    public String getNombre() {
        return nombre;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nodo nodo = (Nodo) o;
        return Double.compare(nodo.x, x) == 0 && Double.compare(nodo.y, y) == 0 && Objects.equals(nombre, nodo.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, x, y);
    }
}
