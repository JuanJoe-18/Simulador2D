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

    // Método para agregar una carretera dirigida (solo un sentido)
    public void agregarCarreteraDirigida(String nombreOrigen, String nombreDestino, double peso) {
        Nodo origen = nodosPorNombre.get(nombreOrigen);
        Nodo destino = nodosPorNombre.get(nombreDestino);
        if (origen == null || destino == null) return;
        grafo.get(origen).add(new Arista(origen, destino, peso));
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

    // Grafo dirigido especial para los autómatas
    public void initializeMapPaths() {
        // NODOS
        agregarInterseccion("A1", 720.0, 1210.0);
        agregarInterseccion("A2", 750.0, 1210.0);
        agregarInterseccion("B1", 720.0, 1945.0);
        agregarInterseccion("B2", 765.0, 1910.0);
        agregarInterseccion("C1", 2405.0, 1910.0);
        agregarInterseccion("C2", 2445.0, 1910.0);
        agregarInterseccion("C3", 2405.0, 1945.0);
        agregarInterseccion("C4", 2445.0, 1945.0);
        agregarInterseccion("D1", 2865.0, 1910.0);
        agregarInterseccion("D2", 2910.0, 1910.0);
        agregarInterseccion("D3", 2865.0, 1945.0);
        agregarInterseccion("D4", 2910.0, 1945.0);
        agregarInterseccion("E1", 3330.0, 1910.0);
        agregarInterseccion("E2", 3365.0, 1910.0);
        agregarInterseccion("E3", 3330.0, 1945.0);
        agregarInterseccion("E4", 3365.0, 1945.0);
        agregarInterseccion("F1", 3690.0, 1910.0);
        agregarInterseccion("F2", 3690.0, 1945.0);
        agregarInterseccion("G1", 2085.0, 2260.0);
        agregarInterseccion("G2", 2085.0, 2295.0);
        agregarInterseccion("H1", 2405.0, 2260.0);
        agregarInterseccion("H2", 2445.0, 2260.0);
        agregarInterseccion("H3", 2405.0, 2295.0);
        agregarInterseccion("H4", 2445.0, 2295.0);
        agregarInterseccion("I1", 2865.0, 2260.0);
        agregarInterseccion("I2", 2910.0, 2260.0);
        agregarInterseccion("I3", 2865.0, 2295.0);
        agregarInterseccion("I4", 2910.0, 2295.0);
        agregarInterseccion("J1", 3330.0, 2260.0);
        agregarInterseccion("J2", 3365.0, 2260.0);
        agregarInterseccion("J3", 3330.0, 2295.0);
        agregarInterseccion("J4", 3365.0, 2295.0);
        agregarInterseccion("K1", 3690.0, 2260.0);
        agregarInterseccion("K2", 3690.0, 2295.0);
        agregarInterseccion("L1", 2405.0, 2610.0);
        agregarInterseccion("L2", 2445.0, 2610.0);
        agregarInterseccion("L3", 2405.0, 2650.0);
        agregarInterseccion("L4", 2445.0, 2650.0);
        agregarInterseccion("M1", 2865.0, 2610.0);
        agregarInterseccion("M2", 2910.0, 2610.0);
        agregarInterseccion("M3", 2865.0, 2650.0);
        agregarInterseccion("M4", 2910.0, 2650.0);
        agregarInterseccion("N1", 3330.0, 2610.0);
        agregarInterseccion("N2", 3365.0, 2610.0);
        agregarInterseccion("N3", 3330.0, 2650.0);
        agregarInterseccion("N4", 3365.0, 2650.0);
        agregarInterseccion("O1", 3690.0, 2610.0);
        agregarInterseccion("O2", 3690.0, 2650.0);
        agregarInterseccion("P1", 245.0, 2965.0);
        agregarInterseccion("P2", 245.0, 3005.0);
        agregarInterseccion("Q1", 2405.0, 2965.0);
        agregarInterseccion("Q2", 2445.0, 2965.0);
        agregarInterseccion("Q3", 2405.0, 3005.0);
        agregarInterseccion("Q4", 2445.0, 3005.0);
        agregarInterseccion("R1", 2865.0, 2965.0);
        agregarInterseccion("R2", 2910.0, 2965.0);
        agregarInterseccion("R3", 2865.0, 3005.0);
        agregarInterseccion("R4", 2910.0, 3005.0);
        agregarInterseccion("S1", 3330.0 , 2965.0);
        agregarInterseccion("S2", 3365.0, 2965.0);
        agregarInterseccion("S3", 3330.0, 3005.0);
        agregarInterseccion("S4", 3365.0, 3005.0);
        agregarInterseccion("T1", 3690.0, 2965.0);
        agregarInterseccion("T2", 3690.0, 3005.0);
        agregarInterseccion("U1", 2405.0, 3315.0);
        agregarInterseccion("U2", 2445.0, 3315.0);
        agregarInterseccion("U3", 2405.0, 3355.0);
        agregarInterseccion("U4", 2445.0, 3355.0);
        agregarInterseccion("V1", 2865.0, 3315.0);
        agregarInterseccion("V2", 2910.0, 3315.0);
        agregarInterseccion("V3", 2865.0, 3355.0);
        agregarInterseccion("V4", 2910.0, 3355.0);
        agregarInterseccion("W1", 3330.0, 3315.0);
        agregarInterseccion("W2", 3365.0, 3315.0);
        agregarInterseccion("W3", 3330.0, 3355.0);
        agregarInterseccion("W4", 3365.0, 3355.0);
        agregarInterseccion("X1", 3690.0, 3315.0);
        agregarInterseccion("X2", 3690.0, 3355.0);

        // ARISTAS DIRIGIDAS
        agregarCarreteraDirigida("A2", "A1", 1.0);
        agregarCarreteraDirigida("A1", "B1", 6.0);
        agregarCarreteraDirigida("B2", "A1", 5.0);
        agregarCarreteraDirigida("C1", "B2", 4.0);
        agregarCarreteraDirigida("B1", "C3", 5.0);
        agregarCarreteraDirigida("C1", "C3", 1.0);
        agregarCarreteraDirigida("C3", "C4", 1.0);
        agregarCarreteraDirigida("C4", "C2", 1.0);
        agregarCarreteraDirigida("C2", "C1", 1.0);
        agregarCarreteraDirigida("D1", "C2", 6.0);
        agregarCarreteraDirigida("C4", "D3", 6.0);
        agregarCarreteraDirigida("D1", "D3", 1.0);
        agregarCarreteraDirigida("D3", "D4", 1.0);
        agregarCarreteraDirigida("D4", "D2", 1.0);
        agregarCarreteraDirigida("D2", "D1", 1.0);
        agregarCarreteraDirigida("D4", "E3", 1.0);
        agregarCarreteraDirigida("E1", "D2", 6.0);
        agregarCarreteraDirigida("E1", "E3", 1.0);
        agregarCarreteraDirigida("E3", "E4", 1.0);
        agregarCarreteraDirigida("E4", "E2", 1.0);
        agregarCarreteraDirigida("E2", "E1", 1.0);
        agregarCarreteraDirigida("E4", "F2", 3.0);
        agregarCarreteraDirigida("F2", "F1", 1.0);
        agregarCarreteraDirigida("F1", "E2", 3.0);
        agregarCarreteraDirigida("C3", "H1", 4.0);
        agregarCarreteraDirigida("H2", "C4", 3.0);
        agregarCarreteraDirigida("D3", "I1", 5.0);
        agregarCarreteraDirigida("I2", "D4", 3.0);
        agregarCarreteraDirigida("E3", "J1", 2.0);
        agregarCarreteraDirigida("J2", "D4", 3.0);
        agregarCarreteraDirigida("G2", "G1", 1.0);
        agregarCarreteraDirigida("H1", "G2", 3.0);
        agregarCarreteraDirigida("G1", "H3", 3.0);
        agregarCarreteraDirigida("H1", "H3", 1.0);
        agregarCarreteraDirigida("H3", "H4", 1.0);
        agregarCarreteraDirigida("H4", "H2", 1.0);
        agregarCarreteraDirigida("H2", "H1", 1.0);
        agregarCarreteraDirigida("H4", "I3", 5.0);
        agregarCarreteraDirigida("I1", "H2", 4.0);
        agregarCarreteraDirigida("I1", "I3", 1.0);
        agregarCarreteraDirigida("I3", "I4", 1.0);
        agregarCarreteraDirigida("I4", "I2", 1.0);
        agregarCarreteraDirigida("I2", "I1", 1.0);
        agregarCarreteraDirigida("I4", "J3", 4.0);
        agregarCarreteraDirigida("J1", "I2", 6.0);
        agregarCarreteraDirigida("J1", "J3", 1.0);
        agregarCarreteraDirigida("J3", "J4", 1.0);
        agregarCarreteraDirigida("J4", "J2", 1.0);
        agregarCarreteraDirigida("J2", "J1", 1.0);
        agregarCarreteraDirigida("J4", "K2", 3.0);
        agregarCarreteraDirigida("K2", "K1", 1.0);
        agregarCarreteraDirigida("K1", "J2", 3.0);
        agregarCarreteraDirigida("H3", "L1", 3.0);
        agregarCarreteraDirigida("L2", "H4", 3.0);
        agregarCarreteraDirigida("I3", "M4", 2.0);
        agregarCarreteraDirigida("M2", "I4", 4.0);
        agregarCarreteraDirigida("J3", "N1", 3.0);
        agregarCarreteraDirigida("N2", "J4", 3.0);
        agregarCarreteraDirigida("L1", "L3", 1.0);
        agregarCarreteraDirigida("L3", "L4", 1.0);
        agregarCarreteraDirigida("L4", "L2", 1.0);
        agregarCarreteraDirigida("L2", "L1", 1.0);
        agregarCarreteraDirigida("L4", "M3", 3.0);
        agregarCarreteraDirigida("M1", "L2", 2.0);
        agregarCarreteraDirigida("M1", "M3", 1.0);
        agregarCarreteraDirigida("M3", "M4", 1.0);
        agregarCarreteraDirigida("M4", "M2", 1.0);
        agregarCarreteraDirigida("M2", "M1", 1.0);
        agregarCarreteraDirigida("M4", "N3", 2.0);
        agregarCarreteraDirigida("N1", "M2", 2.0);
        agregarCarreteraDirigida("N1", "N3", 1.0);
        agregarCarreteraDirigida("N3", "N4", 1.0);
        agregarCarreteraDirigida("N4", "N2", 1.0);
        agregarCarreteraDirigida("N2", "N1", 1.0);
        agregarCarreteraDirigida("N4", "O2", 3.0);
        agregarCarreteraDirigida("O2", "O1", 1.0);
        agregarCarreteraDirigida("O1", "N2", 1.0);
        agregarCarreteraDirigida("L3", "Q1", 2.0);
        agregarCarreteraDirigida("Q2", "L4", 3.0);
        agregarCarreteraDirigida("M3", "R1", 3.0);
        agregarCarreteraDirigida("R2", "M4", 4.0);
        agregarCarreteraDirigida("N3", "S1", 6.0);
        agregarCarreteraDirigida("S2", "N4", 5.0);
        agregarCarreteraDirigida("Q1", "P1", 20.0);
        agregarCarreteraDirigida("P1", "P2", 1.0);
        agregarCarreteraDirigida("P2", "Q3", 20.0);
        agregarCarreteraDirigida("Q1", "Q3", 1.0);
        agregarCarreteraDirigida("Q3", "Q4", 1.0);
        agregarCarreteraDirigida("Q4", "Q2", 1.0);
        agregarCarreteraDirigida("Q2", "Q1", 1.0);
        agregarCarreteraDirigida("Q4", "R3", 4.0);
        agregarCarreteraDirigida("R1", "Q2", 6.0);
        agregarCarreteraDirigida("R1", "R3", 1.0);
        agregarCarreteraDirigida("R3", "R4", 1.0);
        agregarCarreteraDirigida("R4", "R2", 1.0);
        agregarCarreteraDirigida("R2", "R1", 1.0);
        agregarCarreteraDirigida("R4", "S3", 6.0);
        agregarCarreteraDirigida("S1", "R2", 4.0);
        agregarCarreteraDirigida("S1", "S3", 1.0);
        agregarCarreteraDirigida("S3", "S4", 1.0);
        agregarCarreteraDirigida("S4", "S2", 1.0);
        agregarCarreteraDirigida("S2", "S1", 1.0);
        agregarCarreteraDirigida("S4", "T2", 3.0);
        agregarCarreteraDirigida("T2", "T1", 1.0);
        agregarCarreteraDirigida("T1", "S2", 3.0);
        agregarCarreteraDirigida("Q3", "U1", 4.0);
        agregarCarreteraDirigida("U2", "Q4", 2.0);
        agregarCarreteraDirigida("R3", "V1", 4.0);
        agregarCarreteraDirigida("V2", "R4", 2.0);
        agregarCarreteraDirigida("S3", "W1", 2.0);
        agregarCarreteraDirigida("W2", "S4", 3.0);
        agregarCarreteraDirigida("U1", "U3", 1.0);
        agregarCarreteraDirigida("U3", "U4", 1.0);
        agregarCarreteraDirigida("U4", "U2", 1.0);
        agregarCarreteraDirigida("U2", "U1", 1.0);
        agregarCarreteraDirigida("U4", "V3", 2.0);
        agregarCarreteraDirigida("V1", "U2", 3.0);
        agregarCarreteraDirigida("V1", "V3", 1.0);
        agregarCarreteraDirigida("V3", "V4", 1.0);
        agregarCarreteraDirigida("V4", "V2", 1.0);
        agregarCarreteraDirigida("V2", "V1", 1.0);
        agregarCarreteraDirigida("V4", "W3", 3.0);
        agregarCarreteraDirigida("W1", "V2", 2.0);
        agregarCarreteraDirigida("W1", "W3", 1.0);
        agregarCarreteraDirigida("W3", "W4", 1.0);
        agregarCarreteraDirigida("W4", "W2", 1.0);
        agregarCarreteraDirigida("W2", "W1", 1.0);
        agregarCarreteraDirigida("W4", "X2", 3.0);
        agregarCarreteraDirigida("X2", "X1", 1.0);
        agregarCarreteraDirigida("X1", "W2", 3.0);
    }
}
