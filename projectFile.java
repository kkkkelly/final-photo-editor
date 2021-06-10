import java.io.IOException;
/* for read */
import java.io.BufferedReader;
import java.io.FileReader;
/* for write */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.File;
/* for debug */
import java.util.Arrays;

public class projectFile {
	public static ArrayList<Layer> read(File file){
		ArrayList<Layer> LayerArrayList = new ArrayList<Layer>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			/* check if it is a project txt*/
			if (file.getName().equals(br.readLine()) == false)
				return null;
			/* read layer message */
			String line;
			while ((line = br.readLine()) != null){
				/* read layer name,width,height */
				String[] temp = line.split(",");
				Layer layer = new Layer (temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
				LayerArrayList.add(layer);
				/* layerNumber */
				layer.layerNumber = Integer.parseInt(br.readLine());
				/* read visible */
				layer.visible = (Integer.parseInt(br.readLine()) == 1);
				/* read Style1 Slider value*/
				line = br.readLine();
				temp = line.split(",");
				layer.bright = Double.parseDouble(temp[0]);
				layer.hue_value = Double.parseDouble(temp[1]);
				layer.Opacity_value = Double.parseDouble(temp[2]);
				layer.saturation_value = Double.parseDouble(temp[3]);
				layer.reflect_value = Double.parseDouble(temp[4]);
				/* [][][]data of bufferedImage */
				int [][][] data = new int [layer.height][layer.width][3];
				for (int y = 0; y < layer.height; y++) {
					line = br.readLine();
					String[] tempY = line.split(",");
					for (int x = 0; x < layer.width; x++){
						String[] tempX = tempY[x].split(" ");
						for (int i = 0; i < 3; i++)
							data[y][x][i] = Integer.parseInt(tempX[i]);
						//System.out.println (Arrays.toString(data[y][x]));
					}
				}
				layer.setBfImageByData(data);
				layer.refreshImageView();
			}
        }catch (IOException e) {
            System.out.println("fail input txt");
		}
		return LayerArrayList;
	}

	public static void write(ArrayList<Layer> LayerArrayList , File file) throws IOException{
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(file));
		/* file name */
		outputWriter.write(file.getName());
		outputWriter.newLine();
		for (Layer layer:LayerArrayList){
			/* layer name,width,height */
			outputWriter.write(layer.Layer_name+","+layer.width+","+layer.height+",");
			outputWriter.newLine();
			/* layerNumber */
			System.out.println(layer.layerNumber);
			outputWriter.write(Integer.toString(layer.layerNumber));
			outputWriter.newLine();
			/* visible */
			outputWriter.write(Integer.toString(layer.visible? 1:0));
			outputWriter.newLine();
			/* Style1 Slider value*/
			outputWriter.write(layer.bright+","+layer.hue_value+","+layer.Opacity_value+","+layer.saturation_value+","+layer.reflect_value+",");
			outputWriter.newLine();
			/* [][][]data of bufferedImage */
			int [][][] data = layer.getdata();
			for (int y = 0; y < layer.height; y++) {
				for (int x = 0; x < layer.width; x++)
					outputWriter.write(data[y][x][0]+" "+data[y][x][1]+" "+data[y][x][2]+",");
				outputWriter.newLine();
			}
		}
		outputWriter.flush();  
		outputWriter.close();
	}
}