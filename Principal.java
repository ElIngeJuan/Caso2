import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("Menú:");
            System.out.println("1. Generación de referencias");
            System.out.println("2. Calcular datos de paginación");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    GeneradorReferencias generadorReferencias = new GeneradorReferencias();
                    System.out.print("Ingrese el tamaño de página (en bytes): ");
                    int tamPaginaBytes = scanner.nextInt();
                    System.out.print("Ingrese el número de filas de la matriz de datos: ");
                    int NF = scanner.nextInt();
                    System.out.print("Ingrese el número de columnas de la matriz de datos: ");
                    int NC = scanner.nextInt();
        
                    int tamMatriz = Math.max(NF, NC); // Tamaño de la matriz de datos (máximo entre NF y NC)
        
                    generadorReferencias.generarReferencias(tamPaginaBytes, tamMatriz, NF, NC);
                    break;
                case 2:
                    System.out.print("Ingrese el número de marcos: ");
                    int marcos = scanner.nextInt();
                    System.out.print("Ingrese la ruta del archivo de referencias: ");
                    String rutaArchivoReferencias = scanner.next();
                    calcularDatosPaginacion(marcos, rutaArchivoReferencias);
                    break;
                case 3:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
            }
        } while (opcion != 3);

}   


    public static void calcularDatosPaginacion(Integer marcos, String rutaArchivoReferencias){
        Integer np =0; // Número de páginas para envejecimientor
        ArrayList<Integer> referencias = new ArrayList<Integer>();
        Integer nr = 0; // Número de registros
        try {
            // Cambiar la ruta del archivo según sea necesario
            File file = new File(rutaArchivoReferencias);
            Scanner scanner = new Scanner(file);

            // Leer el archivo línea por línea
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("F[") || line.startsWith("M[") || line.startsWith("R[")){
                    // Si la línea es una referencia a una página, obtener la página virtual y procesarla
                    String[] parts = line.split(",");
                    int paginaVirtual = Integer.parseInt(parts[1]);
                    referencias.add(paginaVirtual);
                } else if (line.startsWith("NP=")) {
                    np = Integer.parseInt(line.substring(3));
                }else if (line.startsWith("NR")) {
                    nr = Integer.parseInt(line.substring(3));
                    
                }

            }
            
            scanner.close();


        } catch (FileNotFoundException e) {
            System.out.println("Archivo 'referencias.txt' no encontrado.");
        } catch (NumberFormatException e) {
            System.out.println("Error al leer el archivo: asegúrese de que esté en el formato correcto.");
        }


        LRU lru = new LRU(marcos, np);

        // Crear e iniciar el hilo para agregar páginas virtuales
        AgregarPaginasThread agregarPaginasThread = new AgregarPaginasThread(lru, referencias);

        agregarPaginasThread.start();
        // Esperar a que ambos hilos terminen antes de imprimir los resultados finales
        try {
            agregarPaginasThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Imprimir resultados finales
        System.out.println("Fallas de página: " + lru.getNumFallas());
        System.out.println("Éxitos de página: " + lru.getNumExitos());
        System.out.println("Tiempo de ejecución: (hits * 30) ns + (misses * 10000000) ns = " + lru.getNumExitos()*30 + " + " + lru.getNumFallas()*10000000 + " = " + (lru.getNumExitos()*30 + lru.getNumFallas()*10000000) + " ns");
        System.out.println("Tiempo si todo fuera fallos: " + nr*10000000 + " ns");
        System.out.println("Tiempo si todo fuera hits: " + nr*30 + " ns");
    }
}
