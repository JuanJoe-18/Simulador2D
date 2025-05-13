package structures;

import java.util.*;

public class GrafoCarreteras {
    private final Map<String, List<String>> grafo;
    private final Map<String, double[]> coordenadas; // Coordenadas de cada nodo

    public GrafoCarreteras() {
        this.grafo = new HashMap<>();
        this.coordenadas = new HashMap<>();
    }

    public void agregarInterseccion(String nodo, double x, double y) {
        grafo.putIfAbsent(nodo, new ArrayList<>());
        coordenadas.put(nodo, new double[]{x, y});
    }

    public void agregarCarretera(String origen, String destino) {
        grafo.putIfAbsent(origen, new ArrayList<>());
        grafo.putIfAbsent(destino, new ArrayList<>());
        grafo.get(origen).add(destino);
        grafo.get(destino).add(origen); // Doble sentido
    }

    public List<String> obtenerVecinos(String nodo) {
        return grafo.getOrDefault(nodo, Collections.emptyList());
    }

    public double[] obtenerCoordenadas(String nodo) {
        return coordenadas.get(nodo);
    }

    public List<String> generarRutaDFS(String origen, String destino) {
        List<String> ruta = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        if (dfs(origen, destino, visitados, ruta)) {
            return ruta;
        }
        return Collections.emptyList(); // No se encontró ruta
    }

    private boolean dfs(String actual, String destino, Set<String> visitados, List<String> ruta) {
        visitados.add(actual);
        ruta.add(actual);

        if (actual.equals(destino)) {
            return true;
        }

        for (String vecino : obtenerVecinos(actual)) {
            if (!visitados.contains(vecino)) {
                if (dfs(vecino, destino, visitados, ruta)) {
                    return true;
                }
            }
        }

        ruta.remove(ruta.size() - 1);
        return false;
    }

    public List<String> generarRutaBFS(String origen, String destino) {
        Map<String, String> predecesores = new HashMap<>();
        Queue<String> cola = new LinkedList<>();
        Set<String> visitados = new HashSet<>();

        cola.add(origen);
        visitados.add(origen);

        while (!cola.isEmpty()) {
            String actual = cola.poll();

            if (actual.equals(destino)) {
                break;
            }

            for (String vecino : obtenerVecinos(actual)) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    predecesores.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        List<String> ruta = new LinkedList<>();
        String paso = destino;
        while (paso != null && predecesores.containsKey(paso)) {
            ruta.add(0, paso);
            paso = predecesores.get(paso);
        }

        if (!ruta.isEmpty() && ruta.get(0).equals(origen)) {
            ruta.add(0, origen);
        }

        return ruta;
    }



















}