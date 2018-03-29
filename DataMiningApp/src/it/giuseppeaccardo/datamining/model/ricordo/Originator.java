package it.giuseppeaccardo.datamining.model.ricordo;

import it.giuseppeaccardo.datamining.model.Cluster;
/**
 * <h1>Originator!</h1>
 * Classe che si occupa di salvare uno suo stato transitorio e di salvare fisicamente  il suo stato in memento.
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see Memento
 * @see Originator
 * @see MementoConcreto
 */
public class Originator {
	   /** Stato transitorio salvato **/
	   private Cluster[] statoSalvato;
	   /**
	    * Setta lo stato da salvare
	    * @param statoDaSalvare è lo stato del cluster che si vuole salvare
	    * **/
	   public void set(Cluster[] statoDaSalvare) { 
			statoSalvato = new Cluster[statoDaSalvare.length];
			for(int i = 0;i<statoDaSalvare.length; i++)
				statoSalvato[i] = (Cluster) statoDaSalvare[i].clone();
	       
	       System.out.println("Originator: setta stato cluster");
	   }
	   /**
	    * crea e restituisce il memento con lo stato salvato
	    * @return MementoConcreto restituisce il memento concreto contenento lo stato salvato in originator
	    * **/
	   public Memento saveToMemento() { 
	       return new MementoConcreto(this.statoSalvato); 
	   }
	   /**
	    * Ripristina uno stato da memento
	    * @param m memento da cui ripristinare lo stato
	    * @return statoSalvato ritorna lo stato ripristinato da memento
	    * **/
	   public Cluster[] restoreFromMemento(Memento m) {
			System.out.println("Originator: ripristina cluster da memento\n");
			statoSalvato = m.restoreState(); 
		    return statoSalvato;
	   }
	}   