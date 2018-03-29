package it.giuseppeaccardo.datamining.exception;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 * <h1>File non trovato Exception!</h1>
 * Classe che estende FileMioIOException ed è utilizzata per geestire l'eccezioni di tipo 'FileNotFound'
 *	@author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 */
@SuppressWarnings("serial")
public class FileNonTrovatoException extends FileMioIOException{
	public FileNonTrovatoException(String messaggio) {
		super(messaggio);
	}
	
	/** Gestione dell'exception creando una finestra di alert error **/
	@Override
	public void handleException() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERRORE");
		alert.setHeaderText("Errore durante la lettura del file: "+super.getMessage());
		alert.setContentText("Riprovare a caricare un file esistente o leggibile.");
		alert.showAndWait();
	}
}
