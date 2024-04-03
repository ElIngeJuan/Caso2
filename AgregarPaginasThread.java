import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AgregarPaginasThread extends Thread {
    private LRU lru;
    private ActualizarEnvejecimientoThread actualizarEnvejecimientoThread;

    public AgregarPaginasThread(LRU lru) {
        this.lru = lru;
        this.actualizarEnvejecimientoThread = new ActualizarEnvejecimientoThread(lru);
    }

    @Override
    public void run() {
        try {
            // Cambiar la ruta del archivo según sea necesario
            File file = new File("referencias.txt");
            Scanner scanner = new Scanner(file);
            int np = 0; // Número de páginas para envejecimiento

            // Leer el archivo línea por línea
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("F[") || line.startsWith("M[") || line.startsWith("R[")){
                    // Si la línea es una referencia a una página, obtener la página virtual y procesarla
                    String[] parts = line.split(",");
                    int paginaVirtual = Integer.parseInt(parts[1]);
                    lru.get(paginaVirtual); // Procesar la página virtual utilizando el algoritmo 
                    Thread.sleep(1);
                } else if (line.startsWith("NP=")) {
                    np = Integer.parseInt(line.substring(3));
                    lru.setNumPaginasEnvejecimiento(np); // Configurar el número de páginas para envejecimiento
                    actualizarEnvejecimientoThread.start();
            
                }
                actualizarEnvejecimientoThread.stopThread();
            }
            
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo 'referencias.txt' no encontrado.");
        } catch (NumberFormatException e) {
            System.out.println("Error al leer el archivo: asegúrese de que esté en el formato correcto.");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
