package it.giuseppeaccardo.datamining.model.ricordo;

import java.util.LinkedList;
import java.util.Queue;

/**
 * <h1>Caretaker!</h1>
 * Classe necessaria per l'applciazione del pattern 'Memento'. Caretaker ha il compito di conservare i 'memento', ossia
 * in cui è imagazzinato lo stato dell'originator.
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see Memento
 * @see Originator
 * @see MementoConcreto
 */
public class Caretaker {
	 /** Lista dei memento salvati. La lista è sottoforma di Coda **/
	   private Queue<Memento> mementiSalvati = new LinkedList<Memento>(); //queue è interfaccia
	  
	   /** Aggiungi un memento nella Coda 
	   * @param m memento da salvare**/
	   public void addMemento(Memento m) { 
		   this.mementiSalvati.add(m); 
	   }
	   /** Estrai memento dalla coda
		 * @return memento**/
	   public Memento getMemento() { 
		   return this.mementiSalvati.poll(); 
	   }
}   
