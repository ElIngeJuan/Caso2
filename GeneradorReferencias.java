import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GeneradorReferencias {

    public void generarReferencias(int tamPaginaBytes, int tamMatriz, int NF, int NC){
         try {
            PrintWriter archivoSalida = new PrintWriter(new FileWriter("referencias.txt"));

            int NF_NC_Filtro = 3; // Tamaño del filtro siempre será 3x3
            int tamValorBytes = 4; // Tamaño de un valor en las matrices en bytes

            // Calcular el número total de registros (NR)
            int NR =18 * (NF - 2) * (NC - 2) + NF * NC;

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

            for (int i = 1; i < NF - 1; i++) {
                for (int j = 1; j < NC - 1; j++) {
                    // Recorrer los vecinos y aplicar el filtro
                    for (int a = -1; a <= 1; a++) {
                        for (int b = -1; b <= 1; b++) {
                            int i2 = i + a;
                            int j2 = j + b;
                            int i3 = a + 1;
                            int j3 = b + 1;
    
                           
                            // Verificar límites para evitar desbordamientos

                            int posInicialM = NF_NC_Filtro * NF_NC_Filtro * tamValorBytes + ((i2) * NC + (j2)) * tamValorBytes;
                            int paginaM = posInicialM / tamPaginaBytes;
                            int desplazamientoM = posInicialM % tamPaginaBytes;
                            archivoSalida.println(String.format("M[%d][%d],%d,%d,R", i2, j2, paginaM, desplazamientoM));
    
                            int posInicialF = ((i3) % NF_NC_Filtro) * NF_NC_Filtro * tamValorBytes + ((j3) % NF_NC_Filtro) * tamValorBytes;
                            int paginaF = posInicialF / tamPaginaBytes;
                            int desplazamientoF = posInicialF % tamPaginaBytes;
                            archivoSalida.println(String.format("F[%d][%d],%d,%d,R", i3 % NF_NC_Filtro, j3 % NF_NC_Filtro, paginaF, desplazamientoF));
                        }
                    }
                    // Aplicar reglas para valores de acumulación
                    if (i > 0 && i < NF && j > 0 && j < NC) {
        
                        int posInicialR = NF_NC_Filtro * NF_NC_Filtro * tamValorBytes + NF * NC * tamValorBytes + ((i)* NC +(j)) * tamValorBytes;
                        int paginaR = posInicialR / tamPaginaBytes;
                        int desplazamientoR = posInicialR % tamPaginaBytes;
                        archivoSalida.println(String.format("R[%d][%d],%d,%d,W", i, j, paginaR, desplazamientoR));
                    }
                }
            }
    
            // Asignar un valor predefinido a los bordes
            for (int i = 0; i < NC; i++) {
                int posInicialR = NF_NC_Filtro * NF_NC_Filtro * tamValorBytes + NF * NC * tamValorBytes + (0 * NC + i) * tamValorBytes;
                int paginaR = posInicialR / tamPaginaBytes;
                int desplazamientoR = posInicialR % tamPaginaBytes;
                archivoSalida.println(String.format("R[%d][%d],%d,%d,W", 0, i, paginaR, desplazamientoR));

                int posInicialR1 = NF_NC_Filtro * NF_NC_Filtro * tamValorBytes + NF * NC * tamValorBytes + ((NF-1) * NC + i) * tamValorBytes;
                int paginaR1 = posInicialR1 / tamPaginaBytes;
                int desplazamientoR1 = posInicialR1 % tamPaginaBytes;
                archivoSalida.println(String.format("R[%d][%d],%d,%d,W", NF - 1, i, paginaR1, desplazamientoR1));

            }
            for (int i = 1; i < NF - 1; i++) {
                int posInicialR = NF_NC_Filtro * NF_NC_Filtro * tamValorBytes + NF * NC * tamValorBytes + (i * NC + 0) * tamValorBytes;
                int paginaR = posInicialR / tamPaginaBytes;
                int desplazamientoR = posInicialR % tamPaginaBytes;
                archivoSalida.println(String.format("R[%d][%d],%d,%d,W", i, 0, paginaR, desplazamientoR));

                int posInicialR1 = NF_NC_Filtro * NF_NC_Filtro * tamValorBytes + NF * NC * tamValorBytes + (i * NC + NC - 1) * tamValorBytes;
                int paginaR1 = posInicialR1 / tamPaginaBytes;
                int desplazamientoR1 = posInicialR1 % tamPaginaBytes;
                archivoSalida.println(String.format("R[%d][%d],%d,%d,W", i, NC - 1, paginaR1, desplazamientoR1));

            }

            archivoSalida.close();
            System.out.println("Archivo de referencias generado: referencias.txt");
        } catch (IOException e) {
            System.out.println("Error al generar el archivo de referencias.");
        }

    }


}
