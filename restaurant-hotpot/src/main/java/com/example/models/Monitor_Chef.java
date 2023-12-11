package com.example.models;
public class Monitor_Chef {
    private boolean min_comida_esperando;
    private boolean max_comida_esperando;

    public Monitor_Chef () {
        min_comida_esperando = true;
        max_comida_esperando = false;
    }
    public synchronized void wait_buffet () {
        while (!min_comida_esperando) { //Guarda booleana
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("El cocinero ha tomado la orden");
        min_comida_esperando = false;
        max_comida_esperando = true;
        this.notifyAll();
    }

    public synchronized void release_buffet () {
        while (!max_comida_esperando) //Guarda booleana
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        System.out.println("El cocinero ha despachado la orden");
        max_comida_esperando = false;
        min_comida_esperando = true;
        this.notifyAll();
    }
}


