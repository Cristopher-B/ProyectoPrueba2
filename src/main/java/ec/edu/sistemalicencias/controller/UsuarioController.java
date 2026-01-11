package ec.edu.sistemalicencias.controller;

import ec.edu.sistemalicencias.dao.UsuarioDAO;
import ec.edu.sistemalicencias.model.entities.Usuario;
import ec.edu.sistemalicencias.model.exceptions.BaseDatosException;
import ec.edu.sistemalicencias.util.PDFGenerator;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioController {
    private final UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Obtiene todos los analistas registrados para llenar la tabla de la vista.
     */
    public List<Usuario> obtenerTodosAnalistas() {
        try {
            // Pasamos null en las fechas para que el DAO traiga todos los registros
            return usuarioDAO.listarPorFechas(null, null);
        } catch (BaseDatosException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar analistas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Crea un nuevo analista en la base de datos de Supabase.
     */
    public void crearAnalista(String usuario, String clave, String nombre, String cedula, String email) {
        try {
            Usuario nuevo = new Usuario();
            nuevo.setUsuario(usuario);
            nuevo.setClave(clave); // En un sistema real, aquí se debería encriptar
            nuevo.setNombreCompleto(nombre);
            nuevo.setCedula(cedula);
            nuevo.setEmail(email);
            nuevo.setRol("ANALISTA");

            usuarioDAO.insertar(nuevo);
        } catch (BaseDatosException e) {
            throw new RuntimeException("Error al guardar: " + e.getMessage());
        }
    }

    /**
     * Elimina un usuario por su ID.
     */
    public boolean eliminarUsuario(Long id) {
        try {
            return usuarioDAO.eliminar(id);
        } catch (BaseDatosException e) {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Genera el reporte PDF total (cuando no hay selección).
     */
    public void generarReportePDF(LocalDate inicio, LocalDate fin) {
        try {
            List<Usuario> lista = usuarioDAO.listarPorFechas(inicio, fin);
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay datos para el reporte.");
                return;
            }
            PDFGenerator.generarReporteUsuarios(lista, "Reporte_General_Usuarios.pdf");
            JOptionPane.showMessageDialog(null, "Reporte general generado con éxito.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar PDF: " + e.getMessage());
        }
    }

    /**
     * Genera un reporte PDF de un único usuario seleccionado en la tabla.
     */
    public void generarReporteIndividual(Long id) {
        try {
            // Buscamos el usuario específico
            Usuario u = usuarioDAO.buscarPorId(id);
            if (u != null) {
                // PDFGenerator suele recibir una lista, así que creamos una con un solo elemento
                List<Usuario> listaIndividual = Collections.singletonList(u);
                String nombreArchivo = "Reporte_Usuario_" + u.getCedula() + ".pdf";

                PDFGenerator.generarReporteUsuarios(listaIndividual, nombreArchivo);
                JOptionPane.showMessageDialog(null, "Reporte individual generado: " + nombreArchivo);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar reporte individual: " + e.getMessage());
        }
    }
}