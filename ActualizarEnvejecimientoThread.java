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

            lru.actualizarEnvejecimientoTodos();

        }
    }
}
