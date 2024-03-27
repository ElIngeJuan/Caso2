import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class GeneradorReferencias {

    public static void generarReferencias(int tamPaginaBytes, int tamMatriz, int NF, int NC) {
        try {
            PrintWriter archivoSalida = new PrintWriter(new FileWriter("referencias.txt"));

            int NF_NC_Filtro = 3; // Tamaño del filtro siempre será 3x3
            int tamValorBytes = 4; // Tamaño de un valor en las matrices en bytes

            // Calcular el número total de registros (NR)
            int NR = 2 * (NF * NC) + 9; // 3 matrices en total (filtro, datos, resultado)

            // Calcular el número de páginas necesarias para cada matriz
            int paginasFiltro = (int) Math.ceil((double) NF_NC_Filtro * NF_NC_Filtro * tamValorBytes / tamPaginaBytes);
            int paginasDatos = (int) Math.ceil((double) NF * NC * tamValorBytes / tamPaginaBytes);
            int paginasResultado = (int) Math.ceil((double) NF * NC * tamValorBytes / tamPaginaBytes);

            // Calcular el número total de páginas utilizadas (NP)
            int NP = paginasFiltro + paginasDatos + paginasResultado;

            // Escribir metadatos en el archivo de referencias
            archivoSalida.println("TP=" + tamPaginaBytes);
            archivoSalida.println("NF=" + NF);
            archivoSalida.println("NC=" + NC);
            archivoSalida.println("NF_NC_Filtro=" + NF_NC_Filtro);
            archivoSalida.println("NR=" + NR);
            archivoSalida.println("NP=" + NP);

            // Generar referencias intercaladas para las matrices F (filtro), M (datos) y R (resultado)
            boolean[][] filtroGenerado = new boolean[NF_NC_Filtro][NF_NC_Filtro];

            for (int i = 0; i < NF; i++) {
                for (int j = 0; j < NC; j++) {
                    // Referencia para la matriz M (datos)
                    int posInicialM = NF_NC_Filtro * NF_NC_Filtro * tamValorBytes + (i * NC + j) * tamValorBytes;
                    int paginaM = posInicialM / tamPaginaBytes;
                    int desplazamientoM = posInicialM % tamPaginaBytes;
                    archivoSalida.println(String.format("M[%d][%d],%d,%d,R", i, j, paginaM, desplazamientoM));

                    // Referencia para la matriz F (filtro)
                    if (!filtroGenerado[i % NF_NC_Filtro][j % NF_NC_Filtro]) {
                        int posInicialF = i % NF_NC_Filtro * NF_NC_Filtro * tamValorBytes + j % NF_NC_Filtro * tamValorBytes;
                        int paginaF = posInicialF / tamPaginaBytes;
                        int desplazamientoF = posInicialF % tamPaginaBytes;
                        archivoSalida.println(String.format("F[%d][%d],%d,%d,R", i % NF_NC_Filtro, j % NF_NC_Filtro, paginaF, desplazamientoF));
                        filtroGenerado[i % NF_NC_Filtro][j % NF_NC_Filtro] = true;
                    }

                    // Referencia para la matriz R (resultado)
                    int posInicialR = NF_NC_Filtro * NF_NC_Filtro * tamValorBytes + NF * NC * tamValorBytes + (i * NC + j) * tamValorBytes;
                    int paginaR = posInicialR / tamPaginaBytes;
                    int desplazamientoR = posInicialR % tamPaginaBytes;
                    archivoSalida.println(String.format("R[%d][%d],%d,%d,W", i, j, paginaR, desplazamientoR));
                }
            }

            archivoSalida.close();
            System.out.println("Archivo de referencias generado: referencias.txt");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el tamaño de página (en bytes): ");
        int tamPaginaBytes = scanner.nextInt();
        System.out.print("Ingrese el número de filas de la matriz de datos: ");
        int NF = scanner.nextInt();
        System.out.print("Ingrese el número de columnas de la matriz de datos: ");
        int NC = scanner.nextInt();

        int tamMatriz = Math.max(NF, NC); // Tamaño de la matriz de datos (máximo entre NF y NC)

        generarReferencias(tamPaginaBytes, tamMatriz, NF, NC);
    }
}
