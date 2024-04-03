public class Principal {
    public static void main(String[] args) {
        int marcos = 8; // Número de marcos de página
        LRU lru = new LRU(marcos);

        // Crear e iniciar el hilo para agregar páginas virtuales
        AgregarPaginasThread agregarPaginasThread = new AgregarPaginasThread(lru);
        agregarPaginasThread.start();
        // Esperar a que ambos hilos terminen antes de imprimir los resultados finales
        try {
            agregarPaginasThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Imprimir el número de fallas y éxitos al finalizar la simulación
        System.out.println("Número de fallas: " + lru.getNumFallas());
        System.out.println("Número de éxitos: " + lru.getNumExitos());
    }
}
