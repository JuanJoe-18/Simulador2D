package ui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import model.Carro;


public class Simulador2D extends Application {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 1600;
    private Image mapa;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //Creamos el carro
        Carro carro = new Carro(100, 100);


        try {
            mapa = new Image(getClass().getResource("/assets/MAPA.png").toExternalForm());
            System.out.println("Imagen cargada correctamente.");
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
        //Dibujamos el mapa inicial
        drawMap(gc, mapa);

        Group root = new Group(canvas);
        Scene scene = new Scene(root, 800, 600);

        scene.setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case W -> carro.mover(0, -5); //UP
                        case A -> carro.mover(-5, 0); // LEFT
                        case S -> carro.mover(0, 5);  // DOWN
                        case D -> carro.mover(5, 0);  // RIGHT
                    } // la velocidad seria de 5 pixeles por cada tecla
                    //Redibujamos el mapa y el carro
                    gc.clearRect(0, 0, WIDTH, HEIGHT);
                    drawMap(gc, mapa);
                    carro.dibujar(gc);

                    //Actualizamos la camara
                    double offsetX = Math.min(0, Math.max(scene.getWidth() / 2 - carro.getX(), -WIDTH + scene.getWidth()));
                    double offsetY = Math.min(0, Math.max(scene.getHeight() / 2 - carro.getY(), -HEIGHT + scene.getHeight()));
                    root.setTranslateX(offsetX);
                    root.setTranslateY(offsetY);
                }
        );
        primaryStage.setTitle("Simulador 2D");
        primaryStage.setScene(scene);
        primaryStage.show();
        canvas.requestFocus();
    }

    public void drawMap(GraphicsContext gc, Image mapa) {
        //Dibujamos el mapa como fondo
        gc.drawImage(mapa, 0, 0, WIDTH, HEIGHT);

    }


    public static void main(String[] args) {
        launch(args);
    }

}
