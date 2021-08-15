/**
 *Clase: ControladorScraping
 *
 *@version: 0.1
 *
 *Fecha de Creación: 06/03/2020
 *
 *Fecha de Modificación:
 *
 *@autor: Yanvier
 *
 *Copyright: CECAR
 *
 */
package edu.cecar.controlador;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Clase en la cual se realiza el scraping a los archivos multimedia
 *
 */
public class ControladorScraping {
    
    public int validarUrl(String baseUrl) {//metodo para validar las url
        int code = 0;
        try {
            URL url = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            code = connection.getResponseCode();

        } catch (MalformedURLException ex) {
            JOptionPane.showMessageDialog(null, "URL no valida");
        } catch (IOException ex) {
            ex.printStackTrace();

        }
        return code;
    }

    public List<String> listarEnlaces(String baseUrl, int limit) {//metodo para extraer los enlaces a visitar de la pagina raiz

        List<String> enlacesList = new ArrayList<String>();
        Document doc;
        if (validarUrl(baseUrl) >= 200 & validarUrl(baseUrl) < 300) {

            try {
                doc = Jsoup.connect(baseUrl).get();
                Elements links = doc.select("a");
                int i = 0;

                for (Element enlace : links) {

                    String nuevoEnlace = enlace.attr("href");

                    if (nuevoEnlace.startsWith(baseUrl) //Condicional para que los enlaces inicien solo con la pagina raiz
                            && !enlacesList.contains(nuevoEnlace) //para que no repita enlaces
                            && i < limit) { //limite para que no extraiga todos los enlaces

                        enlacesList.add(nuevoEnlace);
                        System.out.println("lista de links " + nuevoEnlace);
                        i++;
                        
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "URL No existe");
            return null;
        }
        return enlacesList;
    }

    public List<String> getUrlMedia(List<String> Urls) throws IOException {//Metodo para obtener los enlaces multimedia
        List<String> enlacesList = new ArrayList<String>();                //Recibe como parametro los url de descarga 
        for (String url : Urls) {
            Document var = Jsoup.connect(url).get();
            Elements tags = var.select("body");

            for (Element tag : tags) {

                Elements mediaImages = var.select("img");//se extraen las url de descarga de las imagenes

                for (Element src : mediaImages) {

                    if (src.tagName().equals("img")) {
                        String actualUrl = src.attr("src");
                        System.out.println("Link img " + actualUrl);
                        enlacesList.add(actualUrl);
                        //return enlacesList;
                    }
                }

                Elements mediaVideos = var.select("video")//se extraen las url de descarga de los videos
                        .select("source");

                for (Element src : mediaVideos) {

                    if (src.tagName().equals("source")) {
                        String actualUrl = src.attr("src");
                        System.out.println("Link video " + actualUrl);
                        enlacesList.add(actualUrl);
                        
                    }
                }

                Elements mediaAudios = var.select("audio")//se extraen las url de descarga de los audios
                        .select("source");

                for (Element src : mediaAudios) {

                    if (src.tagName().equals("source")) {
                        String actualUrl = src.attr("src");
                        System.out.println("Link audio " + actualUrl);
                        enlacesList.add(actualUrl);
                        
                    }
                }

            }
        }
        return enlacesList;
    }
 }
