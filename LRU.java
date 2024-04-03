import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class LRU {
    private int capacidad;
    private Map<Integer, Integer> lruHash;
    private Map<Integer, List<Integer>> envejecimientoHash;
    private LinkedList<Integer> contadorReferencias;
    private Boolean actualizando;
    private int fallas;
    private int exitos;

    public LRU(int capacidad,Integer np) {
        this.capacidad = capacidad;
        this.lruHash = new HashMap<>();
        this.envejecimientoHash = new HashMap<>();
        this.contadorReferencias = new LinkedList<>();
        this.fallas = 0;
        this.exitos = 0;
        this.actualizando = false;
        initializeEnvejecimiento(np);
        for(int i=0 ; i<np ; i++){
            contadorReferencias.add(0);
        }
    }



    private void initializeEnvejecimiento(int np) {
        for (int i = 0; i < np; i++) {
            List<Integer> envejecimiento = new LinkedList<>();
            for (int j = 0; j < 8; j++) {
                envejecimiento.add(0);
            }
            envejecimientoHash.put(i, envejecimiento);
        }

    }

    public synchronized void get(int paginaVirtual) throws InterruptedException {
        while (actualizando) {
            wait();
        }
        if (lruHash.containsKey(paginaVirtual)) {
            agregarReferencia(paginaVirtual);
            exitos++; 
        } else {
            fallas++; 
            agregarReferencia(paginaVirtual);
            put(paginaVirtual); 
        }
    }

    public synchronized void agregarReferencia(int paginaVirtual) {
        while (actualizando) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
        contadorReferencias.set(paginaVirtual, 1);
        
    }




    public void put(int paginaVirtual) throws InterruptedException {
        if (lruHash.size() == capacidad) {
            int paginaMenosUsada = encontrarPaginaMenosUsada();
            lruHash.remove(paginaMenosUsada);
        }
        lruHash.put(paginaVirtual, paginaVirtual); // Agregar la página al LRU
    }

    private synchronized int encontrarPaginaMenosUsada() throws InterruptedException {
        while (actualizando) {
            wait();
        }
        int paginaMenosUsada = -1;
        int minContador = Integer.MAX_VALUE;
        for (Map.Entry<Integer, Integer> entry : lruHash.entrySet()) {
            int contador = convertirBinarioADecimal(envejecimientoHash.get(entry.getValue()));
            if (contador < minContador) {
                minContador = contador;
                paginaMenosUsada = entry.getKey();
            }
        }
        System.out.println("Pagina menos usada: " + paginaMenosUsada);
        return paginaMenosUsada;
    }

    private int convertirBinarioADecimal(List<Integer> bits) {
        int decimal = 0;
        for (int i = 0; i < bits.size(); i++) {
            decimal += bits.get(i) * Math.pow(2, bits.size() - 1 - i);
        }
        return decimal;
    }

    public synchronized void actualizarEnvejecimientoTodos() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        actualizando = true;
        for (int i = 0; i < contadorReferencias.size(); i++) {
            int estado = contadorReferencias.get(i);

            actualizarEnvejecimiento(i, estado);
            contadorReferencias.set(i, 0);
        }


        actualizando = false;
        notify();
        
    }
    
    public synchronized void SolicitudActualizar(){
        notify();
    }
    
    
    private void actualizarEnvejecimiento(int paginaVirtual, int estado) {
        List<Integer> envejecimiento = envejecimientoHash.get(paginaVirtual);
        for (int i = envejecimiento.size() - 1; i > 0; i--) {
            envejecimiento.set(i, envejecimiento.get(i - 1));
        }
        envejecimiento.set(0, estado); // Actualizar el envejecimiento con el nuevo estado
        envejecimientoHash.put(paginaVirtual, envejecimiento);
    }

    public int getNumFallas() {
        return fallas;
    }

    public int getNumExitos() {
        return exitos;
    }



    }
    

