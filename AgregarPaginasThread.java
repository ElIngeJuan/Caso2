import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class AgregarPaginasThread extends Thread {
    private LRU lru;
    private ActualizarEnvejecimientoThread actualizarEnvejecimientoThread;
    private ArrayList<Integer> referencias;

    public AgregarPaginasThread(LRU lru, ArrayList<Integer> referencias) {
        this.lru = lru;
        this.actualizarEnvejecimientoThread = new ActualizarEnvejecimientoThread(lru);
        this.referencias = referencias;
    }

    @Override
    public void run() {
        actualizarEnvejecimientoThread.start();
        for (int i = 0; i < referencias.size(); i++) {
            try {
                Thread.sleep(1);


                if ((i+1) % 4 == 0) {
                    lru.SolicitudActualizar();  
                }

                lru.get(referencias.get(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        actualizarEnvejecimientoThread.stopThread();
    }
}
