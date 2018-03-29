package it.giuseppeaccardo.datamining;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

/**
 * <h1>Main!</h1>
 * Questo è il main principale dell'applicazione in JavaFX. Esso la parte "View" che si occupa di impostare un "Layout" predefinito, ossia
 * la parte statica dell'applicazione. Main estende application e il metodo che eredita più importante è il metodo start(Stage primaryStage).
 * Questo è automaticamente chiamato quando l'applicazione viene lanciata dall'interno del metodo main. Lo stage indica la finestra su cui
 * agganciare le scene che a loro volta conterranno la disposizione degli oggetti grafici (per definire la GUI)
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see DataminingController
 */
public class Main extends Application 
{
    private Stage primaryStage;
    private BorderPane rootLayout;
	
	@Override
	/** Metodo che ricevo uno Stage principale
	 * @param primaryStage stage principale dell'applicazione **/
	public void start(Stage primaryStage) 
	{
		this.primaryStage = primaryStage;
        initRootLayout();
	}
	/** Tale metodo apre un file FXML (layout principale), mediante un FXMLloader, e lo imposterà come
	 *  radice della scena contenuta nello stage. Inoltre il css viene utilizzato per definire lo stile grafico dell'layout.
	 *  Inoltre viene ceato il controller che si occuperà di interagire con tale stage principale (dataminingcontroller).
	 *  Il layout è costruito mediante SceneBuilder che genererà il corrispondente file FXML.
	 *  @exception IOException eccezione lanciata durante il caricamento errato dei file xml o css */
	 public void initRootLayout() 
	 {
	        try {
				/* Chiamala loader nella stessa directory per caricare il FXML creando un nodo radice di tipo Border
				 * Pane a cui agganceremo gli elementi */
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Datamining.fxml"));
				rootLayout = (BorderPane)loader.load();
				/* Crea scena a partire dall'FXML caricato e aggancia CSS alla scena */
				Scene scene = new Scene(rootLayout,500,500);	
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				/* Setta lo stage e mostra la scena creata */
				primaryStage.setTitle("Data Mining - Iris dataset");
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);
				primaryStage.show();
				/* Definisci controller che interagirà con tale stage */
				DataminingController controller = (DataminingController)loader.getController();
				controller.setStage(primaryStage); 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 }
	 
	/** Avvia o lancia l'applicazione **/
	public static void main(String[] args) 
	{
		//Avvia metodo di Application inizializzando uno stage e richiama start
		launch(args);
	}
}
