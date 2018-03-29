package it.giuseppeaccardo.datamining.exception;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 * <h1>Insufficienti Feature Exception!</h1>
 * Classe che estende Exception ed è utilizzato per geestire l'eccezioni nel caso in cui
 * il numero di features scelte siano minori di due
 *	@author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 */
@SuppressWarnings("serial")
public class FeatureInsufficientiException extends Exception {
	private int featureInserite;
	public FeatureInsufficientiException(String messaggio, int featureInserite) {
		super(messaggio);
		this.featureInserite = featureInserite;
	}
	/** Gestione dell'exception creando una finestra di alert error **/
	public void handleException() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ATTENZIONE!");
		alert.setHeaderText("Evento non rispettato: "+super.getMessage());
		alert.setContentText("Hai selezionato solo "+featureInserite+" features. \nProva a ricalcolare inserendo almeno 2 features");
		alert.showAndWait();
	}

}
