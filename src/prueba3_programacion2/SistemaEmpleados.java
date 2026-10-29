
package prueba3_programacion2;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SistemaEmpleados extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SistemaEmpleados.class.getName());
    
    private ArrayList<Empleado> listaEmpleados = new ArrayList<>();
    private JTextField txtCodigo = new JTextField(10);
    private JTextField txtNombre = new JTextField(15);
    private JTextField txtSalarioBase = new JTextField(8);
    private JTextField txtExtra1 = new JTextField(8);
    private JTextField txtExtra2 = new JTextField(8);
    private JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Administrativo", "Por horas"});

    private DefaultTableModel modeloTabla;
    private JTable tablaEmpleados;

    private JLabel lblExtra1 = new JLabel("Bonificacion:");
    private JLabel lblExtra2 = new JLabel("Descuento (%):");
    
    public SistemaEmpleados() {
        setTitle("Sistema de Gestion de Empleados");
        setSize(950,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        JPanel panelFormulario = new JPanel (new GridLayout(6,2,5,5));
        
        panelFormulario.add(new JLabel("Codigo:"));
        panelFormulario.add(txtCodigo);
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Tipo:"));
        panelFormulario.add(cmbTipo);
        panelFormulario.add(new JLabel("Salario base:"));
        panelFormulario.add(txtSalarioBase);
        
        panelFormulario.add(lblExtra1);
        panelFormulario.add(txtExtra1);
        
        panelFormulario.add(lblExtra2);
        panelFormulario.add(txtExtra2);
        
        cmbTipo.addActionListener(e ->{
            if (cmbTipo.getSelectedItem().equals("Administrativo")){
                lblExtra1.setText("Bonificacion:");
                lblExtra2.setText("Descuento (%):");
            }else{
                lblExtra1.setText("Horas Trabajadas");
                lblExtra2.setText("Tarifa por hora:");
            }
            
        });
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnClonar = new JButton("Clonar");
        JButton btnComparar = new JButton("Comparar");
        JButton btnMaximo = new JButton("Salario Maximo");
        JButton btnPromedio = new JButton("Promedio");
        JButton btnLimpiar = new JButton("Limpiar");
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnClonar);
        panelBotones.add(btnComparar);
        panelBotones.add(btnMaximo);
        panelBotones.add(btnPromedio);
        panelBotones.add(btnLimpiar);
        
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelFormulario, BorderLayout.CENTER);
        panelNorte.add(panelBotones, BorderLayout.SOUTH);
        add(panelNorte, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"Codigo", "Nombre", "Tipo", "Salario Calculado", "Categoria"}, 0);
        tablaEmpleados = new JTable(modeloTabla);
        add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);
        
        btnRegistrar.addActionListener(e -> registrarEmpleado());
        btnBuscar.addActionListener(e -> buscarEmpleado());
        btnClonar.addActionListener(e -> clonarEmpleado());
        btnComparar.addActionListener(e -> compararEmpleados());
        btnMaximo.addActionListener(e -> mostrarSalarioMaximo());
        btnPromedio.addActionListener(e -> mostrarPromedio());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }
    
    private void registrarEmpleado(){
        try{
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            double salarioBase = Double.parseDouble(txtSalarioBase.getText().trim());
            String tipo = (String) cmbTipo.getSelectedItem();
            
            for(Empleado emp : listaEmpleados){
                if (emp.getCodigo().equalsIgnoreCase(codigo)){
                    JOptionPane.showMessageDialog(this, "El codigo ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            Empleado empleado;
            if(tipo.equals("Administrativo")){
                double bonificacion = Double.parseDouble(txtExtra1.getText().trim());
                double descuento = Double.parseDouble(txtExtra2.getText().trim());
                empleado = new EmpleadoAdministrativo(codigo, nombre, salarioBase, bonificacion, descuento);
            }else{
                int horas = Integer.parseInt(txtExtra1.getText().trim());
                double tarifa = Double.parseDouble(txtExtra2.getText().trim());
                empleado = new EmpleadoPorHoras(codigo, nombre, salarioBase, horas, tarifa);
            }
            listaEmpleados.add(empleado);
            actualizarTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Empleado registrado con exito");
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Por favor ingrese valores numericos validos", "Error", JOptionPane.ERROR_MESSAGE);
        }catch(IllegalArgumentException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Empleado emp : listaEmpleados) {
            String tipo = emp instanceof EmpleadoAdministrativo ? "Administrativo" : "Por horas";
            modeloTabla.addRow(new Object[]{
                emp.getCodigo(),
                emp.getNombre(),
                tipo,
                String.format("%.2f", emp.calcularSalario()),
                emp.obtenerCategoria()
            });
        }
    }
    
    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtSalarioBase.setText("");
        txtExtra1.setText("");
        txtExtra2.setText("");
        txtCodigo.requestFocus();
    }
    
    private void buscarEmpleado() {
        String codigo = JOptionPane.showInputDialog(this, "Ingrese el codigo del empleado a buscar:");
        if (codigo == null || codigo.trim().isEmpty()){ 
            return;
        }
        for (Empleado emp :listaEmpleados){
            if (emp.getCodigo().equalsIgnoreCase(codigo)){
                String tipo = emp instanceof EmpleadoAdministrativo ? "Administrativo" : "Por horas";
                String info = "Codigo: " + emp.getCodigo() + "\nNombre: " + emp.getNombre() + "\nTipo: " + tipo + "\nSalario: " + emp.calcularSalario() + "\nCategoria: " + emp.obtenerCategoria();
                JOptionPane.showMessageDialog(this, info, "Empleado Encontrado", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Empleado no encontrado", "Aviso", JOptionPane.WARNING_MESSAGE);
    }
    
    private void clonarEmpleado() {
        int fila = tablaEmpleados.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado de la tabla para clonar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Empleado original = listaEmpleados.get(fila);
        Empleado clon = original.clone();
        
        String nuevoCodigo = JOptionPane.showInputDialog(this, "Ingrese un nuevo codigo unico para el clon:", original.getCodigo() + "-CLON");
        if (nuevoCodigo == null || nuevoCodigo.trim().isEmpty()) return;

        for (Empleado emp : listaEmpleados) {
            if (emp.getCodigo().equalsIgnoreCase(nuevoCodigo)) {
                JOptionPane.showMessageDialog(this, "El codigo ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        clon.setCodigo(nuevoCodigo);
        clon.setNombre("Clon de " + original.getNombre());
        listaEmpleados.add(clon);
        actualizarTabla();
        JOptionPane.showMessageDialog(this, "Empleado clonado con exito");
    }

    private void compararEmpleados() {
        if (listaEmpleados.size() < 2) {
            JOptionPane.showMessageDialog(this, "Se necesitan al menos dos empleados registrados para comparar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String codigo1 = JOptionPane.showInputDialog(this, "Ingrese el codigo del primer empleado:");
        String codigo2 = JOptionPane.showInputDialog(this, "Ingrese el codigo del segundo empleado:");

        Empleado emp1 = null, emp2 = null;
        for (Empleado emp : listaEmpleados) {
            if (emp.getCodigo().equalsIgnoreCase(codigo1)) emp1 = emp;
            if (emp.getCodigo().equalsIgnoreCase(codigo2)) emp2 = emp;
        }

        if (emp1 == null || emp2 == null) {
            JOptionPane.showMessageDialog(this, "Uno o ambos codigos no fueron encontrados", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean mismaReferencia = (emp1 == emp2);
        boolean mismoSalario = emp1.calcularSalario() == emp2.calcularSalario();

        String resultado = 
                "Comparacion de objetos:\n" +
                "Misma referencia (==): " + mismaReferencia + "\n" +
                "Mismo salario calculado: " + mismoSalario + "\n" +
                "Empleado 1: " + emp1.getNombre() + " (" + emp1.calcularSalario() + ")\n" +
                "Empleado 2: " + emp2.getNombre() + " (" + emp2.calcularSalario() + ")";

        JOptionPane.showMessageDialog(this, resultado, "Resultado de Comparacion", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarSalarioMaximo() {
        if (listaEmpleados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay empleados registrados", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Empleado max = listaEmpleados.get(0);
        for (Empleado emp : listaEmpleados) {
            if (emp.calcularSalario() > max.calcularSalario()) {
                max = emp;
            }
        }

        JOptionPane.showMessageDialog(this, "Empleado con mayor salario:\nNombre: " + max.getNombre() + "\nSalario: " + max.calcularSalario(), "Salario Maximo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarPromedio() {
        if (listaEmpleados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay empleados registrados", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double suma = 0;
        for (Empleado emp : listaEmpleados) {
            suma += emp.calcularSalario();
        }
        double promedio = suma / listaEmpleados.size();

        JOptionPane.showMessageDialog(this, "Salario promedio: " + String.format("%.2f", promedio), "Promedio Salarial", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new SistemaEmpleados().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
