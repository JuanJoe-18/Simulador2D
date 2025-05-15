package ui;

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
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 1600;
    private Image mapa;

    @Override
    public void start(Stage primaryStage) {
        // Crear el canvas y el contexto gráfico
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Crear el grafo de carreteras
        GrafoCarreteras grafo = new GrafoCarreteras();
        inicializarGrafo(grafo);

        // Crear el carro controlado por el usuario
        Vehiculo vehiculoPersonal = new Vehiculo(0, 9 * 16);

        // Crear vehículos automáticos
        VehiculoAutomata automata1 = new VehiculoAutomata(78 * 16, 75 * 16, 1, 0, grafo);//sin moverse
        VehiculoAutomata automata2 = new VehiculoAutomata(49 * 16, 31 * 16, 0, 1, grafo);//H
        VehiculoAutomata automata3 = new VehiculoAutomata(20 * 16, 53 * 16, -1, 0, grafo);//sns
        VehiculoAutomata automata4 = new VehiculoAutomata(0, 97 * 16, 0, -1, grafo);
        VehiculoAutomata automata5 = new VehiculoAutomata(49 * 16, 9 * 16, 1, 1, grafo);

        // Asignar rutas válidas a los vehículos automáticos
        automata1.asignarRuta(grafo.generarRutaDFS("R", "C"));
        automata2.asignarRuta(grafo.generarRutaDFS("H", "T"));
        automata3.asignarRuta(grafo.generarRutaBFS("K", "A"));//tampoco se mueve
        automata4.asignarRuta(grafo.generarRutaDFS("T", "E"));
        automata5.asignarRuta(grafo.generarRutaDFS("C", "M"));

        // Crear e iniciar hilos
        new Thread(automata1).start();
        new Thread(automata2).start();
        new Thread(automata3).start();
        new Thread(automata4).start();
        new Thread(automata5).start();

        // Cargar el mapa
        try {
            mapa = new Image(getClass().getResource("/assets/MAPA.png").toExternalForm());
            System.out.println("Imagen cargada correctamente.");
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }

        // Configurar el grupo y la escena
        Group root = new Group(canvas);
        Scene scene = new Scene(root, 800, 600);

        // Dibujar el mapa inicial
        drawMap(gc, mapa);

        // Manejar las teclas para mover el vehículo
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W -> vehiculoPersonal.mover(0, -20); // UP
                case A -> vehiculoPersonal.mover(-20, 0); // LEFT
                case S -> vehiculoPersonal.mover(0, 20);  // DOWN
                case D -> vehiculoPersonal.mover(20, 0);  // RIGHT
            }

            // Redibujar el mapa y los vehículos
            gc.clearRect(0, 0, WIDTH, HEIGHT);
            drawMap(gc, mapa);
            vehiculoPersonal.dibujar(gc);

            VehiculoAutomata[] automatas = {automata1, automata2, automata3, automata4, automata5};
            for (VehiculoAutomata automata : automatas) {
                automata.moverAutomata();
                automata.dibujar(gc);
            }

            for (VehiculoAutomata automata : automatas) {
                // Detectar colisión con el vehículo controlado
                if (vehiculoPersonal.colisionaCon(automata)) {
                    System.out.println("¡Colisión con un vehículo automático!");
                }
                // Detectar colisiones entre vehículos automáticos
                for (VehiculoAutomata otroAutomata : automatas) {
                    if (automata != otroAutomata && automata.colisionaCon(otroAutomata)) {
                        System.out.println("¡Colisión entre vehículos automáticos!");
                    }
                }
            }

            // Actualizar la cámara
            actualizarCamara(root, scene, vehiculoPersonal);
        });

        // Configurar y mostrar la ventana
        primaryStage.setTitle("Simulador 2D");
        primaryStage.setScene(scene);
        primaryStage.show();
        canvas.requestFocus();
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

    public static void inicializarGrafo(GrafoCarreteras grafo) {
        grafo.agregarInterseccion("A", 0, 9 * 16);
        grafo.agregarInterseccion("B", 20 * 16, 9 * 16);
        grafo.agregarInterseccion("C", 49 * 16, 9 * 16);
        grafo.agregarInterseccion("D", 78 * 16, 9 * 16);
        grafo.agregarInterseccion("E", 99 * 16, 9 * 16);

        grafo.agregarInterseccion("F", 0, 31 * 16);
        grafo.agregarInterseccion("G", 20 * 16, 31 * 16);
        grafo.agregarInterseccion("H", 49 * 16, 31 * 16);
        grafo.agregarInterseccion("I", 78 * 16, 31 * 16);
        grafo.agregarInterseccion("J", 99 * 16, 31 * 16);

        grafo.agregarInterseccion("K", 20 * 16, 53 * 16);
        grafo.agregarInterseccion("L", 49 * 16, 53 * 16);
        grafo.agregarInterseccion("M", 78 * 16, 53 * 16);
        grafo.agregarInterseccion("N", 99 * 16, 53 * 16);

        grafo.agregarInterseccion("O", 0, 75 * 16);
        grafo.agregarInterseccion("P", 20 * 16, 75 * 16);
        grafo.agregarInterseccion("Q", 49 * 16, 75 * 16);
        grafo.agregarInterseccion("R", 78 * 16, 75 * 16);
        grafo.agregarInterseccion("S", 99 * 16, 75 * 16);

        grafo.agregarInterseccion("T", 0, 97 * 16);
        grafo.agregarInterseccion("U", 20 * 16, 97 * 16);
        grafo.agregarInterseccion("V", 49 * 16, 97 * 16);
        grafo.agregarInterseccion("W", 78 * 16, 97 * 16);
        grafo.agregarInterseccion("X", 99 * 16, 97 * 16);

        // Lista de adyacencias sin duplicados .>Ojo porque hacer esto manualmente es demasiado tedioso
        // Nodo A
        grafo.agregarCarretera("A", "B");
        // Nodo B
        grafo.agregarCarretera("B", "C");
        grafo.agregarCarretera("B", "G");
        // Nodo C
        grafo.agregarCarretera("C", "D");
        grafo.agregarCarretera("C", "H");
        // Nodo D
        grafo.agregarCarretera("D", "E");
        grafo.agregarCarretera("D", "I");
        // Nodo F
        grafo.agregarCarretera("F", "G");
        // Nodo G
        grafo.agregarCarretera("G", "H");
        grafo.agregarCarretera("G", "K");
        // Nodo H
        grafo.agregarCarretera("H", "I");
        grafo.agregarCarretera("H", "L");
        // Nodo I
        grafo.agregarCarretera("I", "J");
        grafo.agregarCarretera("I", "M");
        // Nodo K
        grafo.agregarCarretera("K", "L");
        grafo.agregarCarretera("K", "P");
        // Nodo L
        grafo.agregarCarretera("L", "M");
        grafo.agregarCarretera("L", "Q");
        // Nodo M
        grafo.agregarCarretera("M", "N");
        grafo.agregarCarretera("M", "R");
        // Nodo O
        grafo.agregarCarretera("O", "P");
        // Nodo P
        grafo.agregarCarretera("P", "Q");
        grafo.agregarCarretera("P", "U");
        // Nodo Q
        grafo.agregarCarretera("Q", "R");
        grafo.agregarCarretera("Q", "V");
        // Nodo R
        grafo.agregarCarretera("R", "S");
        grafo.agregarCarretera("R", "W");
        // Nodo T
        grafo.agregarCarretera("T", "U");
        // Nodo U
        grafo.agregarCarretera("U", "V");
        // Nodo V
        grafo.agregarCarretera("V", "W");
        // Nodo W
        grafo.agregarCarretera("W", "X");
    }
}