package ui;

import controllers.GameController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Simulador2D extends Application {
    private static final int WIDTH = 16 * 400;
    private static final int HEIGHT = 16 * 400;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Group root = new Group(canvas);
        Scene scene = new Scene(root, 800, 600);

        GameController gameController;
        try {
            gameController = new GameController(gc, root, scene, WIDTH, HEIGHT);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        gameController.renderizar(gc);

        scene.setOnKeyPressed(event -> {
            gameController.manejarTecla(event);
            gameController.actualizarSimulacion();
            gameController.renderizar(gc);
            gameController.detectarColisiones();
            gameController.actualizarCamara();
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameController.actualizarSimulacion();
                gameController.renderizar(gc);
                gameController.detectarColisiones();
                gameController.actualizarCamara();
            }
        };
        timer.start();

        primaryStage.setTitle("Simulador 2D");
        primaryStage.setScene(scene);
        primaryStage.show();
        canvas.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

