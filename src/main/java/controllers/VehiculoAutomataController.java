package controllers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.Vehiculo;
import model.VehiculoAutomata;
import structures.GrafoCarreteras;

import java.util.ArrayList;
import java.util.List;

public class VehiculoAutomataController {
    private final GrafoCarreteras grafo;
    private final List<VehiculoAutomata> automatas;
    private final int width;
    private final int height;

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

        automata1.asignarRuta(grafo.generarRutaDFS("A", "N"));
        automata2.asignarRuta(grafo.generarRutaDFS("H", "X"));
        automata3.asignarRuta(grafo.generarRutaDFS("K", "O"));
        automata4.asignarRuta(grafo.generarRutaDFS("G", "E"));
        automata5.asignarRuta(grafo.generarRutaDFS("C", "H"));

        automatas.add(automata1);
        automatas.add(automata2);
        automatas.add(automata3);
        automatas.add(automata4);
        automatas.add(automata5);

        for (VehiculoAutomata automata : automatas) {
            new Thread(automata).start();
        }
    }

    public void dibujarMapa(GraphicsContext gc, Image mapa, Vehiculo jugador) {
        gc.clearRect(0, 0, width, height);
        gc.drawImage(mapa, 0, 0, width, height);
        jugador.dibujar(gc);
        for (VehiculoAutomata automata : automatas) {
            automata.dibujar(gc);
        }
    }

    public void actualizarSimulacion(GraphicsContext gc, Image mapa, Vehiculo jugador) {
        dibujarMapa(gc, mapa, jugador);
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
}