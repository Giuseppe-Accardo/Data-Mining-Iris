package it.giuseppeaccardo.datamining.exception;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 * <h1>Algoritmo Divergente Exception!</h1>
 * Classe che estende Exception ed è utilizzato per geestire l'eccezioni nel caso in cui
 * l'algoritmo del K-Means diverge, a causa della scelte dei centroidi random troppo "vicini".
 *	@author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 */
@SuppressWarnings("serial")
public class AlgoritmoDivergenteException extends ArithmeticException{
	public AlgoritmoDivergenteException(String messaggio) {
		super(messaggio);
	}
	/** Gestione dell'exception creando una finestra di alert error **/
	public void handleException() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ATTENZIONE");
		alert.setHeaderText("Errore durante la clusterizzazione mediante il Kmeans: "+super.getMessage());
		alert.setContentText("Il numero dei cluster e la scelta dei centroidi (random) non possono garantire un risultato finale.\n"
							+"Presi K centroidi random molto vicini tra loro, non garantiscono la convergenza dell'algoritmo nella costruzione dei cluster.\n"
							+ "\nRiprova a ricalcolare il datamining con il Kmeans!");
		alert.showAndWait();
	}
}
