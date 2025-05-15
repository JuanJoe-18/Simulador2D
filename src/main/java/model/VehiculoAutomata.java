package model;

import javafx.scene.canvas.GraphicsContext;
import structures.GrafoCarreteras;

import java.util.LinkedList;
import java.util.List;

public class VehiculoAutomata extends Vehiculo implements Runnable {
    private double velocidadX;
    private double velocidadY;
    private LinkedList<String> ruta;
    private boolean activo = true;
    private GrafoCarreteras grafo;

    public VehiculoAutomata(double x, double y, double velocidadX, double velocidadY, GrafoCarreteras grafo) {
        super(x, y);
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.ruta = new LinkedList<>();
        this.grafo = grafo;
    }

    public void moverAutomata() {
        if (ruta != null && !ruta.isEmpty()) {
            String nodoActual = ruta.peek();
            double[] destino = obtenerCoordenadasNodo(nodoActual);

            // Calcular dirección hacia el nodo destino
            double dx = destino[0] - getX();
            double dy = destino[1] - getY();
            double distancia = Math.sqrt(dx * dx + dy * dy);

            if (distancia < 1) {
                // Llegó al nodo actual, pasar al siguiente
                ruta.poll();
                setX(destino[0]);
                setY(destino[1]);
            } else {
                // Moverse hacia el nodo destino
                mover((dx / distancia) * velocidadX, (dy / distancia) * velocidadY);
            }
        }
    }

    @Override
    public void run() {
        while (activo) {
            moverAutomata();
            try {
                Thread.sleep(50); // Controla la velocidad de movimiento
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void detener() {
        activo = false;
    }

    public void asignarRuta(List<String> ruta) {
        if (ruta != null) {
            this.ruta = new LinkedList<>(ruta);
        }
    }

    private double[] obtenerCoordenadasNodo(String nodo) {
        return grafo.obtenerCoordenadas(nodo);
    }

    @Override
    public void dibujar(GraphicsContext gc) {
        super.dibujar(gc);
    }
}