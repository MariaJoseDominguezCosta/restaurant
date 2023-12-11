package com.example.models;

public class Monitor_Recepcionist {
    private boolean min_clientes_esperando;
    private boolean max_clientes_esperando;
    
    public Monitor_Recepcionist () {
        this.min_clientes_esperando = true;
        this.max_clientes_esperando = false;
    }

    public synchronized void esperar_cola () {
        while (!min_clientes_esperando) { //Guarda booleana
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("El espacio de las mesas está lleno");
        min_clientes_esperando = false;
        max_clientes_esperando = true;
        this.notifyAll();
    }

    public synchronized void liberar_cola () {
        while (!max_clientes_esperando) //Guarda booleana
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        System.out.println("Se ha liberado espacios, puede seguir");
        min_clientes_esperando = true;
        max_clientes_esperando = false;
        this.notifyAll();
    }
}
