package it.giuseppeaccardo.datamining.model;
import java.util.ArrayList;
import it.giuseppeaccardo.datamining.model.ricordo.*;
import it.giuseppeaccardo.datamining.exception.FeatureInsufficientiException;

/**
 * <h1>Data Mining!</h1>
 * Il data mining è l'insieme di tecniche e metodologie che hanno per oggetto l'estrazione di un sapere o di una conoscenza a 
 * partire da grandi quantità di dati (attraverso metodi automatici o semi-automatici) e l'utilizzo scientifico, industriale o 
 * operativo di questo sapere.Le fasi principali del sistema sviluppate sono: selezione delle caratteristiche, 
 * clustering e visualizzazione.
 * <br>
 * La selezione delle caratteristiche (features) prevede quali e quante features vogliamo analizzare del dataset.<br>
 * Nella fase di clustering viene usato un algoritmo per “agglomerare” dati simile, ossia il K-Means che prenderà in input il
 * numero di cluster su cui operare.<br>
 * La visualizzazione di tale operato è eseguita dalle classi presenti nel package "Visualizzazione" anche se tale classe può
 * effettuare una visualizzazione semplice da console.
 * <br>
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see Dataset
 * @see Cluster
 * @see Memento
 */
public class DataMining 
{
	/** Dataset su cui fa riferimento il datamining (con le corrispondenti feature selezionate)**/
	private Dataset dataset;
	/** Piu Cluster contenente i relativi punti **/
	Cluster clusters[];
	
	/** 
	 * Ottieni le features usate
	 * @param dataset Dataset iniziale a cui associare il datamining
	 * @return DataMining 
	*/
	public DataMining(Dataset dataset)
	{
		this.dataset = dataset;
	}
	/* Get & set*/
	/** 
	 * Ottieni i clusters
	 * @return Clusters[] 
	*/
	public Cluster[] getClusters()
	{return clusters;}
	
	/** 
	 * Ottieni dataset selezionato
	 * @return Dataset dataset selezionato
	*/
	public Dataset getDatasetSelected()
	{
		return this.dataset;
	}
	/** 
	 * setta un dataset selezionato
	 * @param dataset Dataset selezionato
	*/
	public void setDatasetSelected(Dataset dataset)
	{
		this.dataset = dataset;
	}
	/**
	 *  Tale metodo si occupa di clonare e di selezionare solo le feature richieste nel dataset.
	 *  Dopo che sarà stato selezionato il dataset, diventerà il dataset che apparterrà al datamining corrente. La clonazione
	 *  è necessaria, altrimenti le modifiche del dataset si ripercuoterebbero verso il dataset di partenza. 
	 *  Questo perchè datamining come costruttore accetta il reference al dataset. Infatti se volessimo effettuare piu
	 *  datamining di diversi dataset selezionati da uno stesso dataset, dataset di partenza non dovrà essere immutato.
	 *  @param featureSelezionate Feature da voler selezionare nel dataset
	 *  @throws FeatureInsufficientiException
	 *  @exception FeatureInsufficientiException Le feature da selezionare devono essere almeno 2.
	 *  */
	public void selezionaFeature(ArrayList<Integer> featureSelezionate) throws FeatureInsufficientiException
	{	
	    /* A partire dal dataset principale, crea un dataset con solo le caratteristiche selezionate
		 Solo se le feature selezionate sono maggiori di due, altrimenti lancia un eccezione */
		if(featureSelezionate.size()<2)
		{   /* Caso in cui bloccare il flusso di istruzioni */
			throw new FeatureInsufficientiException("Le features da selezionare devono essere almeno 2!", featureSelezionate.size());
		}
		/* Clona il DATASET di PARTENZA che dovrà essere 'immune' alla selezione */
		Dataset datasetSel = (Dataset) dataset.clone();
		/* Setta il nuovo dataset clonato e di questo effettua la selezione delle feature richieste */
		this.setDatasetSelected(datasetSel);
		this.getDatasetSelected().selezionaFeature(featureSelezionate);
		//Stampa dataset selezionato su console
		dataset.stampaDataset();	
	}
	/**
	 *  Metodo che si occupa di effettuare il K-means,  un algoritmo di clustering partizionale che permette di suddividere un 
	 *  insieme di oggetti in K gruppi sulla base dei loro attributi.<br>
	 *  L'algoritmo prevede diverse fasi: Assegnazione di centroidi random per ogni cluster e iterativamente assegnare ogni punto
	 *  ad un suo corrispondente cluster, calcolato in base alla minima distanza media euclidica  rispetto ad ogni centroidi del cluster.
	 *  A questo punto si ricalcolano dei nuovi centroidi per ogni cluster e se ne verifica la convergenza del risultato dell'algoritmo.
	 *  @param k Gruppi o numero di Cluster su cui voglia effettuare l'algoritmo
	 *  @throws ArithmeticException eccezione lanciata nel caso in cui l'algoritmo non converga
	 *  */
	public void Kmeans(int k) throws ArithmeticException
	{
		//Crea k gruppi di cluster
		clusters = new Cluster[k];
   		//Assegna K centroidi (xi,yi...ni) rappresentativi dei loro corrispondenti cluster
	 	assegnaCentroidiClusterRandom(k);
	 	// Processo di assegnazione iterativo
	 	calcolaKmeans(k);
	}
	/**
	 *  Tale metodo ha lo scopo di calcolare K Centroidi Random assegnati ai loro corrispondenti cluster.
	 *  Da notare che la <b>convergenza</b> dipende proprio dalla scelta dei centroidi iniziali perchè l'algoritmo
	 *  diverge se i centroidi scelti sono molto vicini tra loro. 
	 *  @param k Gruppi o numero di Cluster su cui voglia effettuare l'algoritmo
	 *  */
	public void  assegnaCentroidiClusterRandom(int k)
	{	
		int indiceRandom;
	 	 //Genero feature selezionate
	 	for(int i=0;i<k; i++)
	 	{
	 		//Assegna un centroide random al k-esimo cluster
		 	indiceRandom = ((int)(Math.random() * dataset.getNumRecord()));//
		 	//Aggiungi il cluster e il suo centroide in indice
		 	clusters[i] = new Cluster(i);
		 	clusters[i].setCentroide( dataset.getRecord(indiceRandom));
	 	}
	 	return;
	}
	

	
	/**
	 *  Fase o processo iterativo dell'algoritmo, in cui si assegnano i centroidi ai corrispondenti cluster utilizzando
	 *  la MDM (minima distanza media) rispetto al centroide. Si ricalcoleranno i centroidi nuovi, sulla media dei nuovi punti
	 *  inseriti, e infine si verifica la convergenza dell'algoritmo, cioè se i cluster posseggono sempre gli stessi punti. <br><br>
	 *  Da notare l'utilizzo del pattern "Memento" per salvare lo stato precedente dei cluster per poterli confrontare successivamente
	 *  con lo stato successivo.
	 *  @param k Gruppi o numero di Cluster su cui voglia effettuare l'algoritmo
	 *  @throws ArithmeticException eccezione lanciata nel caso in cui l'algoritmo non converga
	 *  */
	public void calcolaKmeans(int k) throws ArithmeticException 
	{
		
		//Crea il custode del memento
		Caretaker caretaker = new Caretaker();
		//Crea l'originatore stato   
		Originator originator = new Originator();
		
		/* Variabili utilizzate per la convergenza */
     	boolean finish = false;
		int iteration = 0;
		
		/* Fin quando non converge, ripeti il procedimento*/
		while(!finish) 
		{
			//Pulisci punti inseriti in quei cluster (se centroide e' invariato, i punti che inseriremo saranno lo stesso)		
			for(int i=0; i<k; i++)
				clusters[i].indiciInseriti.clear();
			
			//Origina stato e custodiscilo nel memento
			originator.set(clusters);
			caretaker.addMemento(originator.saveToMemento() );
		
			/*Assegna ai cluster i record piu' "vicini" attraverso la distanza euclidica (assegno sempre indici) rispetto ai centroidi*/
			assignCluster(k);
		    
		    /* Calcola nuovi centroidi come la media dei valori contenuti nei cluster nuovi*/
			calculateCentroids();
		
			//Incrementa passo
			iteration++;				        	       	
			System.out.println("Iterazione : " + iteration);
			
			//Aggiorna lo stato originator e restituisce lo stato precedente    
			Cluster[] oldClusters = originator.restoreFromMemento( caretaker.getMemento());
			/* verifica la convergenza confrontando i cambiamenti nei cluster */
			if(staConvergendo(oldClusters, k) == 0)
				finish=true;
		}	
	}
	
	/**
	 *  verifica la convergenza calcolando la distanza tra i cluster calcolati precedentemente e i cluster attuali,
	 *  cioè verifica se i cluster contengono gli stessi punti.
	 *  @param oldClusters Stato dei cluster precedenti
	 *  @param k Gruppi o numero di Cluster su cui voglia effettuare l'algoritmo
	 *  @return Distanza o "vicinanza" tra i due stati di clusters
	 *  @throws ArithmeticException eccezione lanciata nel caso in cui l'algoritmo non converga
	 *  */
	private double staConvergendo(Cluster[] oldClusters, int k) throws ArithmeticException
	{
		int i=0;
		double dist=0;
		/* Distanza tra i punti dei corrispondenti cluster */
		for(i=0; i<k; i++)
			dist+=clusters[i].calcolaDistanzaCentroide(oldClusters[i]);
		/* Se dist presenta un valore indefinibile, significa che l'algoritmo sta divergendo  e lancia l'eccezione*/
		if (Double.isInfinite(dist) || Double.isNaN(dist))
	        throw new ArithmeticException("Distanza tra cluster: " + dist);
		
		return dist;
	}
	
	/**
	 *  Calcola i nuovi valori dei centroidi per ogni cluster effettuando la media aritmetica sul numero di valori
	 *  contenuti. Questo metodo utilizza una programmazione MultiThreading poichè è la parte piu onerosa a livello 
	 *  computazionale. <br>Ogni thread si occupera' di calcolare il centroide del suo corrispondente cluster,
	 *  in modo tale da sfruttare la concorrenza e ridurre il tempo di lavoro effettivo.
	 *  @see CalcolatoreCentroideThread
	 *  @exception InterruptedException thread interrotto improvvisamente o non si riesce a stopparlo
	 *  */
	public void calculateCentroids()
	{		   
		// Setto la variabile condivisa in lettura tra i thread
		CalcolatoreCentroideThread.setDataset(dataset); 
		// Crea una lista di Threads
		ArrayList<Thread> threads = new ArrayList<Thread>();
		
		//Per ogni cluster
    	for(Cluster cluster : clusters) 
    	{
    		/* Un thread calcolerà il centroide del cluster assegnato.
    		 * Si crea un 'Runnable' che definisce l'operazione da far effetture al thread. 
    		 * In questo caso si associa all'algoritmo Run() il calcolo del centroide passando come costruttore di 
    		 * CalcolatoreCentroideThread il cluster su cui opererà quel thread.
    		 * */
    		// Crea Operazione da far eseguire, definendone il cluster su cui operare
			Runnable  calcolaCentroide = new CalcolatoreCentroideThread(cluster);
			// Crea thread che effettuera quell'operazione, definita in Run, che sarà fatta eseguire da t.start()
			Thread t = new Thread(calcolaCentroide);
			t.start();
			threads.add(t);
    	}
    	
    	/* Il Thread Principale si metterà in attesa di raccogliere i singoli thread lanciati che hanno terminato
    	 * le loro operazioni*/
		for (Thread thread : threads) {
		    try {
		        thread.join();
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		}
		
	}

	/**
	 * 	Assegna i punti, localizzati come indici all'interno della tabella del dataset, ai suoi corrispondenti cluster,
	 *  valutando l'MDM, ossia la distanza media minima rispetto ai centroidi.
  	 *  @param k Gruppi o numero di Cluster su cui voglia effettuare l'algoritmo
	 *  @see Dataset
	 *  */
	public void assignCluster(int k)
	{
		/* Indice del cluster "piu vicino" a quel record */
		int clusterMin;
		/* Variabili utili per il calcolo del minimo*/
		double dist, min;
		
		// Record da assegnare
		for(int i = 0; i< dataset.getNumRecord(); i++)
		{
			/* Cerco cluster con distanza minima */
		    min = dataset.Distanza(i, clusters[0].centroide); // Distanza tra il record e il centroide
			clusterMin = 0;
			
			for(int j=1; j<k; j++)
			{// Distanza tra il record-i e il centroide (calcolato run time)
				dist = dataset.Distanza(i, clusters[j].centroide); 
				if(dist<min)
					clusterMin = j;
			}
			// Inserisci l'indice-i del corrispondente elemento in quel cluster Min.
			clusters[clusterMin].addIndiciInseriti(i);
		}		
	}
	
	/**
	 * 	Effettua una semplice stampa su Console. Il sistema non userà questa, poichè è giusto per 
	 *  l'osservazione del software e dei risultati in una maniera piu basso livello.
	 *  @see grafico2D
	 *  @see grafico3D
	 *  @see PCAcolt
	 *  */
	public void stampaClustering()
	{
		// APPLICA PCA PER RIDURRE IN UNA MATRICE almeno 3D
		if(dataset.getFeatureUsate().size()>3)
		{
			System.out.println(" APPLICA PCA per la RIDUZIONE della MATRICE");
			double matriceRidotta[][];
			matriceRidotta = PCAcolt.pcaReduxMatrix(dataset.toMatrix());
			for(Cluster cluster:clusters)
			{
				System.out.println("                         CLUSTER: "+cluster.getId());

				for(int i=0; i<cluster.indiciInseriti.size(); i++)
				{
					System.out.println("#Record:"+ (cluster.getIndiciInseriti().get(i)+1));
					for(int j=0;j< 3; j++)
						System.out.println("Valore"+j+": "+ matriceRidotta[ cluster.getIndiciInseriti().get(i)][j]);
				}
			}
		}
		else
		{
			// NON APPLICO PCA PERCHE' FEATURE = 2
			for(Cluster cluster:clusters)
			{
				System.out.println("                         CLUSTER: "+cluster.getId());
				
				System.out.println("                         CENTROIDE");
				cluster.stampaCentroide();
				
				for(int i=0; i<cluster.indiciInseriti.size(); i++)
				{
					System.out.println("Indice:"+ (cluster.getIndiciInseriti().get(i)+1));
					for(int j=0;j< dataset.getFeatureUsate().size(); j++)
						System.out.println("Valore: "+dataset.getCella(cluster.getIndiciInseriti().get(i), j));
				}
			}
		}
	}
}