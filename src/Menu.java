import calculos.MergeSortExecutor;
import calculos.MergeSortTask;
import calculos.SequentialMergeSort;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.concurrent.*;

public class Menu extends JFrame implements ActionListener {
    // Componentes de la interfaz
    private JLabel lbOriginal, lbResultado, lbTiempoSecuencial, lbTiemForkJoin, lbTiempo1, lbTiemExceSer;
    private JButton btnSecuencia, btnForkJoin, btnExceSer, btnLimpiar, btnCrearArreglo;
    private JTextArea txtArregloOriginal, txtArregloOrdenado;
    private JTextField txtTamanio;
    private int[] arreglo;
    private int[] arregloAux;
    private chatServidor servidor;

    private int opcion;

    // Tamaño inicial del arreglo
    private static final int TAM = 10;


    public Menu() {

        initComponents();
        generarArreglo(TAM);
        mostrarArregloOriginal();
    }

    private void initComponents() {
        // Configuración de la ventana
        setLayout(null);
        setBounds(10, 10, 600, 500);
        setTitle("Prueba Merge Sort");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicialización de componentes
        lbTiempo1 = new JLabel("Tiempos en milisegundos: ");
        lbTiempo1.setBounds(10, 380, 300, 30);
        add(lbTiempo1);

        lbOriginal = new JLabel("Arreglo Original");
        lbOriginal.setBounds(10, 20, 300, 30);
        add(lbOriginal);

        lbResultado = new JLabel("Resultado:");
        lbResultado.setBounds(150, 20, 300, 30);
        add(lbResultado);

        lbTiempoSecuencial = new JLabel("Tiempo:");
        lbTiempoSecuencial.setBounds(10, 420, 300, 30);
        add(lbTiempoSecuencial);

        lbTiemForkJoin = new JLabel("Tiempo: ");
        lbTiemForkJoin.setBounds(200, 420, 300, 30);
        add(lbTiemForkJoin);

        lbTiemExceSer = new JLabel("Tiempo:");
        lbTiemExceSer.setBounds(400, 420, 300, 30);
        add(lbTiemExceSer);

        btnSecuencia = new JButton("Secuencial");
        btnSecuencia.setBounds(10, 350, 150, 30);
        btnSecuencia.addActionListener(this);
        add(btnSecuencia);

        btnForkJoin = new JButton("ForkJoin");
        btnForkJoin.setBounds(200, 350, 150, 30);
        btnForkJoin.addActionListener(this);
        add(btnForkJoin);

        btnExceSer = new JButton("Executor Service");
        btnExceSer.setBounds(400, 350, 150, 30);
        btnExceSer.addActionListener(this);
        add(btnExceSer);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(350, 270, 150, 30);
        btnLimpiar.addActionListener(this);
        add(btnLimpiar);

        txtArregloOriginal = new JTextArea();
        txtArregloOriginal.setEditable(false);
        JScrollPane scrollPane1 = new JScrollPane(txtArregloOriginal);
        scrollPane1.setBounds(10, 50, 150, 280);
        add(scrollPane1);

        txtArregloOrdenado = new JTextArea();
        txtArregloOrdenado.setEditable(false);
        JScrollPane scrollPane2 = new JScrollPane(txtArregloOrdenado);
        scrollPane2.setBounds(150, 50, 150, 280);
        add(scrollPane2);

        JLabel lblTamanio = new JLabel("Nuevo tamaño del arreglo:");
        lblTamanio.setBounds(250, 20, 180, 25);
        add(lblTamanio);

        txtTamanio = new JTextField(20);
        txtTamanio.setBounds(290, 20, 160, 25);
        add(txtTamanio);

        JButton btnCrearArreglo = new JButton("Crear Arreglo");
        btnCrearArreglo.setBounds(300, 50, 150, 25);
        btnCrearArreglo.addActionListener(e -> {
            try {
                int nuevoTamanio = Integer.parseInt(txtTamanio.getText());
                generarArreglo(nuevoTamanio);
                mostrarArregloOriginal();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingresa un número válido para el tamaño del arreglo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(btnCrearArreglo);
    }

    public void generarArreglo(int tamaño) {
        arreglo = new int[tamaño];
        for (int i = 0; i < arreglo.length; i++) {
            arreglo[i] = (int) Math.floor(Math.random() * 1000 + 1);
        }
        arregloAux = arreglo.clone();
    }

    public void mostrarResultado(String resultado) {
        txtArregloOrdenado.setText(resultado);
    }

    public void mostrarArregloOriginal() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arreglo.length; i++) {
            sb.append(i).append(" - ").append(arreglo[i]).append("\n");
        }
        txtArregloOriginal.setText(sb.toString());
    }

    public void mostrarArregloOriginalEnConsola(){
        StringBuilder sb = new StringBuilder();
        for (int num : arreglo) {
            sb.append(num).append(" ");
        }
    }

    public void mostrarArregloOrdenado(String resultado) {
        StringBuilder sb = new StringBuilder();
        String[] valores = resultado.split(" "); // Dividir el resultado por espacios

        for (int i = 0; i < valores.length; i++) {
            sb.append(valores[i]); // Agregar el valor actual
            if (i != valores.length - 1) {
                sb.append("\n"); // Agregar salto de línea si no es el último valor
            }
        }

        txtArregloOrdenado.setText(sb.toString());
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSecuencia) {
            opcion = 1;
        } else if (e.getSource() == btnForkJoin) {
            opcion = 2;
        } else if (e.getSource() == btnExceSer) {
            opcion = 3;
        }
    }

    public int[] getArreglo(){
        return  this.arregloAux;
    }

    public void setOpcion(int opcion){
        this.opcion = opcion;
    }

    public int getOpcion(){
        return this.opcion;
    }

    public void setTiempo(String resultado) {
        String[] partes = resultado.split("\\|");

        // La segunda parte contiene el tiempo
        if (partes.length > 1) {
            String tiempo = partes[1];
            if (getOpcion() == 1) {
                lbTiempoSecuencial.setText(tiempo);
            }else if(getOpcion() == 2){
                lbTiemForkJoin.setText(tiempo);
            }else if (getOpcion() == 3){
                lbTiemExceSer.setText(tiempo);
            }
        } else {
            // En caso de que el formato no sea el esperado, establecer un mensaje de error
            lbTiempo1.setText("Error al obtener el tiempo");
        }
    }
}
