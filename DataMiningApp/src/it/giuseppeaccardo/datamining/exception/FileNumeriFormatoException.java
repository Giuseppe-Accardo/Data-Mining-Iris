package it.giuseppeaccardo.datamining.exception;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 * <h1>FileMioIOException!</h1>
 * Classe che estende FileMioIOException ed è utilizzato per geestire l'eccezioni nel caso in cui
 * il file di iris non sia formattato con solo numeri
 *	@author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 */
@SuppressWarnings("serial")
public class FileNumeriFormatoException extends FileMioIOException{
	public FileNumeriFormatoException(String messaggio) {
		super(messaggio);
	}
	/** Gestione dell'exception creando una finestra di alert error **/
	@Override
	public void handleException() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERRORE");
		alert.setHeaderText("Errore durante la lettura del file: "+super.getMessage());
		alert.setContentText("Riprovare a caricare un file con una formattazione conforme ad Iris.data");
		alert.showAndWait();
	}

}
