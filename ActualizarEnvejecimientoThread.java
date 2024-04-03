public class ActualizarEnvejecimientoThread extends Thread {
    private LRU lru;
    private Boolean running = true;

    public ActualizarEnvejecimientoThread(LRU lru) {
        this.lru = lru;
    }

    public void stopThread() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Actualizar envejecimiento cada 5 segundos
                Thread.sleep(4);
                lru.actualizarEnvejecimientoTodos();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
