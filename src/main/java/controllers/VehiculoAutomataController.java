package controllers;

import javafx.scene.canvas.GraphicsContext;
import model.Renderizable;
import model.Vehiculo;
import model.VehiculoAutomata;
import structures.GrafoCarreteras;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VehiculoAutomataController {
    private final GrafoCarreteras grafo;
    private final List<VehiculoAutomata> automatas;
    private final int width;
    private final int height;
    private ExecutorService pool;

    public VehiculoAutomataController(GrafoCarreteras grafo, GraphicsContext gc, int width, int height) {
        this.grafo = grafo;
        this.automatas = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    public void inicializarAutomatas() {
        VehiculoAutomata automata1 = new VehiculoAutomata(131 * 16, 120 * 16, 0.333, 0.333, grafo);
        VehiculoAutomata automata2 = new VehiculoAutomata(180 * 16, 142 * 16, 0.333, 0.333, grafo);
        VehiculoAutomata automata3 = new VehiculoAutomata(151 * 16, 164 * 16, 0.333, 0.333, grafo);
        VehiculoAutomata automata4 = new VehiculoAutomata(131 * 16, 208 * 16, 0.333, 0.333, grafo);
        VehiculoAutomata automata5 = new VehiculoAutomata(180 * 16, 120 * 16, 0.333, 0.333, grafo);
        VehiculoAutomata automata6 = new VehiculoAutomata(209 * 16, 120 * 16, 0.333, 0.333, grafo);
        VehiculoAutomata automata7 = new VehiculoAutomata(230 * 16, 142 * 16, 0.333, 0.333, grafo);
        VehiculoAutomata automata8 = new VehiculoAutomata(151 * 16, 186 * 16, 0.333, 0.333, grafo);
        VehiculoAutomata automata9 = new VehiculoAutomata(209 * 16, 208 * 16, 0.333, 0.333, grafo);
        VehiculoAutomata automata10 = new VehiculoAutomata(230 * 16, 208 * 16, 0.333, 0.333, grafo);

        automata1.asignarRuta(grafo.generarRutaDFS("A", "N"));
        automata2.asignarRuta(grafo.generarRutaDFS("H", "X"));
        automata3.asignarRuta(grafo.generarRutaDFS("K", "O"));
        automata4.asignarRuta(grafo.generarRutaDFS("G", "E"));
        automata5.asignarRuta(grafo.generarRutaDFS("C", "H"));
        automata6.asignarRuta(grafo.generarRutaDFS("D", "S"));
        automata7.asignarRuta(grafo.generarRutaDFS("J", "O"));
        automata8.asignarRuta(grafo.generarRutaDFS("P", "A_0"));
        automata9.asignarRuta(grafo.generarRutaDFS("W", "B"));
        automata10.asignarRuta(grafo.generarRutaDFS("X", "F"));

        automatas.add(automata1);
        automatas.add(automata2);
        automatas.add(automata3);
        automatas.add(automata4);
        automatas.add(automata5);
        automatas.add(automata6);
        automatas.add(automata7);
        automatas.add(automata8);
        automatas.add(automata9);
        automatas.add(automata10);

        // Creamos la piscinita de hilos un p de hilos pero fija
        pool = Executors.newFixedThreadPool(automatas.size());
        for (VehiculoAutomata automata : automatas) {
            pool.submit(automata);
        }
    }

    public List<Renderizable> obtenerAutomatasRenderizables() {
        return new ArrayList<>(automatas);
    }

    public void actualizarSimulacion(Vehiculo jugador) {
        for (VehiculoAutomata automata : automatas) {
            automata.moverAutomata();
        }
    }

    public void detectarColisiones(Vehiculo jugador) {
        for (VehiculoAutomata automata : automatas) {
            if (jugador.colisionaCon(automata)) {
                System.out.println("¡Colisión con un vehículo automático!");
            }
            for (VehiculoAutomata otroAutomata : automatas) {
                if (automata != otroAutomata && automata.colisionaCon(otroAutomata)) {
                    System.out.println("¡Colisión entre vehículos automáticos!");
                }
            }
        }
    }

    public void shutdownPool() {
        if (pool != null && !pool.isShutdown()) {
            pool.shutdownNow();
        }
    }
}
