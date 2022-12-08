package functions;

import java.util.Random;

public class FilterOperation {
//    Делаем static для того, чтобы можно было использовать без создания экземпляра данного класса. Это будет по сути хранилище для лямбд (фильтров).
    public static float[] greyScale(float[] rgb) {
        final float mean = (rgb[0] + rgb[1] + rgb[2]) / 3;
        rgb[0] = mean;
        rgb[1] = mean;
        rgb[2] = mean;
        return  rgb;
    }

    public static float[] onlyRed(float[] rgb) {
        rgb[1] = 0;
        rgb[2] = 0;
        return  rgb;
    }

    public static float[] onlyGreen(float[] rgb) {
        rgb[0] = 0;
        rgb[2] = 0;
        return  rgb;
    }

    public static float[] onlyBlue(float[] rgb) {
        rgb[0] = 0;
        rgb[1] = 0;
        return  rgb;
    }

    public static float[] sepia (float[] rgb) {
        Random random = new Random();
        final float randomValue = random.nextFloat()*50/255;
        rgb[0] += randomValue;
        rgb[1] += randomValue;
        rgb[2] += randomValue;
//        for делаем чтобы не выйти за массив из=за белого цвета, вылетели за 255 бит.
        for (int i = 0; i < rgb.length; i++) {
            if (rgb[i]>1) {
                rgb[i]=1;
            }
        }
        return  rgb;
    }
}
