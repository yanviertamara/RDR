/**
 *Clase: ControladorDescarga
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

import edu.cecar.modelo.Media;

import java.io.FileOutputStream;
import java.io.InputStream;

import java.net.URL;
import java.net.URLConnection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * Clase que realiza la descarga de multimedia
 *
 */
public class ControladorDescarga {

    public ArrayList<Media> descargarMedia(List<String> urls) {//metodo para descargar multimedia

        ArrayList<Media> datosMedia = new ArrayList<>();

        for (String url : urls) {
            Media archivo = new Media();
            try {
                // Url con el archivo
                URL urlFile = new URL(url);
               
                // establecemos conexion
                URLConnection urlCon = urlFile.openConnection();
                              
                System.out.println(urlCon.getContentType());
                //Extraemos el nombre y extension del enlace
                int p = url.lastIndexOf("/");
                String nomArchivo = url.substring(p + 1);

                int p2 = nomArchivo.lastIndexOf(".");
                String extension = nomArchivo.substring(p2 + 1);

                // Se obtiene el inputStream del archivo web y se abre el fichero
                // local.
                InputStream is = urlCon.getInputStream();

                FileOutputStream fos = new FileOutputStream("media/" + nomArchivo);
                String ruta = "media/" + nomArchivo;

                // Lectura de la foto de la web y escritura en fichero local
                byte[] array = new byte[1024]; // buffer temporal de lectura.
                int leido = is.read(array);
                while (leido > 0) {
                    fos.write(array, 0, leido);
                    leido = is.read(array);
                }

                // cierre de conexion y fichero.
                is.close();
                fos.close();

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

                archivo.setURL(url);
                archivo.setTipoArchivo(extension);
                archivo.setFecha(dateFormat.format(date));
                archivo.setRuta(ruta);
                datosMedia.add(archivo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(null, "Descarga completada");
        return datosMedia;
    }

}
