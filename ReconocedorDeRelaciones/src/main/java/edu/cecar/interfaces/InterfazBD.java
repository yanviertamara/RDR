/**
 * Clase: InterfazBD
 *
 * @version: 0.1
 *
 * Fecha de Creación: 4/03/2020
 *
 * Fecha de modificación: 
 *
 * @author: 92531165
 *
 * Copyright: CECAR
 *
 */


package edu.cecar.interfaces;


/**
 * Interfaz generica que define la 
 * operacion de insertar y consultar a
 * la base de datos
 */
public interface InterfazBD<Media> {
    
    String insertar(Media media);
    
    Media consultar(String path);
    
    
  
    
    
}
