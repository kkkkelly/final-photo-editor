import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;

class PreStep{
    /* mode */
    int     mode=-1;
    /* 
        mode expression:
        change layer = 1
        delete layer = 2
        rename layer = 3
        new    layer = 4
        paint        = 5
    */

    Layer           layer;
    int             Delta = 0;   //change layer A in delta
    int             indexOfButton;
    String          oldName;
	BufferedImage	OldBfImage;
    public PreStep(Layer x){layer=x;}
    public void setBfImage(Image image){OldBfImage = SwingFXUtils.fromFXImage(image, null);}

}