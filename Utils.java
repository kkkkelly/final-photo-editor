public class Utils {
	public static int getR(int rgb) {return (rgb >> 16) & 0x000000FF;}
	public static int getG(int rgb) {return (rgb >>8 ) & 0x000000FF;}
	public static int getB(int rgb) {return (rgb) & 0x000000FF;}
	public static int getRGB(int r, int g, int b) {return 0xFF000000 | (r << 16) & 0x00FF0000 | (g << 8) & 0x0000FF00 | b & 0x000000FF;}

	public static int checkPixelBound(int s) {return s > 255 ? 255 : (s < 0 ? 0 : s);}
}