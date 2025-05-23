package model;

    import javafx.scene.canvas.GraphicsContext;
    import javafx.scene.paint.Color;

    public class Peaton implements Automata {
        private double x, y;
        private double velocidadX, velocidadY;
        private int[][] matrizColisiones;
        private double objetivoX, objetivoY;
        private boolean visible;

        public Peaton(double x, double y, double velocidadX, double velocidadY, int[][] matrizColisiones) {
            this.x = x;
            this.y = y;
            this.velocidadX = velocidadX;
            this.velocidadY = velocidadY;
            this.matrizColisiones = matrizColisiones;
            this.visible = true; // Inicialmente visible
            establecerNuevoObjetivo();
        }

        @Override
        public void mover() {
            double nuevoX = x + velocidadX;
            double nuevoY = y + velocidadY;

            // Convertir coordenadas a índices de la matriz
            int indiceX = (int) (nuevoX / 16);
            int indiceY = (int) (nuevoY / 16);

            // Verificar si la nueva posición es transitable
            if (indiceX >= 0 && indiceX < matrizColisiones[0].length &&
                indiceY >= 0 && indiceY < matrizColisiones.length) {

                int valorCelda = matrizColisiones[indiceY][indiceX];

                if (valorCelda == 1) {
                    // Zona transitable, reaparece
                    visible = true;
                    x = nuevoX;
                    y = nuevoY;
                } else if (valorCelda == 2) {
                    // Zona no visible, se oculta
                    visible = false;
                    x = nuevoX;
                    y = nuevoY;
                } else {
                    // No transitable, cambiar dirección
                    establecerNuevoObjetivo();
                }
            } else {
                // Fuera de límites, cambiar dirección
                establecerNuevoObjetivo();
            }

            // Verificar si alcanzó el objetivo
            if (Math.hypot(objetivoX - x, objetivoY - y) < 5) {
                establecerNuevoObjetivo();
            }
        }

        private void establecerNuevoObjetivo() {
            int indiceX, indiceY;
            do {
                objetivoX = Math.random() * matrizColisiones[0].length * 16;
                objetivoY = Math.random() * matrizColisiones.length * 16;

                // Convertir coordenadas a índices de la matriz
                indiceX = (int) (objetivoX / 16);
                indiceY = (int) (objetivoY / 16);
            } while (indiceX < 0 || indiceX >= matrizColisiones[0].length ||
                     indiceY < 0 || indiceY >= matrizColisiones.length ||
                     (matrizColisiones[indiceY][indiceX] != 1 && matrizColisiones[indiceY][indiceX] != 2));

            // Calcular nueva dirección hacia el objetivo
            double deltaX = objetivoX - x;
            double deltaY = objetivoY - y;
            double magnitud = Math.hypot(deltaX, deltaY);
            velocidadX = (deltaX / magnitud) * 1.5; // Ajustar velocidad
            velocidadY = (deltaY / magnitud) * 1.5;
        }

        @Override
        public void dibujar(GraphicsContext gc) {
            if (visible) {
                gc.setFill(Color.RED);
                gc.fillOval(x, y, 10, 10); // Representación simple como un círculo
            }
        }

        public boolean colisionaCon(Vehiculo vehiculo) {
            return Math.hypot(x - vehiculo.getX(), y - vehiculo.getY()) < 10;
        }
    }