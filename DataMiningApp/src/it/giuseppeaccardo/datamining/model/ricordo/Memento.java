package it.giuseppeaccardo.datamining.model.ricordo;

import it.giuseppeaccardo.datamining.model.Cluster;
/**
 * <h1>Memento!</h1>
 * definisce l'interfaccia di memento e viene utilizzato per conservare uno stato di originator. 
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see Memento
 * @see Originator
 * @see MementoConcreto
 */
public interface Memento{
	/** Metodo necessario per ripristinare uno stato salvat
	 * 	@return clusters Cluster memorizzati **/
	public Cluster[] restoreState();
}
