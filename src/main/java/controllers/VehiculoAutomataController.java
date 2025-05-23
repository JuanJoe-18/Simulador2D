package controllers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.Vehiculo;
import model.VehiculoAutomata;
import structures.Grafo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VehiculoAutomataController {
    private final Grafo<String> grafo;
    private final List<VehiculoAutomata> automatas;
    private final ExecutorService executor;
    private final int width;
    private final int height;
    private static final int TILE_SIZE = 16;
    private static final double VELOCIDAD_X = 0.333;
    private static final double VELOCIDAD_Y = 0.333;

    public VehiculoAutomataController(Grafo<String> grafo, GraphicsContext gc, int width, int height) {
        this.grafo = grafo;
        this.automatas = new CopyOnWriteArrayList<>();
        this.executor = Executors.newFixedThreadPool(5);
        this.width = width;
        this.height = height;
    }

    public void inicializarAutomatas() {
        String[][] configuraciones = {
                {"131", "120", "A", "N", "0.333", "-0.333"},
                {"180", "142", "H", "X", "-0.5", "0.5"},
                {"151", "164", "K", "O", "0.25", "0.25"},
                {"131", "208", "G", "E", "-0.333", "-0.333"},
                {"180", "120", "C", "H", "0.5", "-0.5"}
        };

        for (String[] config : configuraciones) {
            int x = Integer.parseInt(config[0]) * TILE_SIZE;
            int y = Integer.parseInt(config[1]) * TILE_SIZE;
            String inicio = config[2];
            String fin = config[3];
            double velocidadX = Double.parseDouble(config[4]);
            double velocidadY = Double.parseDouble(config[5]);

            VehiculoAutomata automata = new VehiculoAutomata(x, y, velocidadX, velocidadY, grafo);
            List<String> ruta = grafo.generarRutaDFS(inicio, fin);
            if (ruta != null && !ruta.isEmpty()) {
                automata.asignarRuta(ruta);
            } else {
                System.err.println("No se pudo generar una ruta válida para el automata en coordenadas " + x + ", " + y);
            }
            automatas.add(automata);
            executor.submit(automata);
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
        for (VehiculoAutomata automata : automatas) {
            automata.moverAutomata();
        }
        dibujarMapa(gc, mapa, jugador);
    }

    public void detectarColisiones(Vehiculo jugador) {
        for (VehiculoAutomata automata : automatas) {
            if (jugador.colisionaCon(automata)) {
                manejarColision(jugador, automata);
            }
            for (VehiculoAutomata otroAutomata : automatas) {
                if (automata != otroAutomata && automata.colisionaCon(otroAutomata)) {
                    manejarColision(automata, otroAutomata);
                }
            }
        }
    }

    private void manejarColision(Vehiculo vehiculo1, Vehiculo vehiculo2) {
        System.out.println("¡Colisión detectada entre " + vehiculo1 + " y " + vehiculo2 + "!");
        vehiculo1.detener();
        vehiculo2.detener();
    }

    public void finalizarSimulacion() {
        executor.shutdownNow();
        automatas.clear();
        System.out.println("Simulación finalizada y recursos liberados.");
    }
}