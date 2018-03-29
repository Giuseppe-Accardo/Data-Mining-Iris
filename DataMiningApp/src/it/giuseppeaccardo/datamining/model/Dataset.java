package it.giuseppeaccardo.datamining.model;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <h1>Dataset!</h1>
 * Classe astratta (CaricaDataset è un metodo astratto) utilizzata fondamentalmente per realizzare uno 'Scheletro' iniziale di un dataset. Il dataset
 * costituisce un insieme di dati strutturati in forma relazionale, cioè corrisponde al contenuto di una singola tabella
 * sottoforma di una matrice, dove le righe rappresentano i 'record' e le colonne le 'variabili' dette 'features'.
 * Tale classe è posta in tal modo poichè si vuole rinviare la responsabilità di "come caricare un dataset reale" alla
 * classe che estende dataset, utilizzando la stessa struttura di dataset.
 * <br><br>
 * Il principio seguito è quello del design pattern "Template Method" che definisce lo scheletro di un algoritmo in un’operazione,
 * rinviando alcuni passi alle sottoclassi client, cioè consente alle sottoclassi di ridefinire alcuni passi senza cambiare la 
 * struttura dell’algoritmo. Altri design pattern che potevano essere realizzati erano il "Factory Method" o "Decorator".
 * <br><br>
 * Da notare che tale classe utilizza anche il design pattern "Prototype", mediante l'implementazione dell'interfaccia di 'Cloneable'.
 * Questo perchè quando si selezionano le features di un dataset, si ha la necessita di creare nuovi oggetti copiando il dataset
 * non selezionato di partenza in tal modo da non perdere lo stato iniziale. Copiato il dataset, si selezioneranno
 * solo le features richieste.
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see DatasetIris
 * @see Cluster
 */
public abstract class Dataset  implements Cloneable 
{
	/** Contenitore delle feature usate che sono associate a corrispondenti indici**/
	private ArrayList<Integer> featureUsate;
	/** Nomi delle features **/
	private String[] features;
	/** Nome dataset **/
	private String nomeDataset;
	/** Matrice contenente il dataset di valori **/
	protected ArrayList<ArrayList<Double>> mat; 	
 	
    /**
    * Classe astratta che ogni dataset è obblicato ad implementare poichè ogni dataset presenta un suo
    * tipo di caricamento
    * @param path rappresenta la locazione da cui caricare il dataset (nome di un file o indirizzo di rete)
    */
 	protected abstract void CaricaDataset(String path) throws IOException; 

 	/**
     * Costruttore utilizzato per inizializzare il dataset.
     * @param nomeDataset Nome da assegnare al dataset 
     * @param features Valore di tipo varargs che contiene la lista di features appartenenti
     * @return Dataset 
     */
	public Dataset(String nomeDataset, String... features) 
	{		
		setNomeDataset(nomeDataset);
		setFeatures(features);
		inizializzaTabella();
	}
	
 	/**
     * Ridefinizione di clone che effettua la clonazione di un'oggetto creandone uno con lo stesso stato. La 
     * clonazione definita è di tipo "Deep Copy" perchè clone di default effettua una "Shallow Copy", ossia
     * una copia del reference. Tale è l'applicazione reale del "Prototype".
     * @return Dataset Copia di un dataset esistente mediante 'Deep Copy'.
     * @exception CloneNotSupportedException Oggetto non clonabile
     * @link http://javaconceptoftheday.com/difference-between-shallow-copy-vs-deep-copy-in-java/
     */
	@SuppressWarnings("unchecked")
	@Override
	public Dataset clone(){
		try{
			/* Shallow copy --> Copia tutto ciò che resterà immutato ->reference
			   Deep copy --> Crea le istanze che cambieranno->Valore */
			/* Crea un clone del dataset. Da notare che si necessita di effettuare anche un clone di mat che è una collection e 
			 * ciò lo renderà "Deep", necessario per la seleziona delle features senza modificare il dataset iniziale  */
			Dataset datasetClone = ((Dataset) super.clone());
			datasetClone.setTabella( ((ArrayList<ArrayList<Double>>) datasetClone.getTabella().clone()) );
			return datasetClone;
		}
		catch(CloneNotSupportedException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null; 
		}
	}
	/* --------------------------------------------------------------------------- */
 	/**
     * Inizializza i valori degli attributi. La tabella iniziala userà tutte le features.
    */	
	public void inizializzaTabella()
	{
	   mat = new ArrayList<ArrayList<Double>>();
	   featureUsate = new ArrayList<Integer>();
	   
	   for(int i=0; i< this.getFeatures().length; i++)
	   {
		   mat.add(new ArrayList<Double>());
		   featureUsate.add(i); // uso tutte le feature
	   }
	}
	
 	/**
     * Seleziona le feature del dataset. Tale verrà eseguito dal dataset clonato poichè sarà usatp dal datamining 
     * con le sue corrispondenti features selezionate.
     * @param featureSelezionate Feature selezionate da client
    */	
	public void selezionaFeature(ArrayList<Integer> featureSelezionate)
	{
		/* Crea una matrice temporanea utilizzata per selezionare le features. Mat
		 * dovrà essere vuota perchè le colonne saranno inserite a partire da tmpMat che sara
		 * deallocato dal Garbage della JVM*/
		ArrayList<ArrayList<Double>> tmpMat = new ArrayList<ArrayList<Double>>(mat);
		mat.clear();
		this.featureUsate = featureSelezionate;
		/* Inserisci le colonne a mat */
		for(int i=0; i<featureSelezionate.size(); i++)
			setColonna( tmpMat.get( featureSelezionate.get(i)));
	}
	
	/** 
	 * Stampa il dataset nella console**/
	public void stampaDataset()
	{
		for(int i=0; i< this.getNumRecord(); i++)
		{
			for(int j=0; j < this.getFeatureUsate().size(); j++)
			{
				System.out.print( mat.get(j).get(i)+ " ");
			}
			System.out.println("\n");
		}
	}
	/**
     * Ottiene la distanza (euclidea) tra un centroide (x,y...) e un record di indice i.
     * @param i indice del dataset verso cui calcolare la distanza rispetto il centroide
     * @param centroide punto centroide da cui calcolare la distanza
     * @return double Distanza euclidea tra un centroide e un record i 
     */
 	public double Distanza(int i, ArrayList<Double> centroide)
 	{
 		double Sum=0;
 	     /* Da notare che essendo arraylist di arraylist, gli indici sono inversi ad una matrice.
 	      * Il primo get di mat indica la colonna e il secondo è il record. Inoltre, di un punto si necessitano
 	      * scorrere tutte le sue features (x,y...)*/
 		for(int j=0; j<this.getFeatureUsate().size(); j++)
 			Sum+=Math.pow((centroide.get(j) - mat.get(j).get(i)), 2);
 		return Math.sqrt(Sum);
 	}
	/* Getter e Setter*/	
 	/**
     * Setta la tebella
     * @param tebella Tabella da settare
     */
	private void setTabella(ArrayList<ArrayList<Double>> tabella) {
		this.mat = tabella;
	}
	/**
     * Ottieni nome dataset
     * @return String Nome del dataset
     */
	public String getNomeDataset()
	{return this.nomeDataset;}
 	/**
     * Setta nome dataset
     * @param nomeDataset nome da settare
     */	
	private void setNomeDataset(String nomeDataset)
	{ this.nomeDataset = nomeDataset;}
 	/**
     * Setta il nome delle features
     * @param features[] features da inserire
     */		
	private void setFeatures(String[] features) {
		this.features = features;
	}
 	/**
     * Ottieni le features
     * @return features
     */	
	public String[] getFeatures() {
		return features;
	}
 	/**
     * Ottieni numero dei record
     * @return numero dei record
     */	
	public int getNumRecord() {
		return this.getTabella().get(0).size();
	}
	
 	/**
     * Ottieni il valore dei record
     * @param i indice record
     * @param j indice colonna
     * @return Valore cella
    */	
 	public double getCella(int i, int j)
 	{
 		/* Colonna j e riga i*/
 		return mat.get(j).get(i);
 	}

	/** 
	 * Ottieni un record di indice random
	 * @param indiceRandom indice del record da restituire
	 * @return record restituisce un record di un determinato indice**/
	public ArrayList<Double> getRecord(int indiceRandom)
	{
		ArrayList<Double> record = new ArrayList<Double>();
		for(int i=0; i<this.getFeatureUsate().size(); i++)
			record.add( mat.get(i).get(indiceRandom));
		
		return record;
	}
	/** 
	 * Setta la colonna del dataset. Tale sara utilizzata per inserire le colonne
	 * da selezionare.
	 * @param colonnaValori colonna da inserire
	*/
	public void setColonna( ArrayList<Double> colonnaValori)
	{
		this.mat.add(colonnaValori);
	}
	/** 
	 * Ottieni la tabella
	 * @return tabella
	*/
	public ArrayList<ArrayList<Double>> getTabella()
	{
		return this.mat;
	}
	/** 
	 * Aggiunge l'elemento in una colonna
	 * @param colonna colonna 
	 * @param valore valore
	*/
	public void setElemento(int colonna, Double valore) 
	{
		mat.get(colonna).add(valore);
	}
	
	/** 
	 * Restituisce la tabella sottoforma di matrice. Tale è utile per il calcolo di PCA.
	 * @return matrix Tabella in forma matrice
	*/
	public double[][] toMatrix()
	{
	    double[][] matrix = new double[this.getNumRecord()][this.getFeatureUsate().size()];
		for(int i=0; i< getNumRecord(); i++)
		{
			for(int j=0; j < getFeatureUsate().size(); j++)
			{
				matrix[i][j] = mat.get(j).get(i);
			}
		}
		return matrix;
	}
	/** 
	 * Ottieni le features usate
	 * @return featureUsate 
	*/
	public ArrayList<Integer> getFeatureUsate()
	{return this.featureUsate;}
}
