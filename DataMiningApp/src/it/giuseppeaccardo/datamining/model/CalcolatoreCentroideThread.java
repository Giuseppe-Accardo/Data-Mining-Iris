package it.giuseppeaccardo.datamining.model;

import java.util.ArrayList;
import it.giuseppeaccardo.datamining.model.Cluster;
import it.giuseppeaccardo.datamining.model.Dataset;
/**
 * <h1>CalcolatoreCentroideThread!</h1>
 * Classe necessaria per l'esecuzione dei thread in parallelo. Tale implementa l'interfacca Runnable di cui
 * si riscrive il metodo run(), necessario per il thread poichè saranno inserite le operazioni che tale dovrà eseguire.
 * In questo caso, i thread in parallelo, riceveranno un cluster su cui effettaure il calcolo del centroide utilizzando
 * la media aritmetica e tenendo conto di quanti e quali elementi sono inseriti per ogni cluster.
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see DataMining
 * @see Cluster
 * @see Dataset
 */
public class CalcolatoreCentroideThread implements Runnable {
	private Cluster cluster;
	private static Dataset dataset; // Condivisa tra thread
	/**
	 * Costruttore che associa il cluster su cui dovrà lavorare il thread
	 * @param cluster cluster
	 * @return CalcolatoreCentroideThread sara' l'instanza di Runnable che servirà al costrutture
	 * Thread() per capire cosa dovrà effettuare il thread all'invocazione del metodo start.
	 */
	public CalcolatoreCentroideThread(Cluster cluster)
	{	
		this.cluster = cluster;
	}
	
	/**
	 * Implementazione del metodo run ereditato da runnable. Per ogni cluster, verrà assegnato un nuovo centroide, calcolato
	 * sulla media aritmetica degli elementi che esso contiene.
	 */
	@Override
	public void run() {
		double sumNew;		
		//Per ogni cluster
		cluster.centroide.clear(); 
		ArrayList<Double> newCentroide = new ArrayList<Double>(); //centroide e' ad esempio una tripla di faetures

		//per ogni colonna / feature
		for(int iFeature = 0; iFeature<getDataset().getFeatureUsate().size(); iFeature++)
		{
			sumNew = 0;
			//Per ogni record/colonna contenuto dal cluster, calcola la media
    		for(int iElemento=0; iElemento< cluster.getIndiciInseriti().size(); iElemento++)
    		{	
    			sumNew += dataset.getCella(cluster.getIndiciInseriti().get(iElemento), iFeature); 
    		}
    		/* Aggiungi il centroide medio. Se il centroide è individuato da 3 feature, tale procedimento sara ripetuto
    		 * 3 volte*/
    		newCentroide.add(sumNew/cluster.getIndiciInseriti().size());
		}
		/* Setta centroide finale */
		cluster.setCentroide(newCentroide);
	}
	/**
	 * Ottieni il dataset condiviso e static
	 * @return Dataset dataset condiviso thread**/
	public static Dataset getDataset() {
		return dataset;
	}
	/**
	 * Setta il dataset condiviso tra tutti i thread
	 * @param Dataset dataset condiviso thread**/
	public static void setDataset(Dataset dataset) {
		CalcolatoreCentroideThread.dataset = dataset;
	}

}
