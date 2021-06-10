import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.SepiaTone;
import javafx.scene.effect.*;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.SnapshotResult;
import javafx.scene.SnapshotParameters;
import javafx.scene.paint.Color;

class Layer{
	/* Style1 Slider value*/
	Double 			bright = 0.0;
	Double			hue_value = 0.0;
	Double			Opacity_value = 1.0;
	Double			saturation_value  = 0.0;
	Double			reflect_value = 0.0;
	/* Layer base data */
	int				height, width;
	int				layerNumber = 0;
	boolean			visible = true;
	String			Layer_name = null;
	BufferedImage	BfImage;
	ImageView		imageView = new ImageView();	// for show only painter range view
	ImageView		totalView = new ImageView();	// the whole view
	Button			LayerButton = new Button();
	/*imageView effect*/
	SepiaTone 	sepiaEffect = new SepiaTone();
	ColorAdjust colorAdjust = new ColorAdjust();
	Reflection  reflection  = new Reflection();
	

	public Layer(String name, BufferedImage img, int w, int h){
		sepiaEffect.setLevel(0.0);
		reflection.setFraction(0.0);
		width = w;
		height = h;
		bright = 0.0;
		hue_value = 0.0;
		//opacity = 1.0;
		BfImage = img;
		imageView.setImage(getImage());
		colorAdjust.setInput(sepiaEffect);
		reflection.setInput(colorAdjust);
		imageView.setEffect(reflection);
		
		setName(name);
	}
	
	public Layer (String name, int w, int h){	//create a transparent layer
		sepiaEffect.setLevel(0.0);
		reflection.setFraction(0.0);
		width = w;
		height = h;
		BfImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		imageView.setImage(getImage());
		colorAdjust.setInput(sepiaEffect);
		reflection.setInput(colorAdjust);
		imageView.setEffect(reflection);
		refreshImageView();
		setName(name);
	}
	
	public void setName(String name){Layer_name = name;	LayerButton.setText(Layer_name);}
	public String getName(){return Layer_name;}
	public WritableImage getImage(){	//convert BfImage into image
		WritableImage image = SwingFXUtils.toFXImage(BfImage, null);
		return image;
	}
	public void refreshImageView(){
		imageView.setImage(getImage());	//convert BfImage into ImageView
		imageView.setEffect(reflection);
	}
	public void resizeStackPane(int w, int h) {
		BufferedImage  newBfImage = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);	// create a new BfImage
		Graphics2D g2d = newBfImage.createGraphics();
		g2d.drawImage(BfImage, null, 0, 0);	// past BfImage to new BfImage
		g2d.dispose();
		width = w;
		height = h;
		BfImage = newBfImage;
		refreshImageView();
	}
	public void setSize(int h,int w){
		imageView.setFitHeight(h);
		imageView.setFitWidth(w);
		width = w;
		height = h;
		//BfImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		refreshImageView();
	}
	public void setrotateSize(int h,int w){
		width = w;
		height = h;
		refreshImageView();
	}
	public void setBright(double d){
		colorAdjust.setBrightness(d); 
		bright = d;
		colorAdjust.setInput(sepiaEffect);
		refreshImageView();
	}
	public void setOpacityvalue(double d){
		Opacity_value = d;
	}
	public double getBright(){
		return bright;
	}
	public void set_Saturation(double d){
		colorAdjust.setSaturation(d); 
		colorAdjust.setInput(sepiaEffect);
		saturation_value = d;
		refreshImageView();	
	}
	public void set_Hue(double d){
		colorAdjust.setHue(d); 
		colorAdjust.setInput(sepiaEffect);
		refreshImageView();
		hue_value = d;
	}
	public double getHue(){
		return hue_value;
	}
	
	public void set_Reflect(double d){
		//Reflection reflection = new Reflection();
        reflection.setFraction(d);
		reflect_value = d;
		reflection.setInput(colorAdjust);
		refreshImageView();
	}
	/* set BfImage from int[][][] data */
	public void setBfImageByData(int [][][] data){
		BfImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int [] rgb = data[j][i];
				if (rgb[0] == -1){	//is transparent
					BfImage.setRGB(i, j, Transparency.TRANSLUCENT);
				}
				else
					BfImage.setRGB(i, j, Utils.getRGB(rgb[0], rgb[1], rgb[2]));
			}
		}
	}
	/* get int[][][] data from BfImage */
	public int[][][] getdata(){
		height = BfImage.getHeight();
		width = BfImage.getWidth();
		int[][][] data = new int[height][width][3];
		
		for (int x = 0; x < width; x++)				//store image's RGB data
			for (int y = 0; y < height; y++) {
				int rgb = BfImage.getRGB(x, y);
				if (rgb == 0 || rgb == 3){	//is transparent || Transparency.TRANSLUCENT
					data[y][x][0] = -1;
					data[y][x][1] = -1;
					data[y][x][2] = -1;
				}
				else{
					data[y][x][0] = Utils.getR(rgb);
					data[y][x][1] = Utils.getG(rgb);
					data[y][x][2] = Utils.getB(rgb);
				}
			}
		return data;
	}
}