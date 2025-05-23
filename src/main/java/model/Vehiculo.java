package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Vehiculo {
    private double x;
    private double y;
    private double width;
    private double height;

    public static final double ORIGINAL_WIDTH = 28;
    public static final double ORIGINAL_HEIGHT = 48;

    private Image imagenActual;
    private Image imagenArriba;
    private Image imagenAbajo;
    private Image imagenIzquierda;
    private Image imagenDerecha;

    public Vehiculo(double x, double y) {
        this.x = x;
        this.y = y;
        this.width = ORIGINAL_WIDTH;
        this.height = ORIGINAL_HEIGHT;

        try {
            imagenArriba = new Image(getClass().getResource("/assets/PatrolU.png").toExternalForm());
            imagenAbajo = new Image(getClass().getResource("/assets/PatrolD.png").toExternalForm());
            imagenIzquierda = new Image(getClass().getResource("/assets/PatrolL.png").toExternalForm());
            imagenDerecha = new Image(getClass().getResource("/assets/PatrolR.png").toExternalForm());
            imagenActual = imagenArriba; // Imagen inicial
        } catch (Exception e) {
            System.err.println("Error al cargar las imágenes del carro: " + e.getMessage());
        }
    }

    public void dibujar(GraphicsContext gc) {
        if (imagenActual != null) {
            gc.drawImage(imagenActual, x, y, width, height);
        } else {
            System.out.println("¡La imagen actual es nula!");
        }
    }

    public void mover(double dx, double dy) {
        x += dx;
        y += dy;

        // Cambiar la imagen y ajustar dimensiones según la dirección
        if (dx > 0) {
            imagenActual = imagenDerecha;
            width = 58;
            height = 32;
        } else if (dx < 0) {
            imagenActual = imagenIzquierda;
            width = 58;
            height = 32;
        } else if (dy > 0) {
            imagenActual = imagenAbajo;
            width = ORIGINAL_WIDTH;
            height = ORIGINAL_HEIGHT;
        } else if (dy < 0) {
            imagenActual = imagenArriba;
            width = ORIGINAL_WIDTH;
            height = ORIGINAL_HEIGHT;
        }
    }

    public boolean colisionaCon(Vehiculo otro) {
        return this.x < otro.x + otro.width &&
               this.x + this.width > otro.x &&
               this.y < otro.y + otro.height &&
               this.y + this.height > otro.y;
    }

    public void detener() {
        System.out.println("Vehículo detenido.");
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }

}