import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class LRU {
    private int capacidad;
    private Map<Integer, Integer> map;
    private LinkedList<Integer> elementosList;
    private int fallas;
    private int exitos;

    public LRU(int capacidad) {
        this.capacidad = capacidad;
        this.map = new HashMap<>();
        this.elementosList = new LinkedList<>();
        this.fallas = 0;
        this.exitos = 0;
    }

    public void get(int paginaVirtual) {
        if (map.containsKey(paginaVirtual)) {
            elementosList.remove((Integer) paginaVirtual);
            elementosList.addFirst(paginaVirtual);
            exitos++; // Incrementar el contador de éxitos
        } else {
            fallas++; // Incrementar el contador de fallas
            put(paginaVirtual); // Solo necesitas pasar la clave, ya que es el valor también
        }
    }

    public void put(int paginaVirtual) {
        if (map.containsKey(paginaVirtual)) {
            elementosList.remove((Integer) paginaVirtual);
        } else if (elementosList.size() == capacidad) {
            int last = elementosList.removeLast();
            map.remove(last);
        }
        map.put(paginaVirtual, paginaVirtual); // Key es tanto la clave como el valor
        elementosList.addFirst(paginaVirtual);
    }

    public int getNumFallas() {
        return fallas;
    }

    public int getNumExitos() {
        return exitos;
    }

    public static void main(String[] args) {

        int marcos = 4; // Número de marcos de página
        LRU lru = new LRU(marcos); // Inicializar el algoritmo LRU con el número de marcos

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
                    lru.get(paginaVirtual); // Procesar la página virtual utilizando el algoritmo LRU
                }
            }
            scanner.close();

            // Imprimir el número de fallas y éxitos al finalizar la simulación
            System.out.println("Número de fallas: " + lru.getNumFallas());
            System.out.println("Número de éxitos: " + lru.getNumExitos());
        } catch (FileNotFoundException e) {
            System.out.println("Archivo 'ejemplo.txt' no encontrado.");
        } catch (NumberFormatException e) {
            System.out.println("Error al leer el archivo: asegúrese de que esté en el formato correcto.");
        }
    }
}
