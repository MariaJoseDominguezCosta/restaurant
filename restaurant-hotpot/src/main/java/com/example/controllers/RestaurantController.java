package com.example.controllers;

import com.example.models.Client;
import com.example.models.Monitor_Recepcionist;
import com.example.models.Monitor_Chef;
import com.example.models.Position;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class RestaurantController implements Observer {
    @FXML
    private AnchorPane stage;
    @FXML
    private Label label;
    @FXML
    private ImageView Hotpot_Spicy, Hotpot_Fortune, Hotpot_Chicken, Hotpot_Bejing, Bok_Choy, Edamame, Enoki, Tangyuan, Galanga, Tausi, Salt, Yunnan_Mint;
    private final int N = 100;
    private int MAX = 20;
    private Random random;
    private final String label_color = "-fx-text-fill: black;";
    private String path = "C:\\Users\\Maria Jose\\Documents\\CHINGO DE TAREA\\UNIVERSIDAD\\SEPTIMO CUATRIMESTRE\\PROGRAMACION CONCURRENTE\\JUEGO\\restaurant\\restaurant-hotpot\\src\\main\\resources\\com\\example\\assets\\imgs\\cliente_";
    ImageView[] imageViews = new ImageView[N];
    Thread[] hilo_cliente = new Thread[N];
    Label[] labels = new Label[N];
    private Monitor_Recepcionist recepcionista;
    private Monitor_Chef cocinero;
    boolean [] tables = new boolean[MAX];
    boolean [] salida_cliente = new boolean[N];
    private Client cliente;
    private static int cont_clientes;
    private boolean guarda_boolean = false;
    int control_mesas;
    int generar = 0;

    //Método que genera un número aleatorio para una imagen
    public int generate_random_image () {
        return (int)(Math.random() * 6)+1;
    }

    //Método que genera un número aleatorio evaluar mesas disponibles
    public int generate_random_table () {
        return (int)(Math.random() * 20)+1;
    }
    @FXML
    void empezarOnMouseClicked(MouseEvent event) {
        int space = 0;
        int serial_number = 1; //Id del cliente en etiquetas

        for (int i = 0; i < N; i++) {
            int random = generate_random_image();
            Image image = new Image(path + (random) + ".jpg"); //Asigna una imagen aleatorio a objeto Image
            imageViews[i] = new ImageView(image);
            imageViews[i].setFitWidth(40); // ajusta el ancho de la imagen
            imageViews[i].setFitHeight(80); // ajusta la altura de la imagen
            imageViews[i].setImage(image);
            imageViews[i].setLayoutX(1000 + space); //Cliente llega desde lado derecho a izquierdo
            imageViews[i].setLayoutY(450);
            space+= 150;

            cliente = new Cliente(20); //Se instancia clase cliente
            cliente.addObserver(this);
            cliente.setPosicion(new Position(i, (int)(imageViews[i].getLayoutX()), (int)(imageViews[i].getLayoutY())));
            hilo_cliente[i] = new Thread(cliente); //Se crea arreglo de hilos
            hilo_cliente[i].setName("Cliente " + serial_number);
            labels[i] = new Label(hilo_cliente[i].getName()); //Se asignan etiquetas a los hilos
            labels[i].setStyle(label_color);
            stage.getChildren().addAll(imageViews[i], labels[i]);
            hilo_cliente[i].start(); //Se lanzan los hilos
            serial_number++;
        }
    }

    //Método que llena los arreglos a False
    public void fill_array_to_false () {
        for (int i=0; i<20; i++) {
            tables[i] = false;
        }
        for (int i=0; i<N;i++)
            salida_cliente[i] = false;
    }

    //Se inicializan los siguientes valores al ejecutar el programa
    @FXML
    public void initialize() {
        random = new Random(System.currentTimeMillis()); //Semilla para generar números aleatorios
        cont_clientes = 0;
        control_mesas = 1;
        recepcionista = new Monitor_Recepcionista(); //Instancia única de recepcionista
        cocinero = new Monitor_cocinero(); //Instancia única de cocinero
        fill_array_to_false();
    }

    @Override
    public void update(Observable o, Object arg) {
        Position pos = (Position) arg;
        Platform.runLater(() -> {
            tracking(pos.getId(), pos.getX(), pos.getY()); //Método que realiza el comportamiento de los hilos Cliente
            int cantidad = this.cont_clientes; //Cantidad de clientes actuales dentro del establecimiento
            mesas.setText("Mesas ocupadas: " + cantidad);
        });
    }

    public void tracking(int pos, int posX, int posY) {
        generar = generate_random_table();

        imageViews[pos].setLayoutX(posX);
        imageViews[pos].setLayoutY(posY);
        labels[pos].setLayoutX(imageViews[pos].getLayoutX());
        labels[pos].setLayoutY(imageViews[pos].getLayoutY());

        //condición que evalúa la salida del cliente del restaurante
        if (imageViews[pos].getLayoutX() <= 0 && salida_cliente[pos] != true) {
            salida_cliente[pos] = true;
            this.cont_clientes--;
        }

        //Condición cuando llega el cliente al restaurante, aumenta num clientes y modifica la guarda boleana
        if (imageViews[pos].getLayoutX() == 150 && imageViews[pos].getLayoutY() == 450) {
            this.cont_clientes++;
            if (this.cont_clientes >= MAX) {
                System.out.println("Clientes: " + this.cont_clientes);
                activar_clientes_espera(hilo_cliente, pos);
                System.out.println("Mesas no disponibles");
                cont_clientes = cont_clientes - 1;
                guarda_boolean = true;
            }
                //Switch que controla el comportamiento de los clientes al llegar a las mesas
                switch (control_mesas) {
                    case 1: {
                        imageViews[pos].setLayoutX(220);
                        imageViews[pos].setLayoutY(210);
                        labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                        labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                        tables[generar-1] = false; //Variable que controla la disponibilidad de las mesas
                        Hotpot_Spicy.setVisible(true); //Simula la atención del mesero al cliente
                        break;
                    }
                    case 2: {
                        imageViews[pos].setLayoutX(345);
                        imageViews[pos].setLayoutY(210);
                        labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                        labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                        tables[generar-1] = false;
                        Hotpot_Fortune.setVisible(true);
                        break;
                    }
                    case 3: {
                        imageViews[pos].setLayoutX(470);
                        imageViews[pos].setLayoutY(210);
                        labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                        labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                        tables[generar-1] = false;
                        Hotpot_Chicken.setVisible(true);
                        break;
                    }
                    case 4: {
                        imageViews[pos].setLayoutX(585);
                        imageViews[pos].setLayoutY(210);
                        labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                        labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                        tables[generar-1] = false;
                        Hotpot_Bejing.setVisible(true);
                        break;
                    }
                    case 5: {
                        imageViews[pos].setLayoutX(700);
                        imageViews[pos].setLayoutY(210);
                        labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                        labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                        tables[generar-1] = false;
                        Edamame.setVisible(true);
                        break;
                    }
                    case 6: {
                        imageViews[pos].setLayoutX(225);
                        imageViews[pos].setLayoutY(370);
                        labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                        labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                        tables[generar-1] = false;
                        Enoki.setVisible(true);
                        break;
                    }
                    case 7: {
                        imageViews[pos].setLayoutX(350);
                        imageViews[pos].setLayoutY(370);
                        labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                        labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                        tables[generar-1] = false;
                        Tangyuan.setVisible(true);
                        break;
                    }
                    case 8: {
                        imageViews[pos].setLayoutX(475);
                        imageViews[pos].setLayoutY(370);
                        labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                        labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                        tables[generar-1] = false;
                        Tausi.setVisible(true);
                        break;
                    }
                    case 9: {
                        imageViews[pos].setLayoutX(590);
                        imageViews[pos].setLayoutY(370);
                        labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                        labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                        tables[generar-1] = false;
                        Yunnan_Mint.setVisible(true);
                        break;
                    }
                    case 10: {
                        imageViews[pos].setLayoutX(705);
                        imageViews[pos].setLayoutY(370);
                        labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                        labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                        tables[generar-1] = false;
                        Bok_Choy.setVisible(true);
                        break;
                    }
                }

            if (control_mesas == 20) {
                cocinero.wait_buffet(); //Se activa monitor al no haber disponibilidad, el cocinero para de cocinar
                    control_mesas = 0;
                cocinero.release_buffet(); //Se libera monitor para que el cocinero quede en reposo en espera de nuevos clientes
            }
            control_mesas++;  //Aumenta el número de mesas para tener control de los clientes
        }

        if (guarda_boolean && cont_clientes == 0) { //Se evalúa guarda boleana para activar monitor recepcionista
            recepcionista.esperar_cola(); //Se para cola al llenarse el restaurante
            desactivate_clients_wait(hilo_cliente, pos); //Detiene los hilos de clientes en espera
            cont_clientes = cont_clientes + 1;
            recepcionista.liberar_cola(); //Libera señal para permitir entrada de los clientes al restaurante
            guarda_boolean = false;  //Guarda boleana cambia a false para control de hilos entrantes
        }

    }

    //Método que activa la espera de clientes, pausa proceso de hilos
    public void activar_clientes_espera (Thread [] hilo_cliente, int pos) {
        for (int i = pos; i < hilo_cliente.length; i++)
            hilo_cliente[i].suspend();
    }

    //Método que reanuda proceso de hilos al vacirse el restaurante, espacios disponibles
    public void desactivate_clients_wait (Thread [] hilo_cliente, int pos) {
        //Oculta los platos, simulación de mesero que recoge los platos de clientes satisfechos
        Hotpot_Spicy.setVisible(false);
        Hotpot_Fortune.setVisible(false);
        Hotpot_Chicken.setVisible(false);
        Hotpot_Bejing.setVisible(false);
        Bok_Choy.setVisible(false);
        Tangyuan.setVisible(false);
        Edamame.setVisible(false);
        Enoki.setVisible(false);
        Tausi.setVisible(false);
        Yunnan_Mint.setVisible(false);
        for (int i = pos; i < hilo_cliente.length; i++)
            hilo_cliente[i].resume(); //Reanuda los hilos en su proceso para posteriormente salir del lugar
    }
}
