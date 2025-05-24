package controllers;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import model.Renderizable;
import model.Vehiculo;
import structures.GrafoCarreteras;
import view.RenderizadorMapa;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private final GrafoCarreteras grafo;
    private final Vehiculo jugador;
    private final JugadorController jugadorController;
    private final VehiculoAutomataController automataController;
    private final PeatonController peatonController;
    private final RenderizadorMapa renderizadorMapa;
    private final List<Renderizable> renderizables;
    private final Image mapa;
    private final Group root;
    private final Scene scene;
    private final int width;
    private final int height;

    public GameController(GraphicsContext gc, Group root, Scene scene, int width, int height) throws Exception {
        this.width = width;
        this.height = height;
        this.root = root;
        this.scene = scene;
        this.grafo = new GrafoCarreteras();
        GrafoCarreteras.inicializarGrafo(grafo);
        this.jugador = new Vehiculo(180 * 16, 142 * 16);
        this.jugadorController = new JugadorController(jugador);
        this.automataController = new VehiculoAutomataController(grafo, gc, width, height);
        automataController.inicializarAutomatas();
        String rutaMatriz = "src/main/resources/files/colisionpPeatones.csv";
        this.peatonController = new PeatonController(width, height, rutaMatriz);
        peatonController.inicializarPeatones(50);
        this.renderizadorMapa = new RenderizadorMapa();
        this.mapa = cargarMapa("/assets/MAPA3.0.png");
        this.renderizables = new ArrayList<>();
        renderizables.add(jugador);
        renderizables.addAll(automataController.obtenerAutomatasRenderizables());
        renderizables.addAll(peatonController.obtenerPeatonesRenderizables());
    }

    public void manejarTecla(KeyEvent event) {
        jugadorController.manejarTeclas(event);
    }

    public void actualizarSimulacion() {
        automataController.actualizarSimulacion(jugador);
        peatonController.actualizarAutomatas();
    }

    public void renderizar(GraphicsContext gc) {
        renderizadorMapa.dibujarMapa(gc, mapa, renderizables);
    }

    public void detectarColisiones() {
        automataController.detectarColisiones(jugador);
    }

    public Vehiculo getJugador() {
        return jugador;
    }

    public List<Renderizable> getRenderizables() {
        return renderizables;
    }

    public void actualizarCamara() {
        double offsetX = Math.min(0, Math.max(scene.getWidth() / 2 - jugador.getX(), -width + scene.getWidth()));
        double offsetY = Math.min(0, Math.max(scene.getHeight() / 2 - jugador.getY(), -height + scene.getHeight()));
        root.setTranslateX(offsetX);
        root.setTranslateY(offsetY);
    }

    private Image cargarMapa(String ruta) {
        try {
            return new Image(getClass().getResource(ruta).toExternalForm());
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            return null;
        }
    }
}

