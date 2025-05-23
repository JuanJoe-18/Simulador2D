package structures;

import java.util.*;

public class Grafo<T> {
    private final Map<Nodo<T>, List<Arista<T>>> grafo;

    public Grafo() {
        this.grafo = new HashMap<>();
    }

    public void agregarNodo(T nombre, double x, double y) {
        Nodo<T> nodo = new Nodo<>(nombre, x, y);
        grafo.putIfAbsent(nodo, new ArrayList<>());
    }

    public void agregarArista(T origen, T destino) {
        Nodo<T> nodoOrigen = buscarNodo(origen);
        Nodo<T> nodoDestino = buscarNodo(destino);

        if (nodoOrigen != null && nodoDestino != null) {
            double distancia = calcularDistancia(nodoOrigen, nodoDestino);
            Arista<T> arista = new Arista<>(nodoOrigen, nodoDestino, distancia);
            grafo.get(nodoOrigen).add(arista);
            grafo.get(nodoDestino).add(new Arista<>(nodoDestino, nodoOrigen, distancia)); // Doble sentido
        }
    }

    public List<Nodo<T>> obtenerVecinos(T nombre) {
        Nodo<T> nodo = buscarNodo(nombre);
        if (nodo != null) {
            List<Nodo<T>> vecinos = new ArrayList<>();
            for (Arista<T> arista : grafo.getOrDefault(nodo, Collections.emptyList())) {
                vecinos.add(arista.getDestino());
            }
            return vecinos;
        }
        return Collections.emptyList();
    }

    public List<T> generarRutaDFS(T origen, T destino) {
        List<T> ruta = new ArrayList<>();
        Set<Nodo<T>> visitados = new HashSet<>();
        Nodo<T> nodoOrigen = buscarNodo(origen);
        Nodo<T> nodoDestino = buscarNodo(destino);

        if (nodoOrigen != null && nodoDestino != null && dfs(nodoOrigen, nodoDestino, visitados, ruta)) {
            return ruta;
        }
        return Collections.emptyList();
    }

    private boolean dfs(Nodo<T> actual, Nodo<T> destino, Set<Nodo<T>> visitados, List<T> ruta) {
        visitados.add(actual);
        ruta.add(actual.getNombre());

        if (actual.equals(destino)) {
            return true;
        }

        for (Arista<T> arista : grafo.getOrDefault(actual, Collections.emptyList())) {
            Nodo<T> vecino = arista.getDestino();
            if (!visitados.contains(vecino)) {
                if (dfs(vecino, destino, visitados, ruta)) {
                    return true;
                }
            }
        }

        ruta.remove(ruta.size() - 1);
        return false;
    }

    public List<T> generarRutaBFS(T origen, T destino) {
        Map<Nodo<T>, Nodo<T>> predecesores = new HashMap<>();
        Queue<Nodo<T>> cola = new LinkedList<>();
        Set<Nodo<T>> visitados = new HashSet<>();

        Nodo<T> nodoOrigen = buscarNodo(origen);
        Nodo<T> nodoDestino = buscarNodo(destino);

        if (nodoOrigen == null || nodoDestino == null) {
            return Collections.emptyList();
        }

        cola.add(nodoOrigen);
        visitados.add(nodoOrigen);

        while (!cola.isEmpty()) {
            Nodo<T> actual = cola.poll();

            if (actual.equals(nodoDestino)) {
                break;
            }

            for (Arista<T> arista : grafo.getOrDefault(actual, Collections.emptyList())) {
                Nodo<T> vecino = arista.getDestino();
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    predecesores.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        List<T> ruta = new LinkedList<>();
        Nodo<T> paso = nodoDestino;
        while (paso != null && predecesores.containsKey(paso)) {
            ruta.add(0, paso.getNombre());
            paso = predecesores.get(paso);
        }

        if (!ruta.isEmpty() && ruta.get(0).equals(origen)) {
            ruta.add(0, origen);
        }

        return ruta;
    }

    public List<T> generarRutaDijkstra(T origen, T destino) {
        Map<Nodo<T>, Double> distancias = new HashMap<>();
        Map<Nodo<T>, Nodo<T>> predecesores = new HashMap<>();
        PriorityQueue<Nodo<T>> colaPrioridad = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));
        Set<Nodo<T>> visitados = new HashSet<>();

        Nodo<T> nodoOrigen = buscarNodo(origen);
        Nodo<T> nodoDestino = buscarNodo(destino);

        if (nodoOrigen == null || nodoDestino == null) {
            return Collections.emptyList();
        }

        for (Nodo<T> nodo : grafo.keySet()) {
            distancias.put(nodo, Double.MAX_VALUE);
        }
        distancias.put(nodoOrigen, 0.0);
        colaPrioridad.add(nodoOrigen);

        while (!colaPrioridad.isEmpty()) {
            Nodo<T> actual = colaPrioridad.poll();

            if (!visitados.add(actual)) {
                continue;
            }

            if (actual.equals(nodoDestino)) {
                break;
            }

            for (Arista<T> arista : grafo.getOrDefault(actual, Collections.emptyList())) {
                Nodo<T> vecino = arista.getDestino();
                if (!visitados.contains(vecino)) {
                    double nuevaDistancia = distancias.get(actual) + arista.getDistancia();
                    if (nuevaDistancia < distancias.get(vecino)) {
                        distancias.put(vecino, nuevaDistancia);
                        predecesores.put(vecino, actual);
                        colaPrioridad.add(vecino);
                    }
                }
            }
        }

        List<T> ruta = new LinkedList<>();
        Nodo<T> paso = nodoDestino;
        while (paso != null && predecesores.containsKey(paso)) {
            ruta.add(0, paso.getNombre());
            paso = predecesores.get(paso);
        }

        if (!ruta.isEmpty() && ruta.get(0).equals(origen)) {
            ruta.add(0, origen);
        }

        return ruta;
    }

    public Nodo<T> buscarNodo(T nombre) {
        for (Nodo<T> nodo : grafo.keySet()) {
            if (nodo.getNombre().equals(nombre)) {
                return nodo;
            }
        }
        return null;
    }

    public double[] obtenerCoordenadas(T nombre) {
        for (Nodo<T> nodo : grafo.keySet()) {
            if (nodo.getNombre().equals(nombre)) {
                return new double[]{nodo.getX(), nodo.getY()};
            }
        }
        return null;
    }

    private double calcularDistancia(Nodo<T> origen, Nodo<T> destino) {
        double dx = destino.getX() - origen.getX();
        double dy = destino.getY() - origen.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}