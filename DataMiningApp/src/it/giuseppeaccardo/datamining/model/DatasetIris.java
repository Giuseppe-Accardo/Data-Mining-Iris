package it.giuseppeaccardo.datamining.model;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * <h1>Dataset IRIS!</h1>
 * Tale classe è l'applicazione di un dataset (Iris). Essa eredita da dataset lo scheletro costruito in Dataset in cui 
 * Iris ne implementa il metodo astratto "caricaDataset" (Template Method). Il caricamento avviene mediante un file
 * .data in cui viene definita anche la modalità di memorizzazione nella tabella.
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see Dataset
 */
public class DatasetIris extends Dataset
{
	/**
	 *  Costruttore che Definisce le features e nome dataset. Carica il dataset è implementato in questa classe
	 *  @param path nome file
	 *  @return Restituisce il dataset
	 *  */
	public DatasetIris(String path) throws IOException
	{
		
		// Costruttore padre Imposta il nome dataset e numero colonne
		// NomeDataset
		/* Uso di varargs, altrimenti sarrebbe stato new String["LunghezzaSepalo","......."] */
		super("Iris",   "Lunghezza Petalo",
						"Lunghezza Sepalo",
						"Larghezza Petalo",
						"Larghezza Sepalo");
		System.out.println("Nome dataset: " + getNomeDataset());
		CaricaDataset(path);
		System.out.println("Record letti da dataset: " + super.getNumRecord());		
	}
		
	/**
	 *  Metodo astratto ereditato da Dataset ed reimplementato.
	 *  Dataset Iris è un file .data che bisogna scorrere e caricare
	 *  @param path nome file da cui caricare iris
	 *  @throws IOException
	 *  @exception FileNotFoundException
	 *  @exception IOException
	 *  */
	// Throws specifica l'eccezzione che potrebbe lanciare (IOEception e le sue sottoclassi)
	protected void CaricaDataset(String pathFile) throws IOException
	{
		BufferedReader bufferLetto = null;
		String line = "";
		String cvsSplitBy = ",";

		try {
			/* Carica file leggendo un stream di caratteri dal path indicato in input*/
			bufferLetto = new BufferedReader(new FileReader(pathFile));
			/* Effettua parsing del buffer delle righe apert */
			while ((line = bufferLetto.readLine()) != null) 
			{
				if (line.length() > 0) 
				{
					/* Per ogni record letto, inseriscilo nella struttura dati con addRow*/
					String[] cell = line.split(cvsSplitBy);
					this.addRow(cell);
				}
			}
		} 
		catch (FileNotFoundException e) {/* Caso di file non trovato */
			System.err.println("Eccezione: file not found lanciata");
			e.printStackTrace();
			throw e; /* Rimanda l'eccezione anche verso il view */
		} 
		finally {
			if (bufferLetto != null) {
				try {/* chiudi il buffer aperto*/
					bufferLetto.close();
				} 
				catch (IOException e) { /* Errore durante chiusura */
					e.printStackTrace();
					throw e;
				}
			}
		}
	}
	
	/**
	 *  Aggiungi la riga letta da file nella struttura dati  ereditata dalla superclasse 
	 *  @param cell[] record da inserire
	 *  */
	private void addRow(String[] cell) throws NumberFormatException
	{
		for(int i=0; i<super.getFeatures().length; i++)
			super.setElemento(i, Double.parseDouble(cell[i].toString()));
	}
}