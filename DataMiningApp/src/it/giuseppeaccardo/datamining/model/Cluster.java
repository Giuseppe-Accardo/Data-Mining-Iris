package it.giuseppeaccardo.datamining.model;
import java.util.ArrayList;

/**
 * <h1>Cluster!</h1>
 * Cluster rappresenta il "grappolo" o gruppo, in cui sono agglomerati i punti del dataset "piu simili".
 * Un cluster è rappresentato da un centroide e dall'indice del record della tabella. Da notare che il "Prototype"
 * è utile anche in questo caso poichè si ha la necessità di clonare un cluster in un determinato stato.
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see Dataset
 * @see DataMining
 */
public class Cluster implements Cloneable
{
	/** Cluster ha un identificativo **/
	int idCluster;
	/** Centroide rappresentativo (in coordinate xi,yi...nfeaturei)**/
    ArrayList<Double> centroide; 
    /** Indici dei record inseriti**/
	ArrayList<Integer> indiciInseriti; 
	/**
	 * Costruttore utilizato per inizializare le strutture dati del cluster
	 * @param idCluster Identificativo del cluster
	 */
    public Cluster(int idCluster)
    {
    	centroide = new ArrayList<Double>();
    	indiciInseriti = new ArrayList<Integer>();
    	this.idCluster = idCluster;
    }
    /**
     * Setta il centroide
	 * @param centroide centroide
	 */    
    public void setCentroide(ArrayList<Double> centroide)
    {
    	this.centroide = centroide;
    }
    
    /**
     * Aggiungi gli indici dei record inseriti nel cluster
	 * @param i indice cluster
	 */
    public void addIndiciInseriti(int i)
    {
    	indiciInseriti.add(i);
    	
    }
    /**
     * azzera centroide
	 */
    public void clearCluster()
    {
    	centroide.clear();
    }
    /**
     * Ottieni il centroide
	 * @return centroide
	 */
    public ArrayList<Double> getCentroide()
    {
    	return centroide;
    }
    /**
     * Ottieni gli indici inseriti
	 * @return indiciInseriti
	 */
    public ArrayList<Integer> getIndiciInseriti()
    {
    	return indiciInseriti;
    }
    
    /**
     * Calcola la distanza tra questo cluster e un altro passato come parametro effettuando la distanza sempre
     * euclidea
	 * @param cluster Cluster su cui valutare la dista za
	 * @return distanza dal cluster
	 */ 
	public double calcolaDistanzaCentroide(Cluster cluster) 
	{
		double sum=0;
		int i;
		double x1,x2;
		
		/* Per il numero di elementi composti dal centroide (x, y...)*/
		for(i=0; i<this.centroide.size(); i++)
		{	
			// Eccettua la differenza tra le componenti tra i due centroidi
			x1 = cluster.centroide.get(i);
			x2 = this.centroide.get(i);
			sum+= Math.pow(x1-x2, 2); //Quadrato dei punti in una sommatoria
		}
		/* Ritorna la distanza euclidea*/
		return Math.sqrt(sum);
	}
	
    /**
     * CLona il cluster in un determinato stato
	 * @return cloned Cluster clonato
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object clone()
	{
		Cluster cloned = new Cluster(this.idCluster);
		cloned.centroide = (ArrayList<Double>) this.centroide.clone();
		return cloned;
		
	}
    /**
     * Restituisci l'id
	 * @return idCluster
	 */
	public int getId() {
		return this.idCluster;
	}
    /**
     * Stampa il centroide
	 */
	public void stampaCentroide() 
	{
		for(int i=0; i< this.centroide.size(); i++)
			System.out.println(this.centroide.get(i));
		// TODO Auto-generated method stub
	}
	
}


