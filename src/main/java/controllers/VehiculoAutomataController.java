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
        // Spawns y rutas usando los nuevos nodos del grafo dirigido
        VehiculoAutomata automata1 = new VehiculoAutomata(720.0, 1210.0, 0.333, 0.333, grafo); // A1
        VehiculoAutomata automata2 = new VehiculoAutomata(2405.0, 1910.0, 0.333, 0.333, grafo); // C1
        VehiculoAutomata automata3 = new VehiculoAutomata(2865.0, 1910.0, 0.333, 0.333, grafo); // D1
        VehiculoAutomata automata4 = new VehiculoAutomata(3330.0, 1910.0, 0.333, 0.333, grafo); // E1
        VehiculoAutomata automata5 = new VehiculoAutomata(3690.0, 1910.0, 0.333, 0.333, grafo); // F1
        VehiculoAutomata automata6 = new VehiculoAutomata(2405.0, 2260.0, 0.333, 0.333, grafo); // H1
        VehiculoAutomata automata7 = new VehiculoAutomata(2865.0, 2260.0, 0.333, 0.333, grafo); // I1
        VehiculoAutomata automata8 = new VehiculoAutomata(3330.0, 2260.0, 0.333, 0.333, grafo); // J1
        VehiculoAutomata automata9 = new VehiculoAutomata(3690.0, 2260.0, 0.333, 0.333, grafo); // K1
        VehiculoAutomata automata10 = new VehiculoAutomata(2405.0, 2610.0, 0.333, 0.333, grafo); // L1

        automata1.asignarRuta(grafo.generarRutaDFS("A1", "O2"));
        automata2.asignarRuta(grafo.generarRutaDFS("C1", "X2"));
        automata3.asignarRuta(grafo.generarRutaDFS("D1", "T2"));
        automata4.asignarRuta(grafo.generarRutaDFS("E1", "P2"));
        automata5.asignarRuta(grafo.generarRutaDFS("F1", "A2"));
        automata6.asignarRuta(grafo.generarRutaDFS("H1", "L4"));
        automata7.asignarRuta(grafo.generarRutaDFS("I1", "V4"));
        automata8.asignarRuta(grafo.generarRutaDFS("J1", "W4"));
        automata9.asignarRuta(grafo.generarRutaDFS("K1", "X1"));
        automata10.asignarRuta(grafo.generarRutaDFS("L1", "U4"));

        automatas.clear();
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

        if (pool != null) pool.shutdownNow();
        pool = Executors.newFixedThreadPool(automatas.size());
        for (VehiculoAutomata automata : automatas) {
            pool.execute(automata);
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
