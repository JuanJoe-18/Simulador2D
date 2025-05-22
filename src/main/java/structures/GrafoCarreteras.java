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

    public List<String> generarRutaDijkstra(String origen, String destino) {
        Map<String, Double> distancias = new HashMap<>();
        Map<String, String> predecesores = new HashMap<>();
        PriorityQueue<String> colaPrioridad = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));
        Set<String> visitados = new HashSet<>();

        // Inicializar distancias
        for (String nodo : grafo.keySet()) {
            distancias.put(nodo, Double.MAX_VALUE);
        }
        distancias.put(origen, 0.0);
        colaPrioridad.add(origen);

        while (!colaPrioridad.isEmpty()) {
            String actual = colaPrioridad.poll();

            if (!visitados.add(actual)) {
                continue;
            }

            if (actual.equals(destino)) {
                break;
            }

            for (String vecino : obtenerVecinos(actual)) {
                if (!visitados.contains(vecino)) {
                    double[] coordActual = obtenerCoordenadas(actual);
                    double[] coordVecino = obtenerCoordenadas(vecino);
                    double distancia = Math.sqrt(Math.pow(coordVecino[0] - coordActual[0], 2) + Math.pow(coordVecino[1] - coordActual[1], 2));

                    double nuevaDistancia = distancias.get(actual) + distancia;
                    if (nuevaDistancia < distancias.get(vecino)) {
                        distancias.put(vecino, nuevaDistancia);
                        predecesores.put(vecino, actual);
                        colaPrioridad.add(vecino);
                    }
                }
            }
        }

        // Reconstruir la ruta
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

    public static void inicializarGrafo(GrafoCarreteras grafo) {
        grafo.agregarInterseccion("Inicio", 46 * 16, 75 * 16);
        grafo.agregarInterseccion("A_0", 46 * 16, 120 * 16);
        grafo.agregarInterseccion("A", 131 * 16, 120 * 16);
        grafo.agregarInterseccion("B", 151 * 16, 120 * 16);
        grafo.agregarInterseccion("C", 180 * 16, 120 * 16);
        grafo.agregarInterseccion("D", 209 * 16, 120 * 16);
        grafo.agregarInterseccion("E", 230 * 16, 120 * 16); //🆗

        grafo.agregarInterseccion("F", 131 * 16, 142 * 16);
        grafo.agregarInterseccion("G", 151 * 16, 142 * 16);
        grafo.agregarInterseccion("H", 180 * 16, 142 * 16);
        grafo.agregarInterseccion("I", 209 * 16, 142 * 16);
        grafo.agregarInterseccion("J", 230 * 16, 142 * 16);//🆗

        grafo.agregarInterseccion("K", 151 * 16, 164 * 16);
        grafo.agregarInterseccion("L", 180 * 16, 164 * 16);
        grafo.agregarInterseccion("M", 209 * 16, 164 * 16);
        grafo.agregarInterseccion("N", 230 * 16, 164 * 16);//🆗

        grafo.agregarInterseccion("O", 131 * 16, 186 * 16);
        grafo.agregarInterseccion("P", 151 * 16, 186 * 16);
        grafo.agregarInterseccion("Q", 180 * 16, 186 * 16);
        grafo.agregarInterseccion("R", 209 * 16, 186 * 16);
        grafo.agregarInterseccion("S", 230 * 16, 186 * 16);

       //en el nuevo mapa el noddo T ha desaperecido
        grafo.agregarInterseccion("U", 151 * 16, 208 * 16);
        grafo.agregarInterseccion("V", 180 * 16, 208 * 16);
        grafo.agregarInterseccion("W", 209 * 16, 208 * 16);
        grafo.agregarInterseccion("X", 230 * 16, 208 * 16);

        // Lista de adyacencias sin duplicados .>Ojo porque hacer esto manualmente es demasiado tedioso
        //Nodo Inicio
        grafo.agregarCarretera("Inicio", "A_0");
        //Nodo puente
        grafo.agregarCarretera("A_0", "A");
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
        // Nodo U
        grafo.agregarCarretera("U", "V");
        // Nodo V
        grafo.agregarCarretera("V", "W");
        // Nodo W
        grafo.agregarCarretera("W", "X");

    }
}