/**
 *Clase: ControladorBaseDatos
 *
 *@version: 0.1
 *
 *Fecha de Creación: 11/03/2020
 *
 *Fecha de Modificación:
 *
 *@autor: Yanvier
 *
 *Copyright: CECAR
 *
 */
package edu.cecar.controlador;

import edu.cecar.interfaces.InterfazBD;
import edu.cecar.modelo.Media;
import edu.cecar.componentesReutilizables.ConectarMySQL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Blob;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Clase que realiza las operaciones de insertar y consultar en la tabla archivos
 *
 */
public class ControladorBaseDatos implements InterfazBD<Media> {

    private String nombreTabla = "archivos";

    public String insertar(Media media) {
        String resultado = "OK";
        try {

            ConectarMySQL conexion = new ConectarMySQL("127.0.0.1", "proyectoarchivos", "root", "");

            try {
                File file = new File(media.getRuta()); // se obtiene la ruta del archivo para insertarlo en la base de datos
                FileInputStream fis = new FileInputStream(file);

                PreparedStatement preparedStatement = conexion.getConexion().
                        prepareStatement("insert IGNORE into " + nombreTabla + " values(?,?,?,?)");

                preparedStatement.setString(1, media.getURL());
                preparedStatement.setString(2, media.getTipoArchivo());
                preparedStatement.setString(3, media.getFecha());
                preparedStatement.setBinaryStream(4, fis, (int) file.length());
                preparedStatement.execute();

                preparedStatement.close();
                fis.close();

            } catch (SQLException ex) {
                resultado = ex.getMessage();
            } catch (Exception ex) {
                resultado = ex.getMessage();
            }

            
        } catch (Exception ex) {
            Logger.getLogger(ControladorBaseDatos.class.getName()).log(Level.SEVERE, null, ex);

        }
        return resultado;
    }

    public Media consultar(String ruta) {

        File image = new File(ruta);
        Blob imagen = null;
        Media media = new Media();
        try {

            int tamImg = (int) image.length();

            ConectarMySQL conexion = new ConectarMySQL("127.0.0.1", "proyectoarchivos", "root", "");
            PreparedStatement preparedStatement = conexion.
                    getConexion().
                    prepareStatement("Select * from " + nombreTabla);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                imagen = resultSet.getBlob("archivo");
                
                int tamBlob = (int) imagen.length();

                if (tamBlob == tamImg) {
                    System.out.println("si esta en la base de datos");

                    media.setURL(resultSet.getString("url"));
                    media.setTipoArchivo(resultSet.getString("tipoArchivo"));
                    media.setFecha(resultSet.getString("fecha"));
                    resultSet.close();

                    return media;
                }
            }
            JOptionPane.showMessageDialog(null, "No se encuentra en la base de datos");
            resultSet.close();
            return null;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControladorBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ControladorBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
