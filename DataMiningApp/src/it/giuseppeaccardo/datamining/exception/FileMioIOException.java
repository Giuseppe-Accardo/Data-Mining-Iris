package it.giuseppeaccardo.datamining.exception;

import java.io.IOException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 * <h1>FileMioIOException!</h1>
 * Classe che estende IOExceptio ed è utilizzato per geestire l'eccezioni di tipo IOException
 *	@author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 */
@SuppressWarnings("serial")
public class FileMioIOException extends IOException{
	public FileMioIOException(String messaggio) {
		super(messaggio);
	}
	/** Gestione dell'exception creando una finestra di alert error **/
	public void handleException() {
		/* Crea una finestra di alert error*/
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERRORE");
		alert.setHeaderText("Errore durante l'IO file: "+super.getMessage());
		alert.setContentText("Riprovare a caricare un file.");
		alert.showAndWait();
	}
}
