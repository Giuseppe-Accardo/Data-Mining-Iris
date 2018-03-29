/**
 * PACKAGE RIGUARDANTE LA VISUALIZZAZIONE DEL GRAFICO
 */
package it.giuseppeaccardo.datamining.visualizzazione;

import java.util.ArrayList;

import it.giuseppeaccardo.datamining.model.Cluster;
import it.giuseppeaccardo.datamining.model.DataMining;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

/**
 * <h1>Grafico 2D!</h1>
 * Tale classe è predisposta per la visualizzazione di un grafico in 2D, ossia in 2 dimensioni associate
 * a delle corrispondenti fatures.
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see IvisualizzaGraficoStrategy
 * @see grafico3D
 */
public class grafico2D implements IvisualizzaGraficoStrategy {
	
	/**
	 * Grafico costruito partendo da un oggetto di tipo ScatterChart parametrizzando gli assi
	 * a Number (superclasse dei tipi numerici)
	 */
	private ScatterChart<Number,Number> sc;
	   /**
	   * Metodo reimplementato per la costruzione di un grafico in due dimensioni. Genera un asse cartesiano in due dimensioni e
	   * inserisce i punti colorandoli dei loro corrispondenti Cluster.Anche in questo caso si utilizzerà un ObservableList, che 
	   * implementa implicitamente il concetto dell'Observer pattern, per tenere traccia dei dati da plottare sul grafico caricati in
	   * un ArrayList.
	   * @param model Datamining costruito a partire da un dataset.
	   * @return Scene Ritorna la scena su cui sarà presente il grafico costruito dal corrispondente metodo.
	   */
	@Override
	public Scene visualizzaGrafico(DataMining model) 
	{        	
		/* Definisci il range degli assi da visualizzare e istanzia l'oggetto ScatterChart */
		final NumberAxis xAxis = new NumberAxis(1, 8, 0.5);
        final NumberAxis yAxis = new NumberAxis(0.2, 7, 0.5);        
        sc = new ScatterChart<Number,Number>(xAxis,yAxis);
        sc.setId("2D-grafico");
        /* Stampa le features corrispondenti selezionate */
        xAxis.setLabel(model.getDatasetSelected().getFeatures()[model.getDatasetSelected().getFeatureUsate().get(0)]);                
        yAxis.setLabel(model.getDatasetSelected().getFeatures()[model.getDatasetSelected().getFeatureUsate().get(1)]);
        sc.setTitle("Visualizzazione Data Mining in 2D");
        
        /* Crea un ArrayList di coppie di punti (Series) */
        ArrayList<Series<Number,Number>> series = new ArrayList<Series<Number, Number>>();

        /* Per ogni cluster, inserisci tutti i punti associati. Poichè questo è il caso in 2D e il dataset su cui è stato applicato
         * il datamining produce una matrice 150x2, allora x sarà uguale alla prima colonna (0) e y uguale alla secondacolonna (1).
         * La serie di punti per ogni cluster sarà inserita in una serie finale (series).
         * */
        int i = 0;
        for(Cluster cluster:model.getClusters())
        {	
        	/* Crea una serie di punti per ogni cluster */
        	Series<Number, Number> serie = new Series<Number, Number>();
        	serie.setName("Cluster "+ (++i)+"\n("+cluster.getIndiciInseriti().size()+" fiori)");
        	/* Per ogni elemento di quel cluster*/
        	for(Integer indice:cluster.getIndiciInseriti())
        	{
        		/* Ricava x e y che saranno la prima e seconda colonna del dataset e aggiungi la coppia nella serie */
        		//System.out.println("FERMA: "+model.getDatasetSelected().getCella(indice, 0)+"  "+ model.getDatasetSelected().getCella(indice, 1));
        		double x = model.getDatasetSelected().getCella(indice, 0);
        		double y = model.getDatasetSelected().getCella(indice, 1);
        		serie.getData().add( new XYChart.Data<Number, Number>(x, y));        		
        	}
        	/* Aggiungi la serie dei punti per ogni cluster */
        	series.add(serie);        	
        }
        
        /* Associa l'osservazione dei dati (observableList) ad un array list di series costruito prima.  
         * Tale è necessario perchè i dati sul grafico presentano questo tipo*/
        sc.setData(FXCollections.<XYChart.Series<Number,  Number>>observableArrayList());
        sc.getData().addAll(series);
        
        /* Crea la scena contenente il grafico e restituiscilo */
        Scene scene = new Scene(sc,1000, 600, false, SceneAntialiasing.DISABLED);
        
		scene.getStylesheets().add(getClass().getResource("grafico2D.css").toExternalForm());
        return scene;
	}
   /**
   * Ritorna grafico
   * @return ScatterChart Ritorna il grafico.
   */
	public ScatterChart<Number, Number> getSc() {
		return sc;
	}
}
