import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LRU {
    private int capacidad;
    private Map<Integer, Integer> lruHash;
    private Map<Integer, List<Integer>> envejecimientoHash;
    private LinkedList<Integer> contadorReferencias;
    private int fallas;
    private int exitos;

    public LRU(int capacidad) {
        this.capacidad = capacidad;
        this.lruHash = new HashMap<>();
        this.envejecimientoHash = new HashMap<>();
        this.contadorReferencias = new LinkedList<>();
        this.fallas = 0;
        this.exitos = 0;
    }

    public void setNumPaginasEnvejecimiento(int np) {
        initializeEnvejecimiento(np);
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

    public void get(int paginaVirtual) {
        if (lruHash.containsKey(paginaVirtual)) {
            contadorReferencias.add(paginaVirtual);
            exitos++; 
        } else {
            fallas++; 
            contadorReferencias.add(paginaVirtual);
            put(paginaVirtual); 
        }
    }

    public void put(int paginaVirtual) {
        if (lruHash.size() == capacidad) {
            int paginaMenosUsada = encontrarPaginaMenosUsada();
            lruHash.remove(paginaMenosUsada);
        }
        lruHash.put(paginaVirtual, paginaVirtual); // Agregar la página al LRU
        List<Integer> envejecimiento = new LinkedList<>();
        for (int i = 0; i < 8; i++) {
            envejecimiento.add(0);
        }
        envejecimiento.set(0, 1);
        envejecimientoHash.put(paginaVirtual, envejecimiento); // Inicializar el envejecimiento a 0
    }

    private int encontrarPaginaMenosUsada() {
        int paginaMenosUsada = -1;
        int minContador = Integer.MAX_VALUE;
        for (Map.Entry<Integer, Integer> entry : lruHash.entrySet()) {
            int contador = convertirBinarioADecimal(envejecimientoHash.get(entry.getValue()));
            if (contador < minContador) {
                minContador = contador;
                paginaMenosUsada = entry.getKey();
            }
        }
        return paginaMenosUsada;
    }

    private int convertirBinarioADecimal(List<Integer> bits) {
        int decimal = 0;
        for (int i = 0; i < bits.size(); i++) {
            decimal += bits.get(i) * Math.pow(2, bits.size() - 1 - i);
        }
        return decimal;
    }

    public void actualizarEnvejecimientoTodos() {
        for (Map.Entry<Integer, List<Integer>> entry : envejecimientoHash.entrySet()) {
            int paginaVirtual = entry.getKey();
            actualizarEnvejecimiento(paginaVirtual);
        }
        contadorReferencias.clear(); // Limpiar el contador después de actualizar el envejecimiento
    }
    
    private void actualizarEnvejecimiento(int paginaVirtual) {
        List<Integer> envejecimiento = envejecimientoHash.get(paginaVirtual);
        int estado = 0;
        if (contadorReferencias.contains(paginaVirtual)) {
            estado = 1; 
        }
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
    

