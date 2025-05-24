package model;

import javafx.scene.canvas.GraphicsContext;
import structures.GrafoCarreteras;
import structures.Nodo;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class VehiculoAutomata extends Vehiculo implements Runnable, Renderizable {
    private double velocidadX;
    private double velocidadY;
    private LinkedList<Nodo> ruta;
    private boolean activo = true;
    private GrafoCarreteras grafo;
    private final Random random = new Random();

    public VehiculoAutomata(double x, double y, double velocidadX, double velocidadY, GrafoCarreteras grafo) {
        super(x, y);
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.ruta = new LinkedList<>();
        this.grafo = grafo;
    }

    public void moverAutomata() {
        if (ruta != null && !ruta.isEmpty()) {
            Nodo nodoActual = ruta.peek();
            double[] destino = obtenerCoordenadasNodo(nodoActual);
            double dx = destino[0] - getX();
            double dy = destino[1] - getY();
            double distancia = Math.sqrt(dx * dx + dy * dy);
            if (distancia < 1) {
                ruta.poll();
                setX(destino[0]);
                setY(destino[1]);
            } else {
                mover((dx / distancia) * velocidadX, (dy / distancia) * velocidadY);
            }
        } else {
            Nodo origen = grafo.obtenerNodoPorNombre(buscarNodoMasCercano(getX(), getY()));
            Nodo destino = obtenerDestinoAleatorio(origen);
            if (origen != null && destino != null && !origen.equals(destino)) {
                asignarRuta(grafo.generarRutaDFS(origen.getNombre(), destino.getNombre()));
            }
        }
    }

    private String buscarNodoMasCercano(double x, double y) {
        double minDist = Double.MAX_VALUE;
        String nodoCercano = null;
        for (Nodo nodo : grafo.obtenerNodos()) {
            double dx = nodo.getX() - x;
            double dy = nodo.getY() - y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < minDist) {
                minDist = dist;
                nodoCercano = nodo.getNombre();
            }
        }
        return nodoCercano;
    }

    private Nodo obtenerDestinoAleatorio(Nodo origen) {
        List<Nodo> nodos = new java.util.ArrayList<>(grafo.obtenerNodos());
        if (nodos.size() <= 1) return null;
        Nodo destino;
        do {
            destino = nodos.get(random.nextInt(nodos.size()));
        } while (destino.equals(origen));
        return destino;
    }

    @Override
    public void run() {
        while (activo) {
            moverAutomata();
            try {
                Thread.sleep(25); // Controla la velocidad de movimiento
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void asignarRuta(List<Nodo> ruta) {
        if (ruta != null) {
            this.ruta = new LinkedList<>(ruta);
        }
    }

    private double[] obtenerCoordenadasNodo(Nodo nodo) {
        return grafo.obtenerCoordenadas(nodo);
    }

    @Override
    public void dibujar(GraphicsContext gc) {
        super.dibujar(gc);
    }
}

