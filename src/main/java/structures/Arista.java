package structures;

public class Arista<T> {
    private Nodo<T> origen;
    private Nodo<T> destino;
    private double distancia;

    public Arista(Nodo<T> origen, Nodo<T> destino, double distancia) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
    }

    public Nodo<T> getOrigen() {
        return origen;
    }

    public void setOrigen(Nodo<T> origen) {
        this.origen = origen;
    }

    public Nodo<T> getDestino() {
        return destino;
    }

    public void setDestino(Nodo<T> destino) {
        this.destino = destino;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
}