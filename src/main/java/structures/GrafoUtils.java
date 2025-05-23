package structures;

public class GrafoUtils {
    public static void inicializarGrafo(Grafo<String> grafo) {
        // Agregar nodos
        grafo.agregarNodo("Inicio", 46 * 16, 75 * 16);
        grafo.agregarNodo("A_0", 46 * 16, 120 * 16);
        grafo.agregarNodo("A", 131 * 16, 120 * 16);
        grafo.agregarNodo("B", 151 * 16, 120 * 16);
        grafo.agregarNodo("C", 180 * 16, 120 * 16);
        grafo.agregarNodo("D", 209 * 16, 120 * 16);
        grafo.agregarNodo("E", 230 * 16, 120 * 16);

        grafo.agregarNodo("F", 131 * 16, 142 * 16);
        grafo.agregarNodo("G", 151 * 16, 142 * 16);
        grafo.agregarNodo("H", 180 * 16, 142 * 16);
        grafo.agregarNodo("I", 209 * 16, 142 * 16);
        grafo.agregarNodo("J", 230 * 16, 142 * 16);

        grafo.agregarNodo("K", 151 * 16, 164 * 16);
        grafo.agregarNodo("L", 180 * 16, 164 * 16);
        grafo.agregarNodo("M", 209 * 16, 164 * 16);
        grafo.agregarNodo("N", 230 * 16, 164 * 16);

        grafo.agregarNodo("O", 131 * 16, 186 * 16);
        grafo.agregarNodo("P", 151 * 16, 186 * 16);
        grafo.agregarNodo("Q", 180 * 16, 186 * 16);
        grafo.agregarNodo("R", 209 * 16, 186 * 16);
        grafo.agregarNodo("S", 230 * 16, 186 * 16);

        grafo.agregarNodo("U", 151 * 16, 208 * 16);
        grafo.agregarNodo("V", 180 * 16, 208 * 16);
        grafo.agregarNodo("W", 209 * 16, 208 * 16);
        grafo.agregarNodo("X", 230 * 16, 208 * 16);

        // Agregar aristas
        grafo.agregarArista("Inicio", "A_0");
        grafo.agregarArista("A_0", "A");
        grafo.agregarArista("A", "B");
        grafo.agregarArista("B", "C");
        grafo.agregarArista("B", "G");
        grafo.agregarArista("C", "D");
        grafo.agregarArista("C", "H");
        grafo.agregarArista("D", "E");
        grafo.agregarArista("D", "I");
        grafo.agregarArista("F", "G");
        grafo.agregarArista("G", "H");
        grafo.agregarArista("G", "K");
        grafo.agregarArista("H", "I");
        grafo.agregarArista("H", "L");
        grafo.agregarArista("I", "J");
        grafo.agregarArista("I", "M");
        grafo.agregarArista("K", "L");
        grafo.agregarArista("K", "P");
        grafo.agregarArista("L", "M");
        grafo.agregarArista("L", "Q");
        grafo.agregarArista("M", "N");
        grafo.agregarArista("M", "R");
        grafo.agregarArista("O", "P");
        grafo.agregarArista("P", "Q");
        grafo.agregarArista("P", "U");
        grafo.agregarArista("Q", "R");
        grafo.agregarArista("Q", "V");
        grafo.agregarArista("R", "S");
        grafo.agregarArista("R", "W");
        grafo.agregarArista("U", "V");
        grafo.agregarArista("V", "W");
        grafo.agregarArista("W", "X");
    }
}