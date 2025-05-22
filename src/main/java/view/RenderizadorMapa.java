package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.Vehiculo;

import java.util.List;

public class RenderizadorMapa {
    public void dibujarMapa(GraphicsContext gc, Image mapa, List<Vehiculo> vehiculos) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.drawImage(mapa, 0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for (Vehiculo vehiculo : vehiculos) {
            vehiculo.dibujar(gc);
        }
    }
}