/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas;

import java.util.Random;
import javax.swing.JButton;

/**
 *
 * @author Usuario
 */
public class TableroMatriz {

    private int[][] tablero; // Matriz para representar el estado del tablero
    private int filas;
    private int columnas;
    private int minas;
    private boolean[][] camposRevelados;

    public TableroMatriz(int filas, int columnas, int minas) {
        this.filas = filas;
        this.columnas = columnas;
        this.minas = minas;
        this.tablero = new int[filas][columnas];
        this.camposRevelados = new boolean[filas][columnas];
        inicializarTablero();
    }

    public int[][] getTablero() {
        return tablero;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int getMinas() {
        return minas;
    }

    public boolean[][] getCamposRevelados() {
        return camposRevelados;
    }

    public void inicializarTablero() {
        // Inicializa el tablero con ceros
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                tablero[fila][columna] = 0;
                camposRevelados[fila][columna] = false;
            }
        }

        // Genera minas aleatorias en el tablero
        Minas();
    }

    public void Minas() {
        Random rand = new Random();
        int minasGeneradas = 0;

        while (minasGeneradas < minas) {
            int fila = rand.nextInt(filas);
            int columna = rand.nextInt(columnas);

            // Verifica si ya hay una mina en esa posición
            if (tablero[fila][columna] != -1) {
                tablero[fila][columna] = -1;
                minasGeneradas++;
            }
        }

        // Calcula el número de minas cercanas para cada celda
        numerosCercanos();
    }

    public void numerosCercanos() {
        // Calcula la cantidad de minas cercanas para cada celda
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                if (tablero[fila][columna] != -1) {
                    int minasCercanas = minasCercanas(fila, columna);
                    tablero[fila][columna] = minasCercanas;
                }
            }
        }
    }

    public int minasCercanas(int fila, int columna) {
        int minasCercanas = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nuevaFila = fila + i;
                int nuevaColumna = columna + j;

                // Verifica que la posición esté dentro de los límites del tablero
                if (nuevaFila >= 0 && nuevaFila < filas && nuevaColumna >= 0 && nuevaColumna < columnas) {
                    if (tablero[nuevaFila][nuevaColumna] == -1) {
                        minasCercanas++;
                    }
                }
            }
        }
        return minasCercanas;
    }

    public void revelarCelda(int fila, int columna) {
        // Verifica si la celda ya fue revelada o si está fuera de los límites
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas || camposRevelados[fila][columna]) {
            return;
        }

        // Marca la celda como revelada
        camposRevelados[fila][columna] = true;

        // Si es una mina, termina el juego (implementa la lógica para manejar el final del juego)
        if (tablero[fila][columna] == -1) {
            // Implementa la lógica para el final del juego (perdido)
            return;
        }

        // Si es una celda vacía, revela las celdas adyacentes de manera recursiva
        if (tablero[fila][columna] == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    revelarCelda(fila + i, columna + j);
                }
            }
        }
    }

    public boolean victoria() {
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                // Si alguna celda no minada no está revelada, el juego no ha terminado
                if (tablero[fila][columna] >= 0 && !camposRevelados[fila][columna]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void desbloquearCasillasAdyacentes(int fila, int columna, JButton[][] buttons) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas || camposRevelados[fila][columna]) {
            // La casilla está fuera de los límites o ya ha sido revelada, no se hace nada
            return;
        }

        // Revela la casilla actual
        camposRevelados[fila][columna] = true;
        buttons[fila][columna].setEnabled(false); // Desactiva el botón para que no se pueda volver a hacer clic

        if (tablero[fila][columna] == 0) {
            // Si la casilla es un 0, desbloquea las casillas adyacentes de manera recursiva
            desbloquearCasillasAdyacentes(fila - 1, columna, buttons); // Arriba
            desbloquearCasillasAdyacentes(fila + 1, columna, buttons); // Abajo
            desbloquearCasillasAdyacentes(fila, columna - 1, buttons); // Izquierda
            desbloquearCasillasAdyacentes(fila, columna + 1, buttons); // Derecha
        }
    }
}
