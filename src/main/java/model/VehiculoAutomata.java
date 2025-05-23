package model;

import javafx.scene.canvas.GraphicsContext;
import structures.Grafo;

import java.util.LinkedList;
import java.util.List;

public class VehiculoAutomata extends Vehiculo implements Runnable {
    private double velocidadX;
    private double velocidadY;
    private LinkedList<String> ruta;
    private boolean activo = true;
    private Grafo<String> grafo;

    public VehiculoAutomata(double x, double y, double velocidadX, double velocidadY, Grafo<String> grafo) {
        super(x, y);
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.ruta = new LinkedList<>();
        this.grafo = grafo;
    }


    public void moverAutomata() {
        if (ruta != null && !ruta.isEmpty()) {
            String nodoActual = ruta.peek();
            double[] destino = grafo.obtenerCoordenadas(nodoActual);

            if (destino != null) {
                double dx = destino[0] - getX();
                double dy = destino[1] - getY();
                double distancia = Math.sqrt(dx * dx + dy * dy);

                if (distancia < 1) {
                    ruta.poll();
                    setX(destino[0]);
                    setY(destino[1]);
                } else {
                    double movimientoX = (dx / distancia) * velocidadX;
                    double movimientoY = (dy / distancia) * velocidadY;

                    mover(movimientoX, movimientoY); // Llama al método mover para actualizar posición e imagen
                }
            }
        }
    }

    @Override
    public void run() {
        while (activo) {
            moverAutomata();
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void asignarRuta(List<String> ruta) {
        if (ruta != null) {
            this.ruta = new LinkedList<>(ruta);
        }
    }

    @Override
    public void dibujar(GraphicsContext gc) {
        super.dibujar(gc);
    }

    @Override
    public void detener() {
        this.activo = false;
        System.out.println("VehiculoAutomata detenido.");
    }
}