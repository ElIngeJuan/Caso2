public class Main {
    // Función para aplicar el filtro
    public static void aplicarFiltro(int[][] mat1, int[][] mat2, int[][] mat3, int nf, int nc) {
        // Aplicar el filtro
        for (int i = 1; i < nf - 1; i++) {
            for (int j = 1; j < nc - 1; j++) {
                // Recorrer los vecinos y aplicar el filtro
                int acum = 0;
                for (int a = -1; a <= 1; a++) {
                    for (int b = -1; b <= 1; b++) {
                        int i2 = i + a;
                        int j2 = j + b;
                        
                        // Verificar límites para evitar desbordamientos
                        if (i2 >= 0 && i2 < nf && j2 >= 0 && j2 < nc) {
                            acum += (mat2[a + 1][b + 1] * mat1[i2][j2]);
                        }
                    }
                }
                // Aplicar reglas para valores de acumulación
                if (acum >= 0 && acum <= 255)
                    mat3[i][j] = acum;
                else if (acum < 0)
                    mat3[i][j] = 0;
                else
                    mat3[i][j] = 255;
            }
        }

        // Asignar un valor predefinido a los bordes
        for (int i = 0; i < nc; i++) {
            mat3[0][i] = 0;
            mat3[nf - 1][i] = 255;
        }
        for (int i = 1; i < nf - 1; i++) {
            mat3[i][0] = 0;
            mat3[i][nc - 1] = 255;
        }
    }

    public static void main(String[] args) {
        // Matriz de datos de ejemplo (5x5)
        int[][] datos = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        };

        // Matriz de filtro de ejemplo (3x3)
        int[][] filtro = {
            {1, 0, -1},
            {1, 0, -1},
            {1, 0, -1}
        };

        // Tamaño de filas y columnas de las matrices de datos de ejemplo
        int filas = datos.length;
        int columnas = datos[0].length;

        // Matriz resultante
        int[][] resultado = new int[filas][columnas];

        // Aplicar el filtro a los datos de ejemplo
        aplicarFiltro(datos, filtro, resultado, filas, columnas);

        // Imprimir la matriz resultante
        System.out.println("Matriz resultante después de aplicar el filtro:");
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(resultado[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
