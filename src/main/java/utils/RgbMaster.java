package utils;

import functions.ImageOperation;

import java.awt.image.BufferedImage;
//  В данный класс мы запихиваем фото, определенный образом работаем с ним и при необходимости получаем его обратно.
public class RgbMaster {

    public RgbMaster(BufferedImage image) {
        this.image=image;
        width = image.getWidth();
        height = image.getHeight();
        hasAlphaChannel = image.getAlphaRaster() != null;
        pixels = image.getRGB(0,0,width, height,null, 0,width);
    }

    private BufferedImage image;
    private int width;
    private int height;
    private boolean hasAlphaChannel;
    private int[] pixels;

//    Метод getImage создаём для получения изображения. Чтобы нельзя было это изображ-ие изменить из вне, только методы, которым мы будем передавать лямбды.
    public BufferedImage getImage() {
        return  image;
    }

    public void changeImage(ImageOperation operation) throws Exception {
        for (int i = 0; i < pixels.length; i++) {
            float[] pixel = ImageUtils.rgbIntToArray(pixels[i]);
//   operation.execute(pixel) - заберет массив float и вернет массив float, но pixel пройдёт необходимую нам обработку.
            float[] newpixel = operation.execute(pixel);
            pixels[i] = ImageUtils.arrayToRgbInt(newpixel);
        }
        final int type = hasAlphaChannel ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        image.setRGB(0,0,width, height,pixels, 0,width);
    }
}
