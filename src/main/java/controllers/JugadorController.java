package controllers;

import javafx.scene.input.KeyEvent;
import model.Vehiculo;

public class JugadorController {

    private final Vehiculo jugador;

    public JugadorController(Vehiculo jugador) {
        this.jugador = jugador;
    }

    public void manejarTeclas(KeyEvent tecleo){
        switch (tecleo.getCode()){
            case W :
                jugador.mover(0,-20);
                break;
            case A:
                jugador.mover(-20,0);
                break;
            case S:
                jugador.mover(0,20);
                break;
            case D:
                jugador.mover(20,0);
                break;
        }
    }
}


