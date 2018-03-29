package it.giuseppeaccardo.datamining.model.ricordo;

import it.giuseppeaccardo.datamining.model.Cluster;
/**
 * <h1>Memento Concreto!</h1>
 * Implemento l'interfaccia memento e viene utilizzato per conservare uno stato di originator che lo richiama per
 * fargli immagazzinare uno stato.
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see Memento
 * @see Originator
 * @see MementoConcreto
 */
public class MementoConcreto implements Memento {
	/** Stato del memento **/
   	private Cluster[] statoMemento;
   	/**
   	 * Registra uno stato di originator
   	 * @param statoDaSalvare stato che originator vuole salvare im memento
   	 */
   public MementoConcreto(Cluster[] statoDaSalvare) { 
		this.statoMemento = statoDaSalvare; 
	}
  	/**
  	 * Ripristina uno stato di originator
  	 * @return  statoMemento ritorna lo stato di memento
  	 */
   public Cluster[] restoreState() { 
	return statoMemento; 
	}
}
