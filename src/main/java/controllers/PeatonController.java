package controllers;

import javafx.scene.canvas.GraphicsContext;
import model.Automata;
import model.Peaton;
import model.Renderizable;
import utils.MatrizLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeatonController {
    private List<Automata> automatas;
    private int[][] matrizColisiones;
    private int anchoMapa;
    private int altoMapa;
    private ExecutorService pool;

    public PeatonController(int anchoMapa, int altoMapa, String rutaMatriz) throws IOException {
        this.anchoMapa = anchoMapa;
        this.altoMapa = altoMapa;
        this.automatas = new ArrayList<>();
        this.matrizColisiones = MatrizLoader.cargarMatrizDesdeCSV(rutaMatriz);
    }

    public void inicializarPeatones(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            double x, y;
            int indiceX, indiceY;

            // Intentar generar un peatón en una posición transitable
            do {
                x = Math.random() * anchoMapa;
                y = Math.random() * altoMapa;

                // Convertir coordenadas a índices de la matriz
                indiceX = (int) (x / 16);
                indiceY = (int) (y / 16);
            } while (indiceX < 0 || indiceX >= matrizColisiones[0].length ||
                     indiceY < 0 || indiceY >= matrizColisiones.length ||
                     (matrizColisiones[indiceY][indiceX] != 1 && matrizColisiones[indiceY][indiceX] != 2));

            // Generar velocidades aleatorias
            double velocidadX = (Math.random() - 0.5) * 2;
            double velocidadY = (Math.random() - 0.5) * 2;

            // Crear peatón con la matriz de colisiones
            automatas.add(new Peaton(x, y, velocidadX, velocidadY, matrizColisiones));
        }

        pool = Executors.newFixedThreadPool(automatas.size());
        for (Automata automata : automatas) {
            if (automata instanceof Runnable) {
                pool.submit((Runnable) automata);
            }
        }
    }

    public void actualizarAutomatas() {
        for (Automata automata : automatas) {
            automata.mover();
        }
    }

    public List<Renderizable> obtenerPeatonesRenderizables() {
        List<Renderizable> renderizables = new ArrayList<>();
        for (Automata automata : automatas) {
            if (automata instanceof Renderizable) {
                renderizables.add((Renderizable) automata);
            }
        }
        return renderizables;
    }

    public void shutdownPool() {
        if (pool != null && !pool.isShutdown()) {
            pool.shutdownNow();
        }
    }
}
