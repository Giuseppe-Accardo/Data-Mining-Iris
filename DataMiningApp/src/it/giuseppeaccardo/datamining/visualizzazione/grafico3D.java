/**
 * PACKAGE RIGUARDANTE LA VISUALIZZAZIONE DEL GRAFICO 3D
 */
package it.giuseppeaccardo.datamining.visualizzazione;
import it.giuseppeaccardo.datamining.model.Cluster;
import it.giuseppeaccardo.datamining.model.DataMining;
import it.giuseppeaccardo.datamining.model.PCAcolt;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

/**
 * <h1>Grafico 2D!</h1>
 * Tale classe è predisposta per la visualizzazione di un grafico in 3D, ossia in 3 dimensioni associate
 * a delle corrispondenti fatures. Se il numero di features sono maggiori di 3, si applica un analisi delle
 * principali componenti per ridurne il numero di dimensioni a 3 senza perdere il contenuto informativo.
 * @author Giuseppe Accardo
 * @version 1.0
 * @since   14-02-2017
 * @see IvisualizzaGraficoStrategy
 * @see grafico2D
 */
public class grafico3D implements IvisualizzaGraficoStrategy {
	
    /** Dimensioni del grafico **/
    private int graphSize = 400;

    /** Variabili per gestire il mouse **/
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    
    /** Variabili per gestire la rotazione del grafico **/
    private final Rotate rotateX = new Rotate(20, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(330, Rotate.Y_AXIS);
	  
    /**
	   * Metodo reimplementato per la costruzione di un grafico in tre dimensioni. Costruisce un asse cartesiano in tre dimensioni e
	   * inserisce i punti colorandoli dei loro corrispondenti Cluster. Inoltre, viene inserite unaa legenda con un pie chart per
	   * illustrare ulteriore informazioni sulla clusterizzazione
	   * @param model Datamining costruito a partire da un dataset.
	   * @return Scene Ritorna la scena su cui sarà presente il grafico 3D e il Pie Chart contenente ulteriori informazioni
	   */
	@Override
	public Scene visualizzaGrafico(DataMining model) {
		
        // crea griglia
    	Group grid = createGrid(graphSize);
        // aggiungi la rotazione
    	grid.getTransforms().addAll(rotateX, rotateY);
        // Aggiungi oggetti alla scena. Root sarà il nodo principale
        BorderPane root = new BorderPane();
        // Inizializza una camera
        PerspectiveCamera camera = new PerspectiveCamera(true);

        // Crea il nodo contenente la sottoscena in 3D e aggiungi effetti alla sottoscena
        Group root3D = new Group(grid, camera);
        root3D.setTranslateX(500);
        root3D.setTranslateY(350);
        SubScene subScene = new SubScene(root3D, 990, 900, true, SceneAntialiasing.BALANCED);  
        subScene.setStyle("-fx-background-repeat: stretch;   "+
        		"-fx-background-size: 900 506;"+
        		"-fx-background-position: center center;"+
        		"-fx-effect: dropshadow(three-pass-box, black, 30, 0.5, 0, 0);");
        MotionBlur mb = new MotionBlur();
        mb.setRadius(1.0f);
        mb.setAngle(1.0f);
        subScene.setEffect(mb);
        // Aggiungi la sottoscena 3D al nodo principale
        root.setCenter(subScene);

        // Aggiungi nella griglia i punti
        grid.getChildren().addAll(creaPunti(model));
        // Aggiungi al nodo principale la legenda (2D), posizionandola a destra
        root.setRight(creaLegenda( model)); 

        // Crea la scena su cui agganciare il tutto
       Scene scene = new Scene(root, 1600, 900, false, SceneAntialiasing.DISABLED);
       root.setId("root");
       scene.getStylesheets().add(getClass().getResource("grafico3D.css").toExternalForm());

       //Vari eveti sul mouse (Espressioni Lambda)
        scene.setOnMousePressed(me -> {
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            rotateX.setAngle(rotateX.getAngle() - (mousePosY - mouseOldY));
            rotateY.setAngle(rotateY.getAngle() + (mousePosX - mouseOldX));
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;

        });

        /* Applica zoom sul 3D e ritorna tutta la scena */	
        makeZoomable(root3D);
        return scene;
    }
	
   /**
   * Crea una serie di punti costruiti come sfere. Da osservare che per un numero di features maggiori di 3, si applica la PCA,
   * algoritmo necessario per la riduzione delle componenti in uno spazio 3D. 
   * @param model Datamining costruito a partire da un dataset.
   * @return List Ritorna la serie di punti (sfere) colorati rispetto ai loro cluster.
   */
    private List<Sphere> creaPunti(DataMining model) {
    	// Punti contiene l'insieme di tutti i punti (sfere 3D) aventi il colore del loro corrispondente cluster
    	List<Sphere> punti = new ArrayList<Sphere>();    
    	/* Effettua il controllo se le features sono maggiori di 3. In tal caso, poichè il grafico presenta solo 3 dimensione, si
    	 * applica la PCA, algoritmo necessario per la riduzione delle componenti in uno spazio 3D, 
    	 * senza perdere il contenuto informativo */
    	double[][] mat;
    	if(model.getDatasetSelected().getFeatureUsate().size() > 3) mat = PCAcolt.pcaReduxMatrix(model.getDatasetSelected().toMatrix());
    	else mat = model.getDatasetSelected().toMatrix();
    	
    	//Prepara colori dei cluster
    	Stack<Color> colori = new Stack<Color>();
    	colori.push(Color.BLUE);colori.push(Color.GREEN);colori.push(Color.ORANGE);colori.push(Color.RED);
    	
    	//Per ogni cluster, crea i punti contenuti in esso
        for(Cluster cluster:model.getClusters())
        {	
        	for(Integer indice:cluster.getIndiciInseriti())
        	{	
        		/* Crea punto e carica le sue coordinate */
    	        Sphere punto = new Sphere();  
        		double x =mat[indice][0]*20; //20 è un offset rispetto allo spazio su cui si sta plottando
        		double y =mat[indice][1]*20;
        		double z =mat[indice][2]*20;    		
    	        //Setta le proprieta del punto
        		punto.setRadius(3.5);      	
        		punto.setTranslateX(x);
        		punto.setTranslateY(y);
        		punto.setTranslateZ(z);
    	        //phong material è usato per il colore. Per ogni cluster, verrà letto un colore e assegnato come proprieta del punto
    	        PhongMaterial material6 = new PhongMaterial();  
    	        material6.setDiffuseColor(colori.peek());
    	        punto.setMaterial(material6);
    	        /* Aggiungi punto alla lista dei punti*/
    	        punti.add(punto);
        	}
        	/* Cluster finito -> Passa ai colori successivi*/
        	colori.pop();
        }
        return punti;
	}
    
    /**
     * Crea una legenda su cui è inserito un Pie Chart che conterrà tutte le informazioni inerenti al grafico 3D (colore punti,
     * elementi per ogni cluster ed ecc)
     * @param model Datamining costruito a partire da un dataset.
     * @return BorderPane Ritorna un border pane contenente la legenda, costruito su un Pie chart, inerente al grfico 3D.
     */
	private BorderPane creaLegenda(DataMining model)
    {

		/* Crea il border pane, definendone grandeza e posizione, su cui inseriremo il pie chart. */
        BorderPane legenda = new BorderPane();
        legenda.setMaxHeight(704);
        legenda.setMinWidth(393);	
        legenda.setTranslateY(-4);
        legenda.setTranslateX(-110);
        legenda.setId("legenda"); //Id per il css
        
        /* Associa l'osservazione dei dati (observableList) ad un array list di tipo data.
         * Tale è necessario perchè i dati sul grafico presentano questo tipo*/        
        ObservableList<Data> dati = FXCollections.observableArrayList();
        /* Associa ogni elemento a dati, inserndone numero di cluster e grandezza cluster*/
        for(Cluster cluster:model.getClusters())
        	dati.add(new PieChart.Data("Cluster "+ (cluster.getId()+1), cluster.getIndiciInseriti().size())); 
        /* Per ogni dato inserisci una riga ulteriore per visualizzare il numero di punti. Da notare l'espressione
         * lambda con 'data' che rappresenta ogni elemento di'dati'*/
        dati.forEach(data ->data.nameProperty().bind(Bindings.concat(
                        data.getName(), ":\n [", data.pieValueProperty().getValue().intValue(), " punti] ")));
        /*Crea il pie chart e inserisci i dati creati*/
        final PieChart chart = new PieChart(dati);
        chart.setTitle("Visualizzazione Data Mining 3D");
        chart.setTranslateY(10);
        chart.setLegendVisible(true);
        chart.setLegendSide(Side.BOTTOM);
        chart.setMaxSize(358, 500);
        legenda.setTop(chart);       
    	return legenda;
    }
    		
    		
    /**
     * Crea assi per costruire il modello 3D che rappresenterà il nostro asse cartesiano. Da notare che è una classe
     * innestata.
     * @author Giuseppe Accardo
     * @version 1.0
     * @since   14-02-2017
     * @see grafico3D
     */
    public  class Axis extends Pane {
    	/** Griglia che andrà sul nostro "semi cubo" che rappresenterà l'asse cartesiano in 3D **/
        Rectangle wall;
        /**
        * Costruttore che Crea la griglia da sovrapporre al nostro semicubo
        * @param size Dimensioni del grafo.
        * @return Axis Assi da sovrapporre al cubo.
		*/
        public Axis(double size) {
        	//Wall è la parete o griglia da sovrapporre al cubo
            wall = new Rectangle(size, size);
            getChildren().add(wall);

            // definisce proprietà della griglia e le disegna
            double zTranslate = 0;
            double lineWidth = 1.0; //Spessore linee griglia
            Color gridColor = Color.DARKBLUE; //Colore linee griglia
            //Disegna rispetto agli assi di riferimento
            for (int y = 0; y <= size; y += size / 10) {
                Line line = new Line(0, 0, size, 0);
                line.setStroke(gridColor);
                line.setFill(gridColor);
                line.setTranslateY(y);
                line.setTranslateZ(zTranslate);
                line.setStrokeWidth(lineWidth);
                getChildren().addAll(line); //Ps Le linee sono aggiunte alla griglia aggiunta per prima!
            }
            for (int x = 0; x <= size; x += size / 10) {
                Line line = new Line(0, 0, 0, size);
                line.setStroke(gridColor);
                line.setFill(gridColor);
                line.setTranslateX(x);
                line.setTranslateZ(zTranslate);
                line.setStrokeWidth(lineWidth);
                getChildren().addAll(line);
            }
        }
        
        public void setFill(Paint paint) {
            wall.setFill(paint);
        }

    }

    /**
     * Effettua lo zoom, mediante evento del mouse, sulla figura 3D passata come Group
     * @param control Oggetto 3D su cui zoommare (cubo)
     */
    public void makeZoomable(Group control) {

        final double MAX_SCALE = 20.0;
        final double MIN_SCALE = 0.1;
        /* Aggiungi l'aumento della scala inserendo un evento di qualunque tipo di scrool.
         * Da notare che poteva essere utilizzata anche un espressione lambda*/
        control.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {

                double delta = 1.2;

                double scale = control.getScaleX();

                if (event.getDeltaY() < 0) {
                    scale /= delta;
                } else {
                    scale *= delta;
                }
                scale = clamp(scale, MIN_SCALE, MAX_SCALE);
                control.setScaleX(scale);
                control.setScaleY(scale);
                event.consume();
            }});
    }
    
    /**
     * Crea la griglia "reale" come un cubo con solo 3 facce perpendicolari tra loro, in modo tale da rappresentare
     * un asse cartesiano. Inolte vengo inseriti vari effetti, tra cui la griglia colorata sovrapposta creata con 'Axis'
     * @param size Dimensione del grafico
     * @see Axis
     * @return Group Cubo con 3 facce rappresentante l'asse cartesiano 3D.
     */
    private Group createGrid(int size) {
        Group cube = new Group(); // Gruppo contenenti le facci finali del cubo
        Color color = Color.LIGHTGRAY; //Colore pannelli
        List<Axis> cubeFaces = new ArrayList<>(); // Collezione di facce
        Axis r; //Assi che sovrappongono i pannelli
        
        /* Crea effetto di un contorno un pò soffuso da aggiungere alle facce del cubo*/
    	int depth = 10;
        DropShadow borderGlow= new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.DARKTURQUOISE);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);
        borderGlow.setSpread(1);
         
        /* Creazione delle facce del cubo */
        
        // Dietro
        r = new Axis(size);
        r.setFill(color.deriveColor(0.0, 1.0, (1 - 0.5 * 1), 1.0));
        r.setTranslateX(-0.5 * size);
        r.setTranslateY(-0.5 * size);
        r.setTranslateZ(0.5 * size);
        r.setEffect(borderGlow);
        cubeFaces.add(r);
        
        // Sotto
        r = new Axis(size);
        r.setFill(color.deriveColor(0.0, 1.0, (1 - 0.4 * 1), 1.0));
        r.setTranslateX(-0.5 * size);
        r.setTranslateY(0);
        r.setRotationAxis(Rotate.X_AXIS);
        r.setRotate(90);
        r.setEffect(borderGlow);
        cubeFaces.add(r);

        // Sinistra
        r = new Axis(size);
        r.setFill(color.deriveColor(0.0, 1.0, (1 - 0.2 * 1), 1.0));
        r.setTranslateX(0);
        r.setTranslateY(-0.5 * size);
        r.setRotationAxis(Rotate.Y_AXIS);
        r.setRotate(90);
        r.setEffect(borderGlow);
        cubeFaces.add(r);

        /* Aggiungi la collezione come un unico gruppo (cube) */ 
        cube.getChildren().addAll(cubeFaces);
        
        /* Costruzione dei numeri da inserire sulle facce */
        double gridSizeHalf = size / 2;
        double labelOffset = 18 ;
        double labelPos = gridSizeHalf - labelOffset ;
        for (double coord = -gridSizeHalf ; coord < gridSizeHalf ; coord+=40){
            Text xLabel = new Text(coord, labelPos, String.format("%.0f", coord/20));
            xLabel.setTranslateZ(labelPos);
            Text yLabel = new Text(labelPos, coord, String.format("%.0f", coord/20));
            yLabel.setTranslateZ(labelPos);
            Text zLabel = new Text(labelPos, labelPos, String.format("%.0f", coord/20));
            zLabel.setTranslateZ(coord);
            cube.getChildren().addAll(xLabel, yLabel, zLabel);
        }
        
        return cube;
    }
  
    /**
     * @param value valore da confrontare
     * @param min valore minimo
     * @param max calore massimo
     * @return Ritorna un valore che sia tra il minimo e il massimo, non oltre
     */
    public static double clamp(double value, double min, double max) {
        if (Double.compare(value, min) < 0)
            return min;
        if (Double.compare(value, max) > 0)
            return max;
        return value;
    }

}
