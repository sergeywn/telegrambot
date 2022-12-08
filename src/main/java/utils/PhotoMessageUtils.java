package utils;

import functions.FilterOperation;
import functions.ImageOperation;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PhotoMessageUtils {
    public static List<String> savePhotos(List<File> files, String botToken) throws IOException {
        Random random = new Random();
        ArrayList<String> paths = new ArrayList<>();
        for (File file: files) {
//      Получение url адреса к нашему фото.
            final String imageUrl = "https://api.telegram.org/file/bot" + botToken + "/" + file.getFilePath();
//    Создаём случайные имена файлов состоящие из времени получения+случайного числа
            final String localFileName = "images/"+ new Date().getTime() + random.nextLong() + ".jpeg";
            saveImage(imageUrl, localFileName);
//      Добавляем все наши скаченные фото с вписок для дальнейшей обработки.
            paths.add(localFileName);
        }
        return paths;
    }

    //      класс для сохранения фото по url
    public static void saveImage(String url, String fileName) throws IOException {
        URL urlModel = new URL(url);
        InputStream inputStream = urlModel.openStream();
        OutputStream outputStream = new FileOutputStream(fileName);
//      передача данных из inputStream в outputStream
        byte[] b = new byte[2048];
        int lenght;
        while ((lenght = inputStream.read(b)) != -1) {
            outputStream.write(b,0,lenght);
        }
        inputStream.close();
        outputStream.close();
    }

    //  Обработка фото с помощью лямбд (прошлый проект из уроков)
    public static void processingImage(String fileName, ImageOperation operation) throws Exception {
        final BufferedImage image = ImageUtils.getImage(fileName);
        final RgbMaster rgbMaster = new RgbMaster(image);
        rgbMaster.changeImage(operation);
        ImageUtils.saveImage(image, fileName);
    }
}
