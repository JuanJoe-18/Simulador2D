package model;
import javafx.scene.canvas.GraphicsContext;
import structures.GrafoCarreteras;

import java.util.*;

public class VehiculoAutomata extends Vehiculo{
    private double velocidadX;
    private double velocidadY;
    private LinkedList<String> ruta;

    private GrafoCarreteras grafo;
    public VehiculoAutomata(double x, double y, double velocidadX, double velocidadY) {
        super(x, y);
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.ruta = new LinkedList();
    }

    /*
    * last logica: mover(velocidadX,velocidadY);
        //cambiar de direccion si se llega al borde del mapa
        if(getX() <= 0 || getX()>= 1600 - getWidth()){
            velocidadX = -velocidadX;
        }
        if (getY() <= 0 || getY() >= 1600 - getHeight()) {
            velocidadY = -velocidadY;
        }*/

    public void moverAutomata(){
        if (!ruta.isEmpty()){
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
    public void dibujar(GraphicsContext gc) {
        super.dibujar(gc);
    }

    public void asignarRuta(List<String> ruta) {
        this.ruta = new LinkedList<>(ruta);
    }



    public VehiculoAutomata(double x, double y, double velocidadX, double velocidadY, GrafoCarreteras grafo) {
        super(x, y);
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.ruta = new LinkedList<>();
        this.grafo = grafo;
    }

    private double[] obtenerCoordenadasNodo(String nodo) {
        return grafo.obtenerCoordenadas(nodo);
    }
}
