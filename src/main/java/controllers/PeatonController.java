package controllers;

            import javafx.scene.canvas.GraphicsContext;
            import model.Automata;
            import model.Peaton;
            import utils.MatrizLoader;

            import java.io.IOException;
            import java.util.ArrayList;
            import java.util.List;

            public class PeatonController {
                private List<Automata> automatas;
                private int[][] matrizColisiones;
                private int anchoMapa;
                private int altoMapa;

                public PeatonController(int anchoMapa, int altoMapa, String rutaMatriz) throws IOException {
                    this.anchoMapa = anchoMapa;
                    this.altoMapa = altoMapa;
                    this.automatas = new ArrayList<>();
                    this.matrizColisiones = MatrizLoader.cargarMatrizDesdeCSV(rutaMatriz);
                }

                public void inicializarPeatones(int cantidad) {
                    for (int i = 0; i < cantidad; i++) {
                        double x, y;
                        int indiceX, indiceY;

                        // Intentar generar un peatón en una posición transitable
                        do {
                            x = Math.random() * anchoMapa;
                            y = Math.random() * altoMapa;

                            // Convertir coordenadas a índices de la matriz
                            indiceX = (int) (x / 16);
                            indiceY = (int) (y / 16);
                        } while (indiceX < 0 || indiceX >= matrizColisiones[0].length ||
                                 indiceY < 0 || indiceY >= matrizColisiones.length ||
                                 (matrizColisiones[indiceY][indiceX] != 1 && matrizColisiones[indiceY][indiceX] != 2));

                        // Generar velocidades aleatorias
                        double velocidadX = (Math.random() - 0.5) * 2;
                        double velocidadY = (Math.random() - 0.5) * 2;

                        // Crear peatón con la matriz de colisiones
                        automatas.add(new Peaton(x, y, velocidadX, velocidadY, matrizColisiones));
                    }
                }

                public void actualizarAutomatas() {
                    for (Automata automata : automatas) {
                        automata.mover();
                    }
                }

                public void dibujarAutomatas(GraphicsContext gc) {
                    for (Automata automata : automatas) {
                        automata.dibujar(gc);
                    }
                }
            }