package ui;

import controllers.JugadorController;
import controllers.VehiculoAutomataController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import structures.GrafoCarreteras;
import javafx.scene.image.Image;
import model.Vehiculo;
import model.VehiculoAutomata;

public class Simulador2D extends Application {
    private static final int WIDTH = 16 * 400;
    private static final int HEIGHT = 16 * 400;

    @Override
    public void start(Stage primaryStage) {
        // Crear el canvas y el contexto gráfico
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Group root = new Group(canvas);
        Scene scene = new Scene(root,800,600);


        // Crear el grafo de carreteras
        GrafoCarreteras grafo = new GrafoCarreteras();
        GrafoCarreteras.inicializarGrafo(grafo);

        // Crear el carro controlado por el usuario
        Vehiculo vehiculoPersonal = new Vehiculo(180*16, 142 * 16);
        JugadorController jugadorController = new JugadorController(vehiculoPersonal);

        VehiculoAutomataController automataController = new VehiculoAutomataController(grafo, gc, WIDTH, HEIGHT);
        automataController.inicializarAutomatas();

        Image mapa = cargarMapa("/assets/MAPA3.0.png");
        automataController.dibujarMapa(gc, mapa, vehiculoPersonal);

        scene.setOnKeyPressed(event -> {
            jugadorController.manejarTeclas(event);
            automataController.actualizarSimulacion(gc, mapa, vehiculoPersonal);
            automataController.detectarColisiones(vehiculoPersonal);
            actualizarCamara(root, scene, vehiculoPersonal);
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                automataController.actualizarSimulacion(gc, mapa, vehiculoPersonal);
                automataController.detectarColisiones(vehiculoPersonal);
            }
        };
        timer.start();

        primaryStage.setTitle("Simulador 2D");
        primaryStage.setScene(scene);
        primaryStage.show();
        canvas.requestFocus();
    }

    private Image cargarMapa(String ruta) {
        try {
            return new Image(getClass().getResource(ruta).toExternalForm());
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            return null;
        }
    }

    private void drawMap(GraphicsContext gc, Image mapa) {
        // Dibujar el mapa como fondo
        gc.drawImage(mapa, 0, 0, WIDTH, HEIGHT);
    }

    private void actualizarCamara(Group root, Scene scene, Vehiculo vehiculo) {
        // Calcular el desplazamiento de la cámara
        double offsetX = Math.min(0, Math.max(scene.getWidth() / 2 - vehiculo.getX(), -WIDTH + scene.getWidth()));
        double offsetY = Math.min(0, Math.max(scene.getHeight() / 2 - vehiculo.getY(), -HEIGHT + scene.getHeight()));
        root.setTranslateX(offsetX);
        root.setTranslateY(offsetY);
    }

    public static void main(String[] args) {
        launch(args);
    }

}