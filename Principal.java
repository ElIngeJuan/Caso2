import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        int marcos=8; // Número de marcos de página
        Integer np =0; // Número de páginas para envejecimientor
        ArrayList<Integer> referencias = new ArrayList<Integer>();
        try {
            // Cambiar la ruta del archivo según sea necesario
            File file = new File("referencias.txt");
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
    }
}
