package ec.edu.sistemalicencias.controller;

import ec.edu.sistemalicencias.config.SesionUsuario;
import ec.edu.sistemalicencias.model.entities.Usuario;
import ec.edu.sistemalicencias.model.interfaces.RolConstantes;
import ec.edu.sistemalicencias.service.UsuarioService;
import ec.edu.sistemalicencias.view.GestionUsuariosView;
import ec.edu.sistemalicencias.view.MainView;

import javax.swing.*;
import java.awt.*;

public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController() {
        this.usuarioService = new UsuarioService();
    }

    public void iniciarSesion(String usuario, String clave, JFrame vistaLogin) {
        // Validar campos vacíos antes de ir a la base de datos (ahorra recursos)
        if (usuario == null || usuario.trim().isEmpty() || clave == null || clave.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vistaLogin, "Por favor, ingrese usuario y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 1. Autenticación
            if (usuarioService.autenticar(usuario.trim(), clave.trim())) {

                // 2. Recuperar usuario de la sesión
                Usuario userLogueado = SesionUsuario.getInstancia().getUsuarioLogueado();

                if (userLogueado == null || userLogueado.getRol() == null) {
                    throw new Exception("Error al recuperar el perfil del usuario.");
                }

                String rol = userLogueado.getRol().trim();

                // 3. Ejecutar el cambio de ventana en el hilo de interfaz de usuario (EDT)
                SwingUtilities.invokeLater(() -> {
                    vistaLogin.dispose(); // Cerrar Login

                    if (rol.equalsIgnoreCase(RolConstantes.ADMIN)) {
                        new GestionUsuariosView().setVisible(true);
                    } else if (rol.equalsIgnoreCase(RolConstantes.ANALISTA)) {
                        new MainView().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Rol no autorizado: " + rol, "Error de Acceso", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                    }
                });

            } else {
                JOptionPane.showMessageDialog(vistaLogin, "Usuario o contraseña incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vistaLogin, "Error técnico: " + e.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }
}