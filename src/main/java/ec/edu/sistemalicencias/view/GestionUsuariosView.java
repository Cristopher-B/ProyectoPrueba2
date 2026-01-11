package ec.edu.sistemalicencias.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import ec.edu.sistemalicencias.config.SesionUsuario;
import ec.edu.sistemalicencias.controller.UsuarioController;
import ec.edu.sistemalicencias.model.entities.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista de Gestión de Usuarios (Administrador)
 * Permite CRUD de analistas y generación de reportes PDF.
 */
public class GestionUsuariosView extends JFrame {
    private JPanel panel11;
    private JLabel lblGestionUsuarios;
    private JLabel lblDatosAnalista;
    private JPanel panelCampos;
    private JTextField txtCedula;
    private JTextField txtUsuario;
    private JTextField txtNombre;
    private JTextField txtClave;
    private JTextField txtEmail;
    private JButton btnGuardarAnalista;
    private JLabel lblCedula;
    private JLabel lblUsuario;
    private JLabel lblNombre;
    private JLabel lblClave;
    private JLabel lblEmail;
    private JScrollPane panelTabla;
    private JTable table1;
    private JPanel panelAcciones;
    private JButton btnLimpiar;
    private JButton btnEliminar;
    private JButton btnPDF;
    private JPanel panelInferior;
    private JButton btnCerrarSesion;

    // Lógica
    private final UsuarioController usuarioController;
    private DefaultTableModel modeloTabla;
    private Long idUsuarioSeleccionado = null;

    public GestionUsuariosView() {
        this.usuarioController = new UsuarioController();

        // Configuración básica del Frame
        setTitle("Módulo de Administración - Sistema de Licencias ANT");
        setContentPane(panel11); // panel11 es el contenedor principal del .form
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        aplicarEstilosUI();
        configurarTabla();
        inicializarEventos();
        cargarDatosTabla();

        pack();
        setSize(950, 650);
        setLocationRelativeTo(null);
    }

    private void aplicarEstilosUI() {
        panel11.setBorder(new EmptyBorder(20, 20, 20, 20));
        lblGestionUsuarios.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblGestionUsuarios.setForeground(new Color(28, 40, 65));

        btnEliminar.setEnabled(false);

        btnLimpiar.setToolTipText("Limpiar campos y deseleccionar tabla");

        //Cambiar a un puntero
        btnEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardarAnalista.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnPDF.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLimpiar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Cédula", "Nombre Completo", "Usuario", "Email"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table1.setModel(modeloTabla);

        // Ocultar columna ID para el usuario, pero mantenerla para lógica interna
        table1.getColumnModel().getColumn(0).setMinWidth(0);
        table1.getColumnModel().getColumn(0).setMaxWidth(0);
        table1.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    private void inicializarEventos() {
        // Guardar
        btnGuardarAnalista.addActionListener(e -> guardarAnalista());
        // Eliminar
        btnEliminar.addActionListener(e -> eliminarAnalista());
        // PDF Dinámico
        btnPDF.addActionListener(e -> gestionarReportePDF());
        // Limpiar
        btnLimpiar.addActionListener(e -> limpiarInterfaz());
        // Cerrar Sesión
        btnCerrarSesion.addActionListener(e -> {
            SesionUsuario.getInstancia().cerrarSesion();
            this.dispose();
            new LoginView().setVisible(true);
        });

        // Selección en Tabla
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = table1.getSelectedRow();
                if (fila != -1) {
                    idUsuarioSeleccionado = (Long) modeloTabla.getValueAt(fila, 0);
                    String nombre = (String) modeloTabla.getValueAt(fila, 2);

                    btnEliminar.setEnabled(true);
                    btnPDF.setText("Generar PDF de: " + nombre);
                }
            }
        });
    }

    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0);
        List<Usuario> lista = usuarioController.obtenerTodosAnalistas();
        for (Usuario u : lista) {
            modeloTabla.addRow(new Object[]{
                    u.getId(), u.getCedula(), u.getNombreCompleto(), u.getUsuario(), u.getEmail()
            });
        }
    }

    private void guardarAnalista() {
        if (txtCedula.getText().isEmpty() || txtUsuario.getText().isEmpty() || txtClave.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los campos obligatorios", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        usuarioController.crearAnalista(
                txtUsuario.getText(), txtClave.getText(), txtNombre.getText(), txtCedula.getText(), txtEmail.getText()
        );

        JOptionPane.showMessageDialog(this, "Analista registrado exitosamente.");
        limpiarInterfaz();
        cargarDatosTabla();
    }

    private void eliminarAnalista() {
        if (idUsuarioSeleccionado == null) return;

        int op = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar al analista seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            if (usuarioController.eliminarUsuario(idUsuarioSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                limpiarInterfaz();
                cargarDatosTabla();
            }
        }
    }

    private void gestionarReportePDF() {
        if (idUsuarioSeleccionado == null) {
            usuarioController.generarReportePDF(null, null); // Reporte total
        } else {
            usuarioController.generarReporteIndividual(idUsuarioSeleccionado); // Reporte individual
        }
    }

    private void limpiarInterfaz() {
        txtCedula.setText("");
        txtUsuario.setText("");
        txtNombre.setText("");
        txtClave.setText("");
        txtEmail.setText("");
        table1.clearSelection();
        idUsuarioSeleccionado = null;
        btnEliminar.setEnabled(false);
        btnPDF.setText("Generar PDF");
        txtCedula.requestFocus();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
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
        panel11.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        lblGestionUsuarios = new JLabel();
        lblGestionUsuarios.setText("GESTIÓN DE USUARIOS");
        panel11.add(lblGestionUsuarios, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblDatosAnalista = new JLabel();
        lblDatosAnalista.setText("DATOS DEL NUEVO ANALISTA");
        panel11.add(lblDatosAnalista, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel11.add(panelCampos, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lblCedula = new JLabel();
        lblCedula.setText("Cédula:");
        panelCampos.add(lblCedula, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtCedula = new JTextField();
        panelCampos.add(txtCedula, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lblUsuario = new JLabel();
        lblUsuario.setText("Usuario:");
        panelCampos.add(lblUsuario, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtUsuario = new JTextField();
        panelCampos.add(txtUsuario, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lblNombre = new JLabel();
        lblNombre.setText("Nombre:");
        panelCampos.add(lblNombre, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblClave = new JLabel();
        lblClave.setText("Clave:");
        panelCampos.add(lblClave, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtClave = new JTextField();
        panelCampos.add(txtClave, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lblEmail = new JLabel();
        lblEmail.setText("Email:");
        panelCampos.add(lblEmail, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtEmail = new JTextField();
        panelCampos.add(txtEmail, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnGuardarAnalista = new JButton();
        btnGuardarAnalista.setText("Guardar nuevo ANALISTA");
        panelCampos.add(btnGuardarAnalista, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelTabla = new JScrollPane();
        panel11.add(panelTabla, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        panelTabla.setViewportView(table1);
        panelAcciones = new JPanel();
        panelAcciones.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel11.add(panelAcciones, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnLimpiar = new JButton();
        btnLimpiar.setText("Limpiar");
        panelAcciones.add(btnLimpiar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnEliminar = new JButton();
        btnEliminar.setText("Eliminar");
        panelAcciones.add(btnEliminar, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnPDF = new JButton();
        btnPDF.setText("Generar PDF");
        panelAcciones.add(btnPDF, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelInferior = new JPanel();
        panelInferior.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel11.add(panelInferior, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnCerrarSesion = new JButton();
        btnCerrarSesion.setText("Volver al INICIO");
        panelInferior.add(btnCerrarSesion, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panelInferior.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel11;
    }
}