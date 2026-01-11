package ec.edu.sistemalicencias.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import ec.edu.sistemalicencias.config.SesionUsuario;
import ec.edu.sistemalicencias.controller.UsuarioController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GestionUsuariosView extends JFrame {
    private JPanel panel11;
    private JLabel lblGestionUsuarios;
    private JLabel lblDatosAnalista;
    private JTextField txtCedula;
    private JTextField txtUsuario;
    private JTextField txtNombre;
    private JTextField txtClave;
    private JTextField txtEmail;
    private JButton btnGuardarAnalista;
    private JPanel panel12;
    private JTextField txtDesde;
    private JTextField txtHasta;
    private JButton btnPDF;
    private JLabel lblReporteTotal;
    private JLabel lblDesde;
    private JLabel lblHasta;
    private JLabel lblNombre;
    private JLabel lblEmail;
    private JLabel lblUsuario;
    private JLabel lblClave;
    private JPanel panel13;
    private JButton btnCerrarSesion;

    private final UsuarioController usuarioController;
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public GestionUsuariosView() {
        // 1. Inicializar componentes generados por el Designer primero
        $$$setupUI$$$();

        this.usuarioController = new UsuarioController();

        setTitle("Módulo de Administración - ANT");
        setContentPane(panel11);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        aplicarEstilosUI();

        pack();
        setSize(750, 600);
        setLocationRelativeTo(null);

        // Eventos
        btnGuardarAnalista.addActionListener(e -> guardarNuevoAnalista());
        btnPDF.addActionListener(e -> generarReporte());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    private void aplicarEstilosUI() {
        panel11.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel11.setBackground(new Color(245, 245, 245));

        lblGestionUsuarios.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblGestionUsuarios.setForeground(new Color(28, 40, 65));

        panel12.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "GENERACIÓN DE REPORTES",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(41, 128, 185)));

    }

    private void guardarNuevoAnalista() {
        String cedula = txtCedula.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String nombre = txtNombre.getText().trim();
        String clave = txtClave.getText().trim();
        String email = txtEmail.getText().trim();

        if (cedula.isEmpty() || usuario.isEmpty() || nombre.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La Cédula, Usuario, Nombre y Clave son obligatorios.",
                    "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            usuarioController.crearAnalista(usuario, clave, nombre, cedula, email);
            JOptionPane.showMessageDialog(this, "Analista creado exitosamente en Supabase.");
            limpiarCamposRegistro();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarReporte() {
        LocalDate fechaInicio = null;
        LocalDate fechaFin = null;
        String sDesde = txtDesde.getText().trim();
        String sHasta = txtHasta.getText().trim();

        try {
            // Solo parsear si ambos campos tienen texto
            if (!sDesde.isEmpty() && !sHasta.isEmpty()) {
                fechaInicio = LocalDate.parse(sDesde, DATE_FORMATTER);
                fechaFin = LocalDate.parse(sHasta, DATE_FORMATTER);
            }

            usuarioController.generarReportePDF(fechaInicio, fechaFin);

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd/MM/yyyy",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Desea cerrar la sesión administrativa y volver al login?",
                "Confirmar Salida", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            SesionUsuario.getInstancia().cerrarSesion();
            this.dispose();
            new LoginView().setVisible(true);
        }
    }

    private void limpiarCamposRegistro() {
        txtCedula.setText("");
        txtUsuario.setText("");
        txtNombre.setText("");
        txtClave.setText("");
        txtEmail.setText("");
        txtCedula.requestFocus();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(8, 5, new Insets(0, 0, 0, 0), -1, -1));
        lblGestionUsuarios = new JLabel();
        lblGestionUsuarios.setText("GESTIÓN DE USUARIOS");
        panel11.add(lblGestionUsuarios, new GridConstraints(0, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblDatosAnalista = new JLabel();
        lblDatosAnalista.setText("DATOS DEL NUEVO ANALISTA");
        panel11.add(lblDatosAnalista, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Cédula:");
        panel11.add(label1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        txtCedula = new JTextField();
        txtCedula.setText("");
        panel11.add(txtCedula, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 2, false));
        txtUsuario = new JTextField();
        panel11.add(txtUsuario, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 2, false));
        lblUsuario = new JLabel();
        lblUsuario.setText("Usuario");
        panel11.add(lblUsuario, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        lblNombre = new JLabel();
        lblNombre.setText("Nombre:");
        panel11.add(lblNombre, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        lblClave = new JLabel();
        lblClave.setText("Clave:");
        panel11.add(lblClave, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        lblEmail = new JLabel();
        lblEmail.setText("Email:");
        panel11.add(lblEmail, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        txtNombre = new JTextField();
        panel11.add(txtNombre, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 2, false));
        txtClave = new JTextField();
        panel11.add(txtClave, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 2, false));
        txtEmail = new JTextField();
        panel11.add(txtEmail, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 2, false));
        btnGuardarAnalista = new JButton();
        btnGuardarAnalista.setText("Guardar Analista");
        panel11.add(btnGuardarAnalista, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(5, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel11.add(panel12, new GridConstraints(6, 0, 2, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("REPORTES");
        panel12.add(label2, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblDesde = new JLabel();
        lblDesde.setText("Desde: [dd/mm/aaaa]");
        panel12.add(lblDesde, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        txtDesde = new JTextField();
        txtDesde.setText("");
        panel12.add(txtDesde, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 2, false));
        txtHasta = new JTextField();
        txtHasta.setText("");
        panel12.add(txtHasta, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 2, false));
        lblHasta = new JLabel();
        lblHasta.setText("Hasta: [dd/mm/aaaa]");
        panel12.add(lblHasta, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        lblReporteTotal = new JLabel();
        lblReporteTotal.setText("(Dejar vacío para reporte total)");
        panel12.add(lblReporteTotal, new GridConstraints(2, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        btnPDF = new JButton();
        btnPDF.setText("Generar PDF");
        panel12.add(btnPDF, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel12.add(panel13, new GridConstraints(4, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnCerrarSesion = new JButton();
        btnCerrarSesion.setText("Volver al INICIO");
        panel13.add(btnCerrarSesion, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel13.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel11;
    }

}