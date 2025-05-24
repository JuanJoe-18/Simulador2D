package structures;

import java.util.*;

public class GrafoCarreteras {
    private final Map<Nodo, List<Arista>> grafo;
    private final Map<String, Nodo> nodosPorNombre;

    public GrafoCarreteras() {
        this.grafo = new HashMap<>();
        this.nodosPorNombre = new HashMap<>();
    }

    public void agregarInterseccion(String nombre, double x, double y) {
        Nodo nodo = new Nodo(nombre, x, y);
        grafo.putIfAbsent(nodo, new ArrayList<>());
        nodosPorNombre.put(nombre, nodo);
    }

    public void agregarCarretera(String nombreOrigen, String nombreDestino) {
        Nodo origen = nodosPorNombre.get(nombreOrigen);
        Nodo destino = nodosPorNombre.get(nombreDestino);
        if (origen == null || destino == null) return;
        double distancia = calcularDistancia(origen, destino);
        grafo.get(origen).add(new Arista(origen, destino, distancia));
        grafo.get(destino).add(new Arista(destino, origen, distancia)); // Doble sentido
    }

    public List<Nodo> obtenerVecinos(Nodo nodo) {
        List<Nodo> vecinos = new ArrayList<>();
        for (Arista arista : grafo.getOrDefault(nodo, Collections.emptyList())) {
            vecinos.add(arista.getDestino());
        }
        return vecinos;
    }

    public Nodo obtenerNodoPorNombre(String nombre) {
        return nodosPorNombre.get(nombre);
    }

    public double[] obtenerCoordenadas(Nodo nodo) {
        if (nodo == null) return null;
        return new double[]{nodo.getX(), nodo.getY()};
    }

    public List<Nodo> obtenerNodos() {
        return new ArrayList<>(nodosPorNombre.values());
    }

    public List<Nodo> generarRutaDFS(String nombreOrigen, String nombreDestino) {
        Nodo origen = obtenerNodoPorNombre(nombreOrigen);
        Nodo destino = obtenerNodoPorNombre(nombreDestino);
        List<Nodo> ruta = new ArrayList<>();
        Set<Nodo> visitados = new HashSet<>();
        if (dfs(origen, destino, visitados, ruta)) {
            return ruta;
        }
        return Collections.emptyList();
    }

    private boolean dfs(Nodo actual, Nodo destino, Set<Nodo> visitados, List<Nodo> ruta) {
        if (actual == null) return false;
        visitados.add(actual);
        ruta.add(actual);
        if (actual.equals(destino)) {
            return true;
        }
        for (Nodo vecino : obtenerVecinos(actual)) {
            if (!visitados.contains(vecino)) {
                if (dfs(vecino, destino, visitados, ruta)) {
                    return true;
                }
            }
        }
        ruta.remove(ruta.size() - 1);
        return false;
    }

    public List<Nodo> generarRutaBFS(String nombreOrigen, String nombreDestino) {
        Nodo origen = obtenerNodoPorNombre(nombreOrigen);
        Nodo destino = obtenerNodoPorNombre(nombreDestino);
        Map<Nodo, Nodo> predecesores = new HashMap<>();
        Queue<Nodo> cola = new LinkedList<>();
        Set<Nodo> visitados = new HashSet<>();
        cola.add(origen);
        visitados.add(origen);
        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            if (actual.equals(destino)) {
                break;
            }
            for (Nodo vecino : obtenerVecinos(actual)) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    predecesores.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }
        List<Nodo> ruta = new LinkedList<>();
        Nodo paso = destino;
        while (paso != null && predecesores.containsKey(paso)) {
            ruta.add(0, paso);
            paso = predecesores.get(paso);
        }
        if (!ruta.isEmpty() && ruta.get(0).equals(origen)) {
            ruta.add(0, origen);
        }
        return ruta;
    }

    public List<Nodo> generarRutaDijkstra(String nombreOrigen, String nombreDestino) {
        Nodo origen = obtenerNodoPorNombre(nombreOrigen);
        Nodo destino = obtenerNodoPorNombre(nombreDestino);
        Map<Nodo, Double> distancias = new HashMap<>();
        Map<Nodo, Nodo> predecesores = new HashMap<>();
        PriorityQueue<Nodo> colaPrioridad = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));
        Set<Nodo> visitados = new HashSet<>();
        for (Nodo nodo : grafo.keySet()) {
            distancias.put(nodo, Double.MAX_VALUE);
        }
        distancias.put(origen, 0.0);
        colaPrioridad.add(origen);
        while (!colaPrioridad.isEmpty()) {
            Nodo actual = colaPrioridad.poll();
            if (!visitados.add(actual)) {
                continue;
            }
            if (actual.equals(destino)) {
                break;
            }
            for (Arista arista : grafo.getOrDefault(actual, Collections.emptyList())) {
                Nodo vecino = arista.getDestino();
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
        List<Nodo> ruta = new LinkedList<>();
        Nodo paso = destino;
        while (paso != null && predecesores.containsKey(paso)) {
            ruta.add(0, paso);
            paso = predecesores.get(paso);
        }
        if (!ruta.isEmpty() && ruta.get(0).equals(origen)) {
            ruta.add(0, origen);
        }
        return ruta;
    }

    private double calcularDistancia(Nodo a, Nodo b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static void inicializarGrafo(GrafoCarreteras grafo) {
        grafo.agregarInterseccion("Inicio", 46 * 16, 75 * 16);
        grafo.agregarInterseccion("A_0", 46 * 16, 120 * 16);
        grafo.agregarInterseccion("A", 131 * 16, 120 * 16);
        grafo.agregarInterseccion("B", 151 * 16, 120 * 16);
        grafo.agregarInterseccion("C", 180 * 16, 120 * 16);
        grafo.agregarInterseccion("D", 209 * 16, 120 * 16);
        grafo.agregarInterseccion("E", 230 * 16, 120 * 16);

        grafo.agregarInterseccion("F", 131 * 16, 142 * 16);
        grafo.agregarInterseccion("G", 151 * 16, 142 * 16);
        grafo.agregarInterseccion("H", 180 * 16, 142 * 16);
        grafo.agregarInterseccion("I", 209 * 16, 142 * 16);
        grafo.agregarInterseccion("J", 230 * 16, 142 * 16);

        grafo.agregarInterseccion("K", 151 * 16, 164 * 16);
        grafo.agregarInterseccion("L", 180 * 16, 164 * 16);
        grafo.agregarInterseccion("M", 209 * 16, 164 * 16);
        grafo.agregarInterseccion("N", 230 * 16, 164 * 16);

        grafo.agregarInterseccion("O", 131 * 16, 186 * 16);
        grafo.agregarInterseccion("P", 151 * 16, 186 * 16);
        grafo.agregarInterseccion("Q", 180 * 16, 186 * 16);
        grafo.agregarInterseccion("R", 209 * 16, 186 * 16);
        grafo.agregarInterseccion("S", 230 * 16, 186 * 16);

        grafo.agregarInterseccion("U", 151 * 16, 208 * 16);
        grafo.agregarInterseccion("V", 180 * 16, 208 * 16);
        grafo.agregarInterseccion("W", 209 * 16, 208 * 16);
        grafo.agregarInterseccion("X", 230 * 16, 208 * 16);

        grafo.agregarCarretera("Inicio", "A_0");
        grafo.agregarCarretera("A_0", "A");
        grafo.agregarCarretera("A", "B");
        grafo.agregarCarretera("B", "C");
        grafo.agregarCarretera("B", "G");
        grafo.agregarCarretera("C", "D");
        grafo.agregarCarretera("C", "H");
        grafo.agregarCarretera("D", "E");
        grafo.agregarCarretera("D", "I");
        grafo.agregarCarretera("F", "G");
        grafo.agregarCarretera("G", "H");
        grafo.agregarCarretera("G", "K");
        grafo.agregarCarretera("H", "I");
        grafo.agregarCarretera("H", "L");
        grafo.agregarCarretera("I", "J");
        grafo.agregarCarretera("I", "M");
        grafo.agregarCarretera("K", "L");
        grafo.agregarCarretera("K", "P");
        grafo.agregarCarretera("L", "M");
        grafo.agregarCarretera("L", "Q");
        grafo.agregarCarretera("M", "N");
        grafo.agregarCarretera("M", "R");
        grafo.agregarCarretera("O", "P");
        grafo.agregarCarretera("P", "Q");
        grafo.agregarCarretera("P", "U");
        grafo.agregarCarretera("Q", "R");
        grafo.agregarCarretera("Q", "V");
        grafo.agregarCarretera("R", "S");
        grafo.agregarCarretera("R", "W");
        grafo.agregarCarretera("U", "V");
        grafo.agregarCarretera("V", "W");
        grafo.agregarCarretera("W", "X");
    }
}

