/**
 * PACKAGE RIGUARDANTE LA VISUALIZZAZIONE DEL GRAFICO
 */
package it.giuseppeaccardo.datamining.visualizzazione;

import it.giuseppeaccardo.datamining.model.DataMining;
import javafx.scene.Scene;

/**
 * <h1>Interfaccia di Visualizza Grafico per Strategy!</h1><br>
 * Interfaccia predisposta per l'applicazione del design pattern <i>'Strategy' </i>per supportare l'intercambiabilità tra più metodi.<br>
 * Le classi che implementeranno tale interfaccia funzionale, effettueranno override del metodo visualizza.
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see grafico2D 
 * @see grafico3D
 */

@FunctionalInterface
public interface IvisualizzaGraficoStrategy {
	   /**
	   * Metodo astratto per riscrivere la corrispondente visualizzazione del grafico
	   * @param model Datamining costruito a partire da un dataset.
	   * @return Scene Ritorna la scena su cui sarà presente il grafico costruito dal corrispondente metodo "virtuale"
	   */
	public Scene visualizzaGrafico(DataMining model);
}
