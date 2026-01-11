package ec.edu.sistemalicencias.controller;

import ec.edu.sistemalicencias.model.entities.Usuario;
import ec.edu.sistemalicencias.service.UsuarioService;
import ec.edu.sistemalicencias.util.PDFGenerator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController() {
        this.service = new UsuarioService();
    }

    /**
     * Crea un nuevo analista validando los campos básicos antes de enviar al servicio.
     */
    public void crearAnalista(String user, String pass, String nombre, String cedula, String email) {
        try {
            // Instancia con el constructor completo (Asegúrate que tu entidad Usuario lo tenga)
            Usuario u = new Usuario();
            u.setUsuario(user);
            u.setClave(pass);
            u.setNombreCompleto(nombre);
            u.setCedula(cedula);
            u.setEmail(email);

            service.registrarAnalista(u);

            JOptionPane.showMessageDialog(null, "Analista '" + nombre + "' registrado exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo registrar: " + e.getMessage(),
                    "Error de Registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gestiona la obtención de datos y la apertura del cuadro de diálogo para guardar el PDF.
     */
    public void generarReportePDF(LocalDate inicio, LocalDate fin) {
        try {
            // 1. Obtener datos del servicio
            List<Usuario> lista = service.obtenerReporteAnalistas(inicio, fin);

            if (lista == null || lista.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se encontraron registros en el rango de fechas seleccionado.",
                        "Sin Datos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Configurar el selector de archivos
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Reporte de Usuarios");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Documentos PDF (*.pdf)", "pdf"));

            // Sugerir nombre de archivo con la fecha actual
            String nombreSugerido = "Reporte_Analistas_" + LocalDate.now() + ".pdf";
            fileChooser.setSelectedFile(new File(nombreSugerido));

            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File archivoDestino = fileChooser.getSelectedFile();
                String ruta = archivoDestino.getAbsolutePath();

                // Asegurar extensión .pdf
                if (!ruta.toLowerCase().endsWith(".pdf")) {
                    ruta += ".pdf";
                }

                // 3. Generación física del PDF
                PDFGenerator.generarReporteUsuarios(lista, ruta);

                JOptionPane.showMessageDialog(null, "El reporte ha sido exportado a:\n" + ruta,
                        "Reporte Generado", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SecurityException se) {
            JOptionPane.showMessageDialog(null, se.getMessage(), "Permiso Denegado", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error inesperado al generar el PDF: " + e.getMessage(),
                    "Error de Exportación", JOptionPane.ERROR_MESSAGE);
        }
    }
}