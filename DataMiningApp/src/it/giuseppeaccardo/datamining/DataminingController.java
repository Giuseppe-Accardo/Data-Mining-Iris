package it.giuseppeaccardo.datamining;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.giuseppeaccardo.datamining.exception.AlgoritmoDivergenteException;
import it.giuseppeaccardo.datamining.exception.FeatureInsufficientiException;
import it.giuseppeaccardo.datamining.exception.FileMioIOException;
import it.giuseppeaccardo.datamining.exception.FileNonTrovatoException;
import it.giuseppeaccardo.datamining.exception.FileNumeriFormatoException;
import it.giuseppeaccardo.datamining.model.DataMining;
import it.giuseppeaccardo.datamining.model.DatasetIris;
import it.giuseppeaccardo.datamining.visualizzazione.IvisualizzaGraficoStrategy;
import it.giuseppeaccardo.datamining.visualizzazione.grafico2D;
import it.giuseppeaccardo.datamining.visualizzazione.grafico3D;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.*;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
/**
 * <h1>Data Mining Controller!</h1>
 * L'applicazione utilizza il design pattern di tipo architetturale 'MVC' che permette di separare i componenti software
 * come la logica di business, di funzionamento e di presentazione.<br>
 * In questa applicazione il Model è dato dalle classi 'Dataset' e 'Datamining' poichè forniscono l'entry per accedere ai dati e
 * di memorizzarne uno stato in base alle richieste del client. <br>
 * Il controller (questa classe) ha il compito di ricevere i comandi dall'utente (mediante view) per mapparne tutti
 * gli aggiornamenti verso il model. Effettuate tale modifiche, il controller selezionerà la view su cui voler mostrare
 * i risultati.  <br>
 * La view si occupa di interpretare il model a livello di front end (interfaccia utente). 
 * Da esso possono partire delle richieste e risposte verso il
 * controller, ma può richiedere anche semplicemente lo stato direttamente al model o ricevere da esso una notifica di cambiamento di 
 * stato. Questo legame o Binding è effettuato mediante il design pattern "Observer"  per definire un forte dipendenza tra view e model
 * in modo tale che se una cambia, l'altra è aggiornata automaticamente. In JavaFX tale funzionalità è implementata direttamente col 
 * la collection 'ObservableList' che appunto tiene traccia di tutti i dati e dei suoi aggiornamenti.<br><br>
 * La scelta di costruire l'applicazione solo con JavaFX è perchè presenta uan serie di vantaggi (costruzioni di grafici, facile
 * comunicazione con altri software ed ecc..), ma soprattutto perchè è già predisposto per la costruzioni di applicazioni
 * seguendo il Design Pattern 'MVC'. Infatti, come detto prima, fornisce ggià strutture che facilitano il compito di determinate operazioni,
 * come l'aggiornamento dei dati che è diretta mediante l'ObservableList che autoimplementa l'Observer pattern. Da ricordare che
 * Sun sostiene che il 90% delle applicaziono sono costruite mediante JavaFX che sta sostituendo tutta una serie di librerie obsolete
 * (awt,swing...).<br><br>
 * Da notare che il controller interagisce con alcuni oggetti del layout che si presentano in un formato FXML. Infatti l'annotazione
 * è necessaria per far capire alla JVM che sono oggetti riferiti al file Datamining.fxml che, appunto, definisce il layout iniziale.
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see DatasetIris
 * @see Dataset
 * @see DataMining
 */
public class DataminingController {
	/** Lo stage rappresenta il contenitore principale delle scene. Solitamente è la finestra che contiene una o più scene.
	 * myStage indica lo stage principale del sistema **/
	private Stage myStage;

	/** Il model su cui funge il controllo e tutto il sistema (dove saranno conservati i dati del funzionamento) **/
	private DataMining model;
	private DatasetIris dataset;
	
	/** Serie di oggetti in FXML necessari per l'interazione con la view. Da notare che la notazione è necessaria
	 * per effettuare il binding del corrispontente oggetto nel layout.xml (ad esempio, un bottone creato nel file fxml,
	 * è accessibile dichiarandone una corrispondende variabile che sarà associata al file.xml grazie all'annotazione).**/
    @FXML // fx:id="sfogliaButton"
    private Button sfogliaButton; // Value injected by FXMLLoader
    @FXML // fx:id="textSfoglia"
    private TextField textSfoglia; // Value injected by FXMLLoader
    @FXML // fx:id="caricaButton"
    private Button caricaButton; // Value injected by FXMLLoader  
    @FXML // fx:id="kmeansButton"
    private Button kmeansButton; // Value injected by FXMLLoader
    @FXML // fx:id="graficoButton"
    private Button graficoButton; // Value injected by FXMLLoader

	/** Varie checkbox da selezionare per le features**/
    @FXML // fx:id="lenPetalo"
    private CheckBox lenPetalo; // Value injected by FXMLLoader
    @FXML // fx:id="lenSepalo"
    private CheckBox lenSepalo; // Value injected by FXMLLoader
    @FXML // fx:id="larPetalo"
    private CheckBox larPetalo; // Value injected by FXMLLoader
    @FXML // fx:id="larSepalo"
    private CheckBox larSepalo; // Value injected by FXMLLoader
    private CheckBox[] boxes; 
    
    /** Combo box per la scelta dei cluster**/
    @FXML // fx:id="sceltaCluster"
    private ComboBox<Integer> sceltaCluster; // Value injected by FXMLLoader
    
    /** Tabella su cui visualizzare il dataset e su cui si avranno le selezioni **/
    @FXML // fx:id="tableDataset"
    private TableView<ArrayList<Double>> tableDataset; // Value injected by FXMLLoader
    @FXML // fx:id="lenSep"
    private TableColumn<ArrayList<Double>, Double> lenSep; // Value injected by FXMLLoader
    @FXML // fx:id="lenPet"
    private TableColumn<ArrayList<Double>, Double> lenPet; // Value injected by FXMLLoader
    @FXML // fx:id="larSep"
    private TableColumn<ArrayList<Double>, Double> larSep; // Value injected by FXMLLoader
    @FXML // fx:id="larPet"
    private TableColumn<ArrayList<Double>, Double> larPet; // Value injected by FXMLLoader
    
    /**
     * Registra evento sul bottone "graficoButton" che si occupa, appunto, di effettuare la la visualizzazione
     * del dataset. In questo caso è stato applicato il design pattern "Strategy" poichè si rende la visualizzazione 2D e 3D
     * intercambiabile, senza cambiare metodo.
     * @param event Evento sul bottone 'graficoButton'**/
    @FXML
    private void doVisualizzaGrafico(ActionEvent event) {
    	/* Strategy necessitaa di un'interfaccia*/
    	IvisualizzaGraficoStrategy grafico;
    	/* Il grafico sarà su uno stage su cui sarà presente la scena del grafico*/
    	Scene sceneGrafico;
    	Stage stageGrafico;
    	//dataset.get
    	System.out.println(model.getDatasetSelected().getFeatureUsate().get(0));
    	/* Istanziamo il grafico 2D o 3D, in base alle features utilizzate */
    	if(model.getDatasetSelected().getFeatureUsate().size()<3)
    	{
    		grafico = new grafico2D();
    	}
    	else
    	{
    		grafico = new grafico3D();
    	}
    	/* Utilizzando questo metodo, otteniamo direttamente la scena corrispondente, senza che sia il programmatore
    	 * a doverlo riprogrammare */
    	sceneGrafico = grafico.visualizzaGrafico(model);
    	/* Istanzia stage su cui visualizzare grafico*/    	
        stageGrafico = new Stage();
        stageGrafico.setScene(sceneGrafico);
        stageGrafico.setTitle("Grafico Datamining");
        stageGrafico.setResizable(false);
        stageGrafico.show();
    }
    
    /**
     * Registra evento sul bottone "kmeansButton" che si occupa, appunto, di effettuare la clusterizzazione
     * del dataset. Il metodo conserva le feature selezionate che saranno utilizzate per creare il nuovo dataset su cui
     * effettueremo datamining. Se le features sono minori di due o l'algoritmo diverge, il metodo catturerà delle eccezioni.
     * @param event Evento sul bottone 'kmeansbutton'
     * @exception FeatureInsufficientiException per verificare la correttezza del numero di features selezionate
     * @exception AlgoritmoDivergenteException Controllo per verificare la giusta convergenza dell'algoritmo.
     * **/
    @FXML
    void doApplicaKm(ActionEvent event) 
    {
    	System.out.println("FEATURE SELEZZIONATE"+dataset.getFeatureUsate().size());
    	try{
    		/* Crea un datamining */
	    	model = new DataMining(dataset);
			ArrayList<Integer> featureSelezionate = new ArrayList<Integer>();
	    	
	    	/* Conserva le features selezionate nelle checkbox*/
			for (int i=0; i<dataset.getFeatures().length; i++) 
				if(boxes[i].isSelected())
					featureSelezionate.add(i);
			/* Seleziona le feature dal datamining */
			model.selezionaFeature(featureSelezionate);
			/* Effettua kmeans passando in input il numero di k gruppi */
			model.Kmeans(sceltaCluster.getValue());
			/* Effettua una stampa su console */
			model.stampaClustering();
			/* Conferma l'avvenuto successo del calcolo del k means*/
			confermaSuccesso();
    	}
    	catch(FeatureInsufficientiException e){/* Cattura l'eccezione delle feature insufficienti*/
    		e.handleException();
    	}
    	catch(ArithmeticException e){ /* Eccezione lanciata dal kmeans e catturata qui nel caso in cui l'algoritmo diverga*/
    		try{/* Rilancia eccezione personalizzata che sarà catturata subito dopo */
    			throw new AlgoritmoDivergenteException("l'algoritmo stava divergendo!\n"+e.getMessage());
    		}
    		catch(AlgoritmoDivergenteException ex) { /* Gestisci eccezione*/
    			ex.handleException();
    		}
    	}
    }
    /**
     * Crea alert per confermare il successo del kmeans
     * */
    private void confermaSuccesso() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("SUCCESSO DEL CALCOLO!");
		alert.setHeaderText("Il calcolo del K-Means è avvenuto con successo!");
		alert.setContentText("Conferma successo e clicca su 'Visualizza il grafico' per visualizzare il risultato.");
		alert.showAndWait();
		
	}

	/**
     * Registra evento sul bottone "sfogliaButton" che si occupa, appunto, di sfogliare le directory e selezionare
     * il file '.data'. Il metodo effettua ciò grazie alla classe 'FileChooser' che server per aprire una finestra di dialogo
     * e restituire il file selezionato. A questo punto il path del file sarà salvato e verrà abilitato il bottone per caricare
     * @param event Evento sul bottone 'sfogliaButton'
     * **/
	@FXML
    void doSfogliaButtonPress(ActionEvent event) 
    {	
    	 /* Crea un oggetto per la scelta del file da Sfoglia aggiungendono l'estensione accettata 
    	  * e definendo una directory di partenza (file di default progetto) */
    	 FileChooser fileChooser = new FileChooser();
         FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DATA files (*.data)", "*.data");
         fileChooser.getExtensionFilters().add(extFilter);
         fileChooser.setInitialDirectory(new File("resources/data/")); 
         
         /* Apri finestra dialogo e se seleziono un file, inserisci il path sul text e attiva il tasto carica */
         File file = fileChooser.showOpenDialog(myStage.getScene().getWindow());
         if(file != null)
         {
        	 textSfoglia.setText(file.getPath());
        	 caricaButton.setDisable(false);
         }
    }
    /**
     * Registra evento sul bottone "caricaButton" che si occupa, appunto, di caricare
     * il file 'iris.data'. Il metodo effettua ciò andando ad istanziare un oggetto di tipo "DatasetIris" con il reference
     * "Dataset". Il path è praso da sfoglia, però potrebbe lanciare delle eccezioni la lettura da file. 
     * @param event Evento sul bottone 'sfogliaButton'
     * @exception FileNonTrovatoException Caso in cui il file non viene trovato ed è lanciata alla cattura di 'FileNotFoundException'
     * @exception FileNumeriFormatoException caso in cui il file non è formattato con valori numeri ed è lanciata alla cattura di 'NumberFormatException'
     * @exception FileMioIOException Caso puramente generico ed è avviato quando cattura un eccezione che non è una delle sopra elencate
     * **/
    @FXML
    void doCaricaDatasetButton(ActionEvent event)
    {
    		// Carica dataset
        	try {
        	/* Prova a caricare il dataset */
			dataset =  new DatasetIris( textSfoglia.getText() );
			System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
	    	dataset.stampaDataset(); //Stampa su console
	    	/* Abilita i bottoni disabilitati*/
	    	visualizzaButton();
        	/* Inizializza degli oggetti della view */
        	inizializzaCheckbox(); 
	    	//visualizza
	    	visualizzaDataset();
	    	caricaCombobox();
		} 
        /* Eccezioni varie già spiegate sopra */
    	catch (FileNotFoundException e) {
			try {
				throw new FileNonTrovatoException("File non trovato o irreperibile!");
			}catch(FileNonTrovatoException ex) {
				ex.handleException();
			 }
    	}
    	catch (NumberFormatException e) {
			try {
				throw new FileNumeriFormatoException("Formato del file sbagliato!");
			}catch(FileNumeriFormatoException ex) {
				ex.handleException();
			 }
    	} /* Questo cattura qualunque IOexception */
    	catch (IOException e) {
			try {
				throw new FileMioIOException("Errore durante l'apertura o lettura file!");
			}catch(FileMioIOException ex) {
				ex.handleException();
			 }
		}
    }
    /**
     * Setta la visibilita e l'abilitazione a determinati oggetti della GUI
     * */
    private void visualizzaButton() {
    	/* Visualizza le checkbox nella scena e i bottoni*/
		myStage.getScene().lookup("#boxSelezionaFeatures").setVisible(true);
		myStage.getScene().lookup("#boxBassoDestra").setDisable(false);
	}

	/**
     * Carica la Combobox contenente il numero di features che è possibile selezionare, necessarie per l'invocazione
     * del kmeans    * **/
    private void caricaCombobox() 
    {
    	// per il k-means posso k cluster tale che 1<K<N_Dataset
    	List<Integer> totCluster = new ArrayList<Integer>();
    	// Teoricamente k<dataset.getNumRecord(), ma già verso k=5 l'algoritmo tende a converge raramente
    	for(int k= 2; k<5; k++)
    		totCluster.add(k);
    	
    	//sceltaCluster = new ComboBox<Integer>();
    	sceltaCluster.getItems().addAll(totCluster);
    	sceltaCluster.getSelectionModel().select(1);
	}
    
    /**
     * Inizializza le checkbox utilizzate per la scelta delle features. Alla selezione di una feature, verrà colorata
     * la colonna selezionata all'interno della TableColumn corrispondente. Per fare ciò si conservarno e si tengono
     * traccia delle colonne selezionate e quelle non selezionate. Da osservare che "table.getColumns()" restituisce un un 
     * 'ObservableList<TableColumn<ArrayList<Double>, ?>>' e quindi la necessità di utilizzare la Wildcard anche
     * per la definizione del reference. Si può notare come l'ObservableList stia utilizzando il pattern Observer, poichè
     * si vuole tenere traccia di quali colonne si evidenzieranno e quali no.
     **/
	private void inizializzaCheckbox() 
    {
		/* Crea insieme di checkbox */
		boxes = new CheckBox[]{lenPetalo, lenSepalo, larPetalo, larSepalo};
		
		/* Crea delle ObservableList per contenere le colonne che sono evidenziate (selezionate) */
    	ObservableList<TableColumn<ArrayList<Double>, ?>> highlightColumns = FXCollections.observableArrayList();
    	/* Crea ObservableList per contenere le colonne della tabella*/
		ObservableList<TableColumn<ArrayList<Double>, ?>> columns = FXCollections.observableArrayList(tableDataset.getColumns());
		/* Per tutte le features (4)*/
		for(int i=0;i<dataset.getFeatures().length; i++)
		{
			/* Ottieni la prima colonna da ObservableList columns */
			TableColumn<ArrayList<Double>, ?> col = columns.get(i);
			
			/* Verifica se tale colonna dovrà essere selezionando controllando la corrispondente checkbox 
			 * se è selezionata.*/
			boxes[i].selectedProperty().addListener(/* Lambda expression con changeListener e changer */
													(obs, wasSelected, isNowSelected) -> //parametri disponibili per il metodo da riscrivere a run time
													{
														/* Se il box è stato selezionato aggiungilo tra le colonne da evidenziare,
														 * altrimenti toglielo da quelle evidenziate. */
											            if (isNowSelected) {
											                highlightColumns.add(col);
											            } else {
											                highlightColumns.remove(col);
											            }
											         });
			/* Evidenzia la colonna graficamente registrando l'evento. Il secondo argomento contiene se deve essere evidenziata */
			highlightColumnWhenNeeded(col, highlightColumns);
		}
	}
    /**
     * Metodo generico utilizzato per effettuare la registrazione dell'evento della effettiva colonna selezionata.
     * Il metodo ricava la cell factory, ossia le proprietà delle celle che compongono la colonna e crea una pseudo-classe utilizzata per
     * associare lo stile alla colonna (colonne gialle) all'occorrenza dell'evento. Alla colonna sarà settata la property da aggiungere
     * alle sue celle, se l'osservazione delle colonne evidenziate cambiano stato e conterranno la suddetta colonna.
     * PS  Da notare il forte uso delle espressioni Lambda per la semplificazione e scrittura delle classi anonime implementate da interfacce fuzionali.
     * @param column Una colonna su cui registrare evento per evidenziarla.
     * @param highlightColumns Osservazioni su cui i listner registrano quando far partire l'azione di evidenziare la colonna. Inoltre serve per
     * verificare se quella colonna è quella da evidenziare, ossia che ha fatto scaturire l'evento.
     **/
    private <S,T> void highlightColumnWhenNeeded(TableColumn<S,T> column, ObservableList<TableColumn<ArrayList<Double>,?>> highlightColumns) {
    	/* Ottieni la property delle celle contenuto nella colonna. 
    	 * Il metodo ritorna una callback, che è un'interfaccia funzionale implementata, utilizzata per specificare gli input del metodo 'call' e il
    	 * tipo di ritorno*/
        Callback<TableColumn<S,T>, TableCell<S,T>> currentCellFactory = column.getCellFactory(); //Ottieni property delle celle della colonna
        /* Crea pseudoclasse contenente le proprietà del CSS in 'highlights' per settare lo stato delle celle evidenziate */
        PseudoClass highlight = PseudoClass.getPseudoClass("highlight"); //Class css
        
        /* Setta le proprietà delle celle della colonna che devono essere evidenziate.
         * PS notare l'espressione LAMBDA: setCellFactory accetta un oggetto callback<P,R>() che nel nostro caso verrebbe creato come
         * classe anonima, andando ad effettuare override dell'interfaccia funzionale callback con l'unico metodo call.
         * La struttura nella versione estesa sarebbe new callback<tc,cell>(){  public cell call(tc){ //override metodo }  }. 	
        */
        column.setCellFactory(tc -> { /* tc è il parametro del metodo call dell'interfaccia callback da implementare */ 
	            TableCell<S,T> cell = currentCellFactory.call(tc); 
	            /* registra evento sulle colonne osservate da evidenziare (observablelist highlightsColumns). Se viene aggiunta una
	             * colonna da evidenziare, tale azione verrà eseguita da tutti e solo alla colonna corrispondente sara modificata
	             * la property delle sue celle.  */
	            highlightColumns.addListener((ListChangeListener.Change<? extends TableColumn<ArrayList<Double>,?>> c) -> 
	                    							cell.pseudoClassStateChanged(highlight, highlightColumns.contains(column))); /* 'Evidenzatore' registrata per le colonne da evidenziare osservate*/
	            
	            // aggiungi linea nuova poiche le celle in javaFX sono aggiunte dopo scroll
	            cell.pseudoClassStateChanged(highlight, highlightColumns.contains(column)); 
	            return cell ;
        	});
    }

    /**
     * Tale metodo viene utilizzato per visualizzare graficamente in una tabella (TableView) il dataset caricato da file.
     * Anche in questo caso, è necessario l'utilizzo di un ObservableList<ArrayList<Double>> perchè i record del dataset sono, appunto,
     * rappresentati con un arraylist di double e se deve creare un'associazione (binding) tra tabella e dati.
     * Per ogni colonna, si setta le caratteristiche delle sue property. Nello specifico, ad una cella del dataset corrisponde ad
     * una determinata colonna su cui visualizzare. Inoltre si noti che i double da visualizzare debbano essere solo di lettura.
     */
	public void visualizzaDataset()
    {        
    	// Crea una collezione observable di array che conterra' i record da aggiungere
        ObservableList<ArrayList<Double> > dataObs;
        dataObs = FXCollections.observableArrayList();
        
        /* Aggiungi record alla collezione Observable */
        for (int i=0;i<dataset.getNumRecord(); i++) 
        	dataObs.add(dataset.getRecord(i));
        
        /* Ad ogni colonna (TableColumn) settiamo una property (di solo lettura) indicando quali
    	 * celle del record del dataset debbano contenere. 
    	 * */
        lenPet.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Double>(param.getValue().get(0))); //Accetta observableValue<double>, non double
        lenSep.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Double>(param.getValue().get(1))); //JavaFX 2.0 introduce property, non usabili per il dataset realizzato
        larPet.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Double>(param.getValue().get(2)));
        larSep.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Double>(param.getValue().get(3)));
        /* Settati i record alla collezione e impostate le recgole di come memorizzare nelle tabella, passiamo alla tabella 
         * tutto il dataset */
        tableDataset.setItems(dataObs);
    }
    /**
     * Setta lo stage su cui il controller opererà
     * @param myStage stage su cui controller opera
     */
	void setStage(Stage myStage)
    {
    	this.myStage = myStage;
    }
	
	/** Metotdo chiamato dal FXMLloader quando l'inizializzazione del file corrispondente (.xml) è andato
	 * a buon fine. Le asserzioni mi assicurano la giusta correttezza del caricamento**/
	 @FXML 
	    void initialize() {
	        assert textSfoglia != null : "fx:id=\"textSfoglia\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert sfogliaButton != null : "fx:id=\"sfogliaButton\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert caricaButton != null : "fx:id=\"caricaButton\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert lenPetalo != null : "fx:id=\"lenPetalo\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert lenSepalo != null : "fx:id=\"lenSepalo\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert larPetalo != null : "fx:id=\"larPetalo\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert larSepalo != null : "fx:id=\"larSepalo\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert tableDataset != null : "fx:id=\"tableDataset\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert lenPet != null : "fx:id=\"lenPet\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert lenSep != null : "fx:id=\"lenSep\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert larPet != null : "fx:id=\"larPet\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert larSep != null : "fx:id=\"larSep\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert sceltaCluster != null : "fx:id=\"sceltaCluster\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert kmeansButton != null : "fx:id=\"kmeansButton\" was not injected: check your FXML file 'Datamining.fxml'.";
	        assert graficoButton != null : "fx:id=\"graficoButton\" was not injected: check your FXML file 'Datamining.fxml'.";

	    }
}
