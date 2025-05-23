package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MatrizLoader {
    public static int[][] cargarMatrizDesdeCSV(String rutaArchivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int filas = 0;
            int columnas = 0;

            // Determinar dimensiones de la matriz
            while ((linea = br.readLine()) != null) {
                filas++;
                if (columnas == 0) {
                    columnas = linea.split(",").length;
                }
            }

            // Reiniciar el lector para cargar los datos
            int[][] matriz = new int[filas][columnas];
            br.close();

            try (BufferedReader br2 = new BufferedReader(new FileReader(rutaArchivo))) {
                int i = 0;
                while ((linea = br2.readLine()) != null) {
                    String[] valores = linea.split(",");
                    for (int j = 0; j < valores.length; j++) {
                        matriz[i][j] = Integer.parseInt(valores[j]);
                    }
                    i++;
                }
            }

            return matriz;
        }
    }
}