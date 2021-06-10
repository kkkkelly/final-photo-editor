/* paint*/
import javafx.fxml.FXML; 
import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import javafx.scene.Group;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.*; 
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.shape.*; 
import javafx.scene.paint.*;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javax.swing.JLayeredPane;
import javafx.scene.image.*;
import javafx.scene.Node;
import javafx.scene.SnapshotResult;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.beans.value.*;
import javafx.scene.effect.SepiaTone;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import java.awt.Graphics2D;
import java.awt.Transparency;  
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.Cursor;
import javafx.scene.control.ToolBar;
import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
/* for BufferImage produce*/
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
/* for swap*/
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.effect.ColorAdjust;

public class PhotoEditorController { 
	@FXML private Pane BackgroundPane;
	@FXML private StackPane stackPane;
	@FXML private DialogPane DiaPane;
	@FXML private VBox Layer_Vbox;
	@FXML private Button newLayerButton;
	@FXML private Button visibleLayerButton;
	@FXML private Button deleteLayerButton;
	/*slider*/
	@FXML private Slider graySlider;
	@FXML private Slider OpacitySlider;
	@FXML private Slider brightSlider;
	@FXML private Slider HueSlider;
	@FXML private Slider SaturationSlider;
	/* NewDialog*/
	@FXML private TextField TF_Width;
	@FXML private TextField TF_Height;
	/*new button*/
	@FXML private Button changeSizeButton;
	@FXML private TextField height_text;
	@FXML private TextField width_text;
	//@FXML private Button upButton;
	//@FXML private Button downButton;
	//@FXML private Button leftButton;
	//@FXML private Button rightButton;
	/*colorpicker*/
	@FXML private ColorPicker colorpicker;
	/*pensize*/
	@FXML private Slider pensizeSlider;
	/*translate button*/
	@FXML private Button transparentButton;
	/*reflecSlider*/
	@FXML private Slider reflectSlider;
	/*cutButton*/
	@FXML Button cutButton;
	@FXML Button selectButton;
	/* Text */
	@FXML private ComboBox<String> fontBox;
	@FXML private ComboBox<String> fontSizeBox;
	@FXML private Pane textPane;
	@FXML private TextField typeInField;
	@FXML private RadioButton boldRadioButton;
	@FXML private RadioButton italicRadioButton;
	/* Tool bar */
	@FXML private Button  backToMenuButton;
	@FXML private StackPane toolStackPane;
	@FXML private ToolBar toolBar;
	@FXML private ToolBar textToolBar;

	
	
	//final static SepiaTone sepiaEffect = new SepiaTone();
	ArrayList<Layer> LayerArrayList = new ArrayList<Layer>();
	ArrayList<PreStep> PreStepArrayList = new ArrayList<PreStep>();
	/* For now Layer component*/
	Button	nowButton = null;
	Layer	nowLayer = null;
	ImageView	nowImageView = null;
	/*For last Layer component*/
	Button	lastButton = null;
	Layer	lastLayer = null;
	/*For preStep*/
	PreStep	preStep = null;
	/*cut*/
	RubberBandSelection rb;
	/* font */
	Font	  font = null;
	
	int				height=300, width=300;	//painter
	
	BufferedImage	img = null;
	int				number = 0; //the number of exist layers
	int				totalnumber = 0; //the number of total layers
	boolean			isOpen = false, isFirstNew = true, isPressButtonDelete = true;
	double			orgSceneY, orgTranslateY; //for LayerButton drag
	int				offsetIndex = 0;
	private	int		gray = 0;
	int				LBWidth = 140 , LBHeight = 35;	//layer button width and height
	/* for draw */
	javafx.scene.paint.Color	penColor = Color.BLACK;
	int				penSize = 4;
	double			lastX = 0;
	double			lastY = 0;
	/* font */
	String			fontName = "Calibri";
	int				fontSize = 24;
	boolean			isBold = false;
	boolean			isItalic = false;
	/* the mode on stackpane */
	int				mode = 0;
	int				draw = 0;
	int				typeText = 1;
	int				notTypeOver = 2;
	int				erase = 3;
	boolean  		cut = false;
	boolean 		transparentpen = false;
	
	public void initialize() {	
		backToMenuButton.setDisable(true);
		/* For paint */
		stackPane.setOnMousePressed(e->{
			/* For PreStep*/
			preStep = new PreStep(nowLayer);
			PreStepArrayList.add(preStep);
			preStep.mode = 5;
			preStep.layer = nowLayer;
			preStep.setBfImage(nowLayer.getImage());
			/* get the last X,Y in stackPane */
			lastX = e.getX();
			lastY = e.getY();
			//System.out.println("last="+lastX+","+lastY);
		});
		//graySlider
		graySlider.valueProperty().addListener(
		new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov,
			Number oldValue, Number newValue) {
				nowLayer.sepiaEffect.setLevel(newValue.doubleValue());

			}
		}
		);
		
		OpacitySlider.valueProperty().addListener(
		new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov,
			Number oldValue, Number newValue) {
				nowLayer.imageView.setOpacity((double)(Math.round(newValue.intValue()))/100.0);
				nowLayer.setOpacityvalue((double)(Math.round(newValue.intValue()))/100.0);
			}
		}
		);
		
		brightSlider.valueProperty().addListener(
		new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov,
			Number oldValue, Number newValue) {

				nowLayer.setBright((double)(Math.round(newValue.intValue()))/100.0);

			}
		}
		);
		
		SaturationSlider.valueProperty().addListener(
		new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov,
			Number oldValue, Number newValue) {

				nowLayer.set_Saturation((double)(Math.round(newValue.intValue()))/100.0);

			}
		}
		);
		
		HueSlider.valueProperty().addListener(
		new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov,
			Number oldValue, Number newValue) {

				nowLayer.set_Hue((double)(Math.round(newValue.intValue()))/100.0);

			}
		}
		);
		
		colorpicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                
				System.out.println(colorpicker.getValue());
				penColor = colorpicker.getValue();
            }
        });
		pensizeSlider.valueProperty().addListener(
		new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov,
			Number oldValue, Number newValue) {

				penSize = (Math.round(newValue.intValue()));

			}
		}
		);
		
		reflectSlider.valueProperty().addListener(
		new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov,
			Number oldValue, Number newValue) {
				nowLayer.set_Reflect(newValue.doubleValue());

			}
		}
		);
		/* For text */
		font = new Font(fontName, fontSize);
		typeInField.setFont(font);
		typeInField.setStyle("-fx-background-color: transparent;");
		/* For Font choose */
		GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String envfonts[] = gEnv.getAvailableFontFamilyNames();
		fontBox.getItems().addAll(envfonts);
		fontBox.setValue("Calibri");	// initial font
		fontBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue ov, String original, String choose) {
				fontName = choose;
				font = font.font(fontName, isBold == true ? FontWeight.BOLD : FontWeight.NORMAL, isItalic == true ? FontPosture.ITALIC : FontPosture.REGULAR, (double)fontSize);
				typeInField.setFont(font);
			}  
		});
		/* For Font Size choose */
		String size[] = {"8","12","24","28","36","48","72"};
		fontSizeBox.getItems().addAll(size);
		fontSizeBox.setValue("24");	// initial font
		fontSizeBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue ov, String original, String choose) {
				fontSize = Integer.parseInt(choose);
				font = font.font(fontName, isBold == true ? FontWeight.BOLD : FontWeight.NORMAL, isItalic == true ? FontPosture.ITALIC : FontPosture.REGULAR, (double)fontSize);
				typeInField.setFont(font);
			}  
		});
	} 
	
	public void New_newpaint(){
		TF_Width.setText(String.valueOf(width));
		TF_Height.setText(String.valueOf(height));
		Dialog <ButtonType>dialog = new Dialog<>();
		dialog.setTitle("Set painter size");
		dialog.setResizable(false);
		dialog.getDialogPane().setContent(DiaPane);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
		dialog.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				width = Integer.parseInt(TF_Width.getText());
				height = Integer.parseInt(TF_Height.getText());
				stackPane.setPrefSize(width, height);
				BackgroundPane.setPrefSize(width, height);
				stackPane.setVisible(true);
				if (isFirstNew == true)
					newlayer();
				else 
					for (Layer layer:LayerArrayList)
						layer.resizeStackPane(width, height);
			}
		});
	}
	
	public void Open_readImg() {	
		File selectedFile = new File(new String (System.getProperty("user.dir")));	//get User working directory
		stackPane.setVisible(true);
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File"); 
		fileChooser.getExtensionFilters().addAll(
			new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
			new ExtensionFilter("All Files", "*.*")
		);
		fileChooser.setInitialDirectory(selectedFile);
		selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			System.out.println(selectedFile.toURI().toString());	
		}
		else
			return;

		Image ima = new Image(selectedFile.toURI().toString(), width,height, false, true);
		img = SwingFXUtils.fromFXImage(ima, null);
		isOpen = true;
		newlayer();		
	}

	public void Open_readProject() {	
		/* select project file (.txt) */
		File selectedFile = new File(new String (System.getProperty("user.dir")));	//get User working directory
		stackPane.setVisible(true);
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Project File"); 
		fileChooser.getExtensionFilters().addAll(
			new ExtensionFilter("Project Files", "*.txt"),
			new ExtensionFilter("All Files", "*.*")
		);
		fileChooser.setInitialDirectory(selectedFile);
		selectedFile = fileChooser.showOpenDialog(null);
		/* read project file */
		if (selectedFile != null) {
			System.out.println(selectedFile);	
			LayerArrayList = projectFile.read(selectedFile);
			if (LayerArrayList == null){
				Stage stage = (Stage) stackPane.getScene().getWindow();
				showPopupMessage("This is not a Project file", stage);
				return;
			}
		}else return;
		/* setup */
		for (Layer layer: LayerArrayList){
			stackPane.getChildren().add(layer.imageView);
			/* set button */
			nowButton = layer.LayerButton;
			nowButton.setPrefSize(LBWidth,LBHeight);
			nowButton.setMinSize(LBWidth,LBHeight);
			nowButton.setOnMousePressed(new LayerButtonPressHandler());
			nowButton.setOnMouseDragged(new LayerButtonDragHandler());
			nowButton.setOnMouseReleased(e -> {
				((Button) (e.getSource())).setCursor(Cursor.DEFAULT);
				if (preStep.Delta != 0)
					preStep.mode = 1; //change Layer mode
				if (preStep.mode == -1)
					PreStepArrayList.remove(preStep);
			});
			nowButton.setOnMouseClicked(e -> {if (e.getButton() == MouseButton.SECONDARY) LayerRename();});
			Layer_Vbox.getChildren().add(nowButton);
			Layer_Vbox.getChildren().get(number).toBack();//move new button to top
			/* set visible */
			if (layer.visible == false){
				stackPane.getChildren().get(layer.layerNumber).setVisible(false);
				nowButton.setStyle("-fx-background-color: LIGHTSLATEGRAY;"+"-fx-text-fill: DARKGRAY;");
			}
			number++;
			totalnumber++;
		}
		if (LayerArrayList.size() > 1)
			deleteLayerButton.setDisable(false);
		height = LayerArrayList.get(0).height;
		width  = LayerArrayList.get(0).width;
		newlayer();
		LayerDelete();
		preStep = null;
		totalnumber--;
		System.out.println("open successful");
	}

	public void SaveAs_project(){
		File file = new File(new String (System.getProperty("user.dir")));	//get User working directory
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Image File"); 
		fileChooser.getExtensionFilters().addAll(
			new ExtensionFilter("txt","*.txt")
		);
		fileChooser.setInitialDirectory(file);
		file = fileChooser.showSaveDialog(null);
		
		if (file != null) {
			try {
				projectFile.write(LayerArrayList, file);
				System.out.println("save complete");
			} catch (IOException ex) {
				System.out.println("fail output txt");
			}
		}
	}

	public void SaveAs_save(){
		File file = new File(new String (System.getProperty("user.dir")));	//get User working directory
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
			new ExtensionFilter("Png","*.png")
		);
		fileChooser.setInitialDirectory(file);
		file = fileChooser.showSaveDialog(null);
		
		
		if (file != null) {
			System.out.println(file);
			try {
				SnapshotParameters parameters = new SnapshotParameters();
				parameters.setFill(Color.TRANSPARENT);
				WritableImage writableImage = stackPane.snapshot(parameters, null);
				RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);            
				ImageIO.write(renderedImage, "png", file);
				System.out.println("save complete");
				Desktop.getDesktop().open(file);
			} catch (IOException ex) {
				Logger.getLogger(PhotoEditorController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
		
	public void newlayer(){	
		Layer	newLayer = null;
		if (isFirstNew){
			BackgroundPane.setVisible(true);
			newLayerButton.setDisable(false);
			visibleLayerButton.setDisable(false);
		}
		else // if isFirstNew == true, there is only one layer, so remain disabled the delete button
			deleteLayerButton.setDisable(false);
		if (isOpen == true){
			newLayer = new Layer("new"+String.valueOf(totalnumber), img, width, height);
			isOpen = false;
		}
		else{
			newLayer = new Layer("new"+String.valueOf(totalnumber), width, height);
			stackPane.setVisible(true);
		}
		isFirstNew = false;
		LayerArrayList.add(newLayer);
		newLayer.layerNumber = number;
		stackPane.getChildren().add(newLayer.imageView);

		/* set nowlayer component = new component */
		nowLayer = newLayer;
		nowImageView = nowLayer.imageView;
		nowButton = nowLayer.LayerButton;

		nowButton.setPrefSize(LBWidth,LBHeight);
		nowButton.setMinSize(LBWidth,LBHeight);
		nowButton.setOnMousePressed(new LayerButtonPressHandler());
		nowButton.setOnMouseDragged(new LayerButtonDragHandler());
		nowButton.setOnMouseReleased(e -> {
			((Button) (e.getSource())).setCursor(Cursor.DEFAULT);
			if (preStep.Delta != 0)
				preStep.mode = 1; //change Layer mode
			if (preStep.mode == -1)
				PreStepArrayList.remove(preStep);
		});
		nowButton.setOnMouseClicked(e -> {if (e.getButton() == MouseButton.SECONDARY) LayerRename();});
		Layer_Vbox.getChildren().add(nowButton);
		Layer_Vbox.getChildren().get(number).toBack();//move new button to top
		
		produceLayerButtonColor();
		
		number++;
		totalnumber++;
		
		/* For PreStep */
		preStep = new PreStep(nowLayer);
		PreStepArrayList.add(preStep);
		preStep.mode = 4;
	}
	
	public  Layer getLayer(Button button){	// use layer button name to get Layer
		int index = 0;
		for (Layer array:LayerArrayList){
			if (button == array.LayerButton)	//compare with reference
				return LayerArrayList.get(index);
			else
				index++;
		}
		return null;
	}

	@FXML	//***
	private void LeftAndRightButtonPressed(ActionEvent event) {
		Graphics2D g2d = nowLayer.BfImage.createGraphics();

		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-nowLayer.BfImage.getWidth(null), 0);

		AffineTransformOp op = new AffineTransformOp(tx,
		AffineTransformOp.TYPE_BILINEAR);
		nowLayer.BfImage = op.filter(nowLayer.BfImage, null);
		nowLayer.refreshImageView();
		//nowLayer.imageView.setRotate(nowLayer.imageView.getRotate() + 90);
		
		
	}	
	@FXML	//***
	private void rotateButtonPressed(ActionEvent event) {
		//nowLayer.setSize(nowLayer.width,nowLayer.height);
		nowLayer.imageView.setRotate(nowLayer.imageView.getRotate() + 90);
		nowLayer.setrotateSize(nowLayer.width,nowLayer.height);
		System.out.println("width:"+nowLayer.width+"height"+nowLayer.height);
	}

	public void LayerDelete(){
		/* For preStep*/
		if (isPressButtonDelete == true){
			preStep = new PreStep(nowLayer);
			PreStepArrayList.add(preStep);
			preStep.indexOfButton = Layer_Vbox.getChildren().indexOf(nowButton);	// get the index in Layer_Vbox
			preStep.mode = 2;
		}
		isPressButtonDelete = true;
		
		number--;
		stackPane.getChildren().remove(nowImageView);
		Layer_Vbox.getChildren().remove(nowButton);
		int removeLayer = nowLayer.layerNumber;
		int index = 0;
		for (Layer array:LayerArrayList){
			if (array.layerNumber > removeLayer){
				LayerArrayList.get(index).layerNumber--;
			}
			index++;
		} 
		LayerArrayList.remove(nowLayer.layerNumber);
		
		if (LayerArrayList.size() == 1)	// if there is only one layer
			deleteLayerButton.setDisable(true);
		else {
			if (nowLayer.layerNumber == LayerArrayList.size()) 	//top
				nowLayer = LayerArrayList.get(nowLayer.layerNumber - 1);
			else	//not top
				nowLayer = LayerArrayList.get(nowLayer.layerNumber);
			nowImageView = nowLayer.imageView;
			nowButton = nowLayer.LayerButton;
			produceLayerButtonColor();
		}
	}
	
	public void LayerVisible(){
		if (nowLayer.visible == true){
			stackPane.getChildren().get(nowLayer.layerNumber).setVisible(false);
			nowLayer.visible = false;
			nowButton.setStyle("-fx-background-color: LIGHTSLATEGRAY;"+"-fx-text-fill: DARKGRAY;");
		}
		else{
			stackPane.getChildren().get(nowLayer.layerNumber).setVisible(true);
			nowLayer.visible = true;
			nowButton.setStyle(null);
		}
	}
	
	public void LayerUp(){
		//Layer_Vbox
		int n = number - 1 - nowLayer.layerNumber;	// get the index in Layer_Vbox
		if (n == 0)	//is already on top
			return;
		ObservableList<Node> workingCollection = FXCollections.observableArrayList(Layer_Vbox.getChildren());
		Collections.swap(workingCollection, n-1, n);
		Layer_Vbox.getChildren().setAll(workingCollection);
		//stackPane
		n = nowLayer.layerNumber;	// get the index in Layer_Vbox
		workingCollection = FXCollections.observableArrayList(stackPane.getChildren());
		Collections.swap(workingCollection, n, n+1);
		stackPane.getChildren().setAll(workingCollection);
		//LayerArrayList of Layer
		n = nowLayer.layerNumber;
		Layer temp = LayerArrayList.get(n + 1);
		LayerArrayList.set(n + 1	, LayerArrayList.get(n));
		LayerArrayList.set(n		, temp);	
		LayerArrayList.get(n + 1).layerNumber++;
		LayerArrayList.get(n).layerNumber--;
	}
	
	public void LayerDown(){
		//Layer_Vbox
		int n = number - 1 - nowLayer.layerNumber;	// get the index in Layer_Vbox
		if (n == number -1)	//is already on bottom
			return;
		ObservableList<Node> workingCollection = FXCollections.observableArrayList(Layer_Vbox.getChildren());
		Collections.swap(workingCollection, n, n+1);
		Layer_Vbox.getChildren().setAll(workingCollection);
		//stackPane
		n = nowLayer.layerNumber;	// get the index in Layer_Vbox
		workingCollection = FXCollections.observableArrayList(stackPane.getChildren());
		Collections.swap(workingCollection, n-1, n);
		stackPane.getChildren().setAll(workingCollection);
		//LayerArrayList of Layer
		n = nowLayer.layerNumber;
		Layer temp = LayerArrayList.get(n - 1);
		LayerArrayList.set(n - 1	, LayerArrayList.get(n));
		LayerArrayList.set(n		, temp);	
		LayerArrayList.get(n - 1).layerNumber--;
		LayerArrayList.get(n).layerNumber++;
	}	
	public void SwapLayer(Layer x, int delta){
		//Layer_Vbox
		int n = Layer_Vbox.getChildren().indexOf(x.LayerButton);	// get the index in Layer_Vbox
		//check if the Layer is already at bottom or front
		if ((Layer_Vbox.getChildren().size()-1 == n && delta == 1 ) || (n == 0 && delta == -1) )
			return;
		ObservableList<Node> workingCollection = FXCollections.observableArrayList(Layer_Vbox.getChildren());
		Collections.swap(workingCollection, n, n+delta);
		Layer_Vbox.getChildren().setAll(workingCollection);
		//stackPane
		n = x.layerNumber;	// get the index in Layer_Vbox
		workingCollection = FXCollections.observableArrayList(stackPane.getChildren());
		Collections.swap(workingCollection, n, n-delta);
		stackPane.getChildren().setAll(workingCollection);
		//LayerArrayList of Layer
		n = x.layerNumber;
		Layer temp = LayerArrayList.get(n - delta);
		LayerArrayList.set(n - delta	, LayerArrayList.get(n));
		LayerArrayList.set(n		, temp);	
		LayerArrayList.get(n - delta).layerNumber-=delta;
		LayerArrayList.get(n).layerNumber+=delta;
	}
	public void LayerRename(){
		TextInputDialog textInputDialog = new TextInputDialog(nowLayer.getName());
		textInputDialog.setTitle("Rename");
		textInputDialog.setHeaderText("Original name is "+nowLayer.getName());
		textInputDialog.setContentText("Name replaced by:");
		Optional<String> opt = textInputDialog.showAndWait();
		String rtn;
		try{
			rtn = opt.get(); //can replaced by textInputDialog.getResult()
		}catch(final NoSuchElementException ex){
			rtn = nowLayer.getName();
		}
		/* For PreStep */
		String oldName = new String(nowLayer.Layer_name);
		if (oldName.equals(rtn) == false){
			preStep = new PreStep(nowLayer);
			PreStepArrayList.add(preStep);
			preStep.mode = 3;
			preStep.oldName = oldName;
		}
		nowLayer.setName(rtn);
	}
	
	public void produceLayerButtonColor(){
		if (nowButton != lastButton && lastButton != null){
			if (lastLayer.visible == false)
				lastButton.setStyle("-fx-text-fill: DARKGRAY;");
			else
				lastButton.setStyle(null);
		}
		
		if (nowLayer.visible == false){
			nowButton.setStyle("-fx-background-color: LIGHTSLATEGRAY;"+"-fx-text-fill: DARKGRAY;");
		}
		else
			nowButton.setStyle("-fx-background-color: LIGHTSLATEGRAY;");
		
		lastButton = nowButton;
		lastLayer = nowLayer;
	}
	
	class LayerButtonPressHandler implements EventHandler<MouseEvent>{
		@Override
		public void handle(MouseEvent event){
			orgSceneY = event.getSceneY();	//the Y of the window

			nowButton = (Button)event.getSource();
			nowLayer = getLayer(nowButton);
			nowImageView = nowLayer.imageView;
			
			produceLayerButtonColor();
			changeslider();
			/* For PreStep*/
			preStep = new PreStep(nowLayer);
			PreStepArrayList.add(preStep);
		}
	}
	/*change slider value*/
	public void changeslider(){
		graySlider.setValue(nowLayer.sepiaEffect.getLevel());
		brightSlider.setValue((int)(nowLayer.getBright()*100));
		HueSlider.setValue((int)(nowLayer.getHue()*100));
		OpacitySlider.setValue((int)(nowLayer.Opacity_value*100));
		SaturationSlider.setValue((int)(nowLayer.saturation_value*100));
		reflectSlider.setValue(nowLayer.reflect_value);
	}

	class LayerButtonDragHandler implements EventHandler<MouseEvent>{
		@Override
		public void handle(MouseEvent event){
			double offsetY = event.getSceneY() - orgSceneY;	//the delta Y of the window
			offsetIndex =(int) offsetY / LBHeight;
			if (offsetIndex >= 1){
				for (int i = 0; i < offsetIndex; i++){
					LayerDown();
					orgSceneY+=LBHeight;
				}
			}
			else if (offsetIndex <= -1){
				for (int i = 0; i > offsetIndex; i--){
					LayerUp();
					orgSceneY-=LBHeight;
				}
			}
			preStep.Delta+=offsetIndex;
			((Button) (event.getSource())).setCursor(Cursor.CLOSED_HAND);
		}
	}

	public void StackPaneDragHandler (MouseEvent e){
		if(cut==false){
			if (nowLayer.visible == false){
				Stage stage = (Stage) stackPane.getScene().getWindow();
				showPopupMessage("This layer is hidden!", stage);
			}else if(transparentpen){
				Graphics2D g2d = nowLayer.BfImage.createGraphics();	// draw on BfImage
				g2d.setColor(convertColor(Color.WHITE));
				g2d.setStroke(new BasicStroke(penSize));
				g2d.drawLine((int)lastX, (int)lastY, (int)e.getX(), (int)e.getY());
				lastX = e.getX();
				lastY = e.getY();
				
				Image image = SwingFXUtils.toFXImage(nowLayer.BfImage, null);
				image = makeTransparent(image);
				nowLayer.BfImage = SwingFXUtils.fromFXImage(image, null);
				nowLayer.imageView.setImage(image);
				
				nowLayer.refreshImageView();
			}
			else if (mode == draw){	
				Graphics2D g2d = nowLayer.BfImage.createGraphics();	// draw on BfImage
				g2d.setColor(convertColor(penColor));
				g2d.setStroke(new BasicStroke(penSize));
				g2d.drawLine((int)lastX, (int)lastY, (int)e.getX(), (int)e.getY());
				lastX = e.getX();
				lastY = e.getY();
				/*...
				Image image = SwingFXUtils.toFXImage(nowLayer.BfImage, null);
				image = makeTransparent(image);
				nowLayer.BfImage = SwingFXUtils.fromFXImage(image, null);
				nowLayer.imageView.setImage(image);
				*/
				nowLayer.refreshImageView();	//convert BfImage into ImageView
			}
			else if (mode == notTypeOver){	//move the text when not over type in
				typeInField.setLayoutX(typeInField.getLayoutX() + (e.getX()-lastX));
				typeInField.setLayoutY(typeInField.getLayoutY() + (e.getY()-lastY));
				typeInField.setPrefWidth(textPane.getWidth() - typeInField.getLayoutX());
				lastX = e.getX();
				lastY = e.getY();
			}
		}
	}
	@FXML	
	private void transparentpenButtonPressed(ActionEvent event){
		if(transparentpen==false)
			transparentpen = true;
		else
			transparentpen = false;
	}
	
	
	
	public void StackPaneClickHandler (MouseEvent e){
		if (mode == typeText){
			lastX = e.getX();
			lastY = e.getY();
			typeInField.setLayoutX((int)e.getX() - 10);
			typeInField.setLayoutY((int)e.getY() - 40);
			typeInField.setPrefWidth(textPane.getWidth() - typeInField.getLayoutX());
			typeInField.requestFocus();
			textPane.setCursor(Cursor.MOVE);
			mode = notTypeOver;
		}
		
	}
	
	public java.awt.Color convertColor(javafx.scene.paint.Color fx){
		java.awt.Color awtColor = new java.awt.Color((float) fx.getRed(),
                                             (float) fx.getGreen(),
                                             (float) fx.getBlue(),
                                             (float) fx.getOpacity());
		return awtColor;
	}
	@FXML
	private void transparentButtonPressed(ActionEvent event) {
		System.out.println("translate button pressed");
		Image image = SwingFXUtils.toFXImage(nowLayer.BfImage, null);
		image = makeTransparent(image);
		nowLayer.BfImage = SwingFXUtils.fromFXImage(image, null);
		nowLayer.imageView.setImage(image);
		//nowLayer.refreshImageView();
	}
	@FXML
	private Image makeTransparent(Image inputImage) {
		int TOLERANCE_THRESHOLD =  0xCF;
        int W = (int) inputImage.getWidth();
        int H = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(W, H);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int argb = reader.getArgb(x, y);

                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                if (r >= TOLERANCE_THRESHOLD 
                        && g >= TOLERANCE_THRESHOLD 
                        && b >= TOLERANCE_THRESHOLD) {
                    argb &= 0x00FFFFFF;
                }

                writer.setArgb(x, y, argb);
            }
        }

        return outputImage;
    }
	/*resize the layer*/
	@FXML
	private void changeSizeButtonPressed(ActionEvent event) {
		
		int h = Integer.parseInt(height_text.getText());
		
		int w = Integer.parseInt(width_text.getText());
		if(h>height)
			h = height;
		if(w>width)
			w = width;
		System.out.println(h+" "+w);
		nowLayer.setSize(h,w);
	}
	
	/* for cut image*/
	@FXML
	private void selectButtonPressed(ActionEvent event) {
		cut=true;
		rb = new RubberBandSelection(BackgroundPane);
	}
	@FXML
	private void cutButtonPressed(ActionEvent event) {
		if(cut==false)
			return;
	
		Rectangle2D croppedPortion = new Rectangle2D((int)rb.rect.getX(), (int)rb.rect.getY(), (int)rb.getw(),(int)rb.geth());

        nowLayer.imageView.setViewport(croppedPortion);
		rb.notselect();
		nowLayer.setSize((int)rb.geth(),(int)rb.getw());
		System.out.println(nowLayer.height+" "+nowLayer.width);
		
		cut=false;
	}

	/* for show a popup */
	public static void showPopupMessage(final String message, final Stage stage) {
		final Popup popup = createPopup(message);
		popup.setOnShown(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				popup.setX(stage.getX() + stage.getWidth()/2 - popup.getWidth()/2);
				popup.setY(stage.getY() + stage.getHeight()/2 - popup.getHeight()/2);
			}
		});        
		popup.show(stage);
	}
	public static Popup createPopup(final String message) {
		Popup popup = new Popup();
		popup.setAutoFix(true);
		popup.setAutoHide(true);
		popup.setHideOnEscape(true);
		Label label = new Label(message);
		label.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				popup.hide();
			}
		});
		label.setStyle("-fx-background-color: cornsilk;"+
						"-fx-padding: 10;"+
						"-fx-border-color: black;" +
						"-fx-border-width: 5;"+
						"-fx-font-size: 16;");
		popup.getContent().add(label);
		return popup;
	}
	/* For key event */
	public void keyPressHandle(KeyEvent e){
		if (e.isControlDown() == true && e.getCode() == KeyCode.Z){
			System.out.println("preStep");
			doPreStep();
		}
		if(e.getCode() == KeyCode.W){
			//System.out.println(nowLayer.imageView.getTranslateY()+"\n"+((nowLayer.height)/2-height));
			Double y = nowLayer.imageView.getTranslateY();
			if(y>((nowLayer.height)-height)/2)
				nowLayer.imageView.setTranslateY(y-10);
		}
		if(e.getCode() == KeyCode.S){
			Double y = nowLayer.imageView.getTranslateY();
			if(y<(height-(nowLayer.height))/2)
				nowLayer.imageView.setTranslateY(y+10);
		}
		if(e.getCode()== KeyCode.A){
			Double x = nowLayer.imageView.getTranslateX();
			//System.out.println(x);
			if(x>((nowLayer.width)-width)/2)
				nowLayer.imageView.setTranslateX(x-10);
		}
		if(e.getCode()== KeyCode.D){
			Double x = nowLayer.imageView.getTranslateX();
			if(x<(width-(nowLayer.width))/2)
				nowLayer.imageView.setTranslateX(x+10);
		}
		
	}
	/* For text */
	public void NewText(){
		backToMenuButton.setDisable(false);
		textPane.setCursor(Cursor.TEXT);
		textToolBar.toFront();
		typeInField.setText("");
		textPane.setVisible(true);
		mode = typeText;

	}

	/* For PreStep process */
	public void doPreStep(){
		if (PreStepArrayList.isEmpty())
			return;
		preStep = PreStepArrayList.get(PreStepArrayList.size()-1);
		nowLayer = preStep.layer;
		nowImageView = nowLayer.imageView;
		nowButton = nowLayer.LayerButton;
		produceLayerButtonColor();
		switch (preStep.mode){
			case (1):	//change layer
				if (-preStep.Delta >= 1)
					for (int i = 0; i < -preStep.Delta; i++)
						SwapLayer(preStep.layer, 1);	//layer down
				else if (-preStep.Delta <= -1)
					for (int i = 0; i > -preStep.Delta; i--)
						SwapLayer(preStep.layer, -1);	//layer up
				break;
			case (2):	//delete layer
				number++;
				for (int index = preStep.layer.layerNumber; index < LayerArrayList.size(); index++){
					LayerArrayList.get(index).layerNumber++;
				} 
				LayerArrayList.add(preStep.layer.layerNumber, preStep.layer);
				stackPane.getChildren().add(preStep.layer.layerNumber, preStep.layer.imageView);
				Layer_Vbox.getChildren().add(preStep.indexOfButton, preStep.layer.LayerButton);
				deleteLayerButton.setDisable(false);
				break;
			case (3):	//rename layer
				preStep.layer.setName(preStep.oldName);
				break;
			case (4):	//new layer
				isPressButtonDelete = false;
				LayerDelete();
				break;
			case (5):	//paint
				nowLayer.BfImage = preStep.OldBfImage;
				nowLayer.refreshImageView();
				break;
		}
		PreStepArrayList.remove(preStep);
	}
	/* For change tool bar to main menu */
	public void backToMenu(){
		if (mode == notTypeOver)
			textOver();
		backToMenuButton.setDisable(true);
		stackPane.setCursor(Cursor.DEFAULT);
		textPane.setCursor(Cursor.DEFAULT);
		textPane.setVisible(false);
		toolBar.toFront();
		mode = draw;
	}
	/* For text */
	public void textOver(){
		if (typeInField.getText().length() < 1)
			return;
		newlayer();    
		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage writableImage = textPane.snapshot(parameters, null);
		nowLayer.totalView.setImage(writableImage);	// store the whole image view
		double x = (textPane.getWidth()/2) - (width/2);
		double y = (textPane.getHeight()/2) - (height/2);
		Rectangle2D croppedPortion = new Rectangle2D(x, y, width, height);
		nowLayer.totalView.setViewport(croppedPortion);
		writableImage = nowLayer.totalView.snapshot(parameters, null);   
		nowLayer.BfImage = SwingFXUtils.fromFXImage(writableImage, null); // store the painter view to Bfimage
		nowLayer.refreshImageView();	// refresh the painter view
	}
	public void boldRadioSelected(ActionEvent e) { 
		isBold = boldRadioButton.isSelected();
		font = font.font(fontName, isBold == true ? FontWeight.BOLD : FontWeight.NORMAL, isItalic == true ? FontPosture.ITALIC : FontPosture.REGULAR, (double)fontSize);
		typeInField.setFont(font);
	} 
	public void italicRadioSelected(ActionEvent e) { 
		isItalic = italicRadioButton.isSelected();
		font = font.font(fontName, isBold == true ? FontWeight.BOLD : FontWeight.NORMAL, isItalic == true ? FontPosture.ITALIC : FontPosture.REGULAR, (double)fontSize);
		typeInField.setFont(font);
	} 
}