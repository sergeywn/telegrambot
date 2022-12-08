package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    static public BufferedImage getImage(String path) throws IOException {
        final File file = new File(path);
        return ImageIO.read(file);
    }

    static public void saveImage(BufferedImage image, String path) throws IOException {
        ImageIO.write(image, "png", new File(path));
    }

    //    Ниже методы нужны, чтобы int переводить в цвета. rgbIntToArray - для перевода int to array. arrayToRgbInt - обратная операция.
    static float[] rgbIntToArray(int pixel) {
//        new Color(pixel) перевод pixel в конкретный цвет; color.getRGBColorComponents получение rgb  в виде массива.
        Color color = new Color(pixel);
        return color.getRGBColorComponents(null);
    }

    static int arrayToRgbInt(float[] pixel) throws Exception {
//   Здесь пишем логику, т.к. в данном классе нет нужного конструктора, который может передать массив.
        Color color = null;
        if (pixel.length == 3) {
            color = new Color(pixel[0], pixel[1], pixel[2]);
        } else if (pixel.length == 4) {
            color = new Color(pixel[0], pixel[1], pixel[2], pixel[3]);
        }
        if (color != null) {
            return color.getRGB();
        }
        throw new Exception("Invalide color");
    }
}
