package ec.edu.sistemalicencias;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.view.LoginView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel: " + e.getMessage());
        }

        DatabaseConfig dbConfig = DatabaseConfig.getInstance();

        SwingUtilities.invokeLater(() -> {
            // 1. Mostrar mensaje de inicio
            mostrarPantallaInicio();

            // 2. Verificar conexión a BD
            if (!dbConfig.verificarConexion()) {
                mostrarErrorConexion();
                return;
            }
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }

    private static void mostrarPantallaInicio() {
        JOptionPane.showMessageDialog(
                null,
                "SISTEMA DE LICENCIAS DE CONDUCIR - ECUADOR\n\n" +
                        "Agencia Nacional de Tránsito\n" +
                        "Versión 1.0\n\n" +
                        "Desarrollado con:\n" +
                        "- Java 21\n" +
                        "- PostgreSQL Database con Supabase\n" +
                        "- Arquitectura MVC\n" +
                        "- iText PDF\n\n" +
                        "Iniciando sistema...",
                "Bienvenido",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private static void mostrarErrorConexion() {
        String mensaje = "ERROR DE CONEXIÓN A BASE DE DATOS\n" +
                "La aplicación se cerrará.";
        JOptionPane.showMessageDialog(null, mensaje, "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}