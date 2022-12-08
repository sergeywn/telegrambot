import functions.FilterOperation;
import functions.ImageOperation;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.PhotoMessageUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Bot {

//    Пользовательский выбор фильтра обработки фото: 1) к фото делать комментарий(приписку) / 2) через предложенные кнопки (выбрать фильтр)

    public static class MyTelegramBot extends TelegramLongPollingBot {

        //        1-й метод: возвращает имя бота (по сути id бота)
        @Override
        public String getBotUsername() {
            return "javatestbot2217and0219bot";
        }

        //        2-й метод: хранение и передача токена для телеграма (идентификация для телеграмма, что всё корректно).
        @Override
        public String getBotToken() {
            return "5585607378:AAEQElA4qeMJI9a43jTdxlvrXmVk6eAZJdA";
        }

        //        3-й метод: по сути главный метод для реализации бота (здесь происходят ответы на запрос к нашему боту).
        @Override
        public void onUpdateReceived(Update update) {
//      Принимаем в консоли полученный текст от пользователя
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            try {
                SendMediaGroup responseMediaMessage = runPhotoFilter(message);
                if (responseMediaMessage != null) {
                    execute(responseMediaMessage);
                    return;
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

//        получение файлов из сообщений
        private List<File> getFilesByMessage(Message message) {
//    Принимаем от user'а все полученные фото
            List<PhotoSize> photoSizes = message.getPhoto();
            ArrayList<org.telegram.telegrambots.meta.api.objects.File> files = new ArrayList<>();
//    Получаем не само фото, а его id, для того, чтобы не загружать ресурсы бота (особенно в случаях если фото множество)
            for (PhotoSize photoSize : photoSizes) {
                final String fileId = photoSize.getFileId();
                try {
//    org.telegram.telegrambots.meta.api.objects.File-указание, что класс File именно из библиотеки telegram, а не из java
                    files.add(sendApiMethod(new GetFile(fileId)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            return files;
        }

//      Отправляем ответ user'у (работа с несколькими фото)
        private SendMediaGroup runPhotoFilter(Message message) {
            ImageOperation operation = FilterOperation::greyScale;
            List<File> files = getFilesByMessage(message);
            try {
                List<String> paths = PhotoMessageUtils.savePhotos(files,getBotToken());
                String chatId = message.getChatId().toString();
                return  preparePhotoMessage(paths, operation, chatId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
////      Отправляем ответ user'у (работа с несколькими фото)
        private SendMediaGroup preparePhotoMessage(List<String> localPaths, ImageOperation operation, String chatId) throws Exception {
            SendMediaGroup mediaGroup = new SendMediaGroup();
            ArrayList<InputMedia> medias = new ArrayList<>();
            for (String path: localPaths) {
                InputMedia inputMedia = new InputMediaPhoto();

                PhotoMessageUtils.processingImage(path, operation);
                inputMedia.setMedia(new java.io.File(path),"path");
                medias.add(inputMedia);
            }
            mediaGroup.setMedias(medias);
            mediaGroup.setChatId(chatId);
            return mediaGroup;
        }

//        Типы кнопок в телеграмме: inline кнопки - кнопки в части сообщения; keyboard в телеграм (обычная клавиатура)
        private ReplyKeyboardMarkup getKeyboard(Class someClass) {
//      создаем виртуальную клавиатуру - ReplyKeyboardMarkup
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//      создаем строки и ряды кнопок
            ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
            Method[] methods = someClass.getMethods();
            int columnCount=3;
            int rowsCount = methods.length/columnCount + ((methods.length/columnCount !=0) ? 0 : 1);
            for (int rowIndex = 0; rowIndex < rowsCount; rowIndex++) {
                KeyboardRow row = new KeyboardRow();
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    int index = rowIndex*columnCount+columnIndex;
                    if (index>=methods.length) continue;
                    Method method = methods[rowIndex*columnCount+columnIndex];
                    KeyboardButton keyboardButton = new KeyboardButton(method.getName());
                    row.add(keyboardButton);
                }
                keyboardRows.add(row);
            }
//      добавляем наши строки и ряды кнопок в клавиатуру
            replyKeyboardMarkup.setKeyboard(keyboardRows);
//      указываем, что кнопки будут только в одном нашем сообщении (если поставить false, то будут отображаться всегда)
            replyKeyboardMarkup.setOneTimeKeyboard(true);
            return replyKeyboardMarkup;
        }
    }
}

////      Из предыдущего выполнения
//    //        3-й метод: по сути главный метод для реализации бота (здесь происходят ответы на запрос к нашему боту).
//    @Override
//    public void onUpdateReceived(Update update) {
////      Принимаем в консоли полученный текст от пользователя
//        Message message = update.getMessage();
//        String chatId = message.getChatId().toString();
//        try {
////      PhotoMessageUtils.savePhotos - сохранение фото по url
//            ArrayList<String> photoPaths = new ArrayList<>(PhotoMessageUtils.savePhotos(getFilesByMessage(message), getBotToken()));
//            for (String Path : photoPaths) {
////      Обработка полученного фото
//                PhotoMessageUtils.processingImage(Path);
////      Отправляем в ответ фото
//                execute(preparePhotoMessage(Path, chatId));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

////      Отправляем в ответ фото
//        private SendPhoto preparePhotoMessage(String localPath, String chatId) {
////      создаём сам объект отправки сообщения (любого типа)
//            SendPhoto sendPhoto = new SendPhoto();
////      добавляем клавиатуру
//            sendPhoto.setReplyMarkup(getKeyboard(FilterOperation.class));
////      указали необходимый chatId
//            sendPhoto.setChatId(chatId);
////      создали файл
//            InputFile newFile = new InputFile();
//            newFile.setMedia(new java.io.File(localPath));
////      прикрепили файл к сообщению
//            sendPhoto.setPhoto(newFile);
//            return sendPhoto;
//        }

//            System.out.println(message.getText());
////      Обработка полученного фото
//            try {
//                processingImage(localFileName);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
////      Отправляем наше ответное сообщение на полученное сообщение от user'а(message.getChatId())
//            SendMessage sendMessage = new SendMessage();
//            sendMessage.setChatId(message.getChatId());
////      Отправляем в ответ фото с подписью.
//            SendPhoto sendPhoto = new SendPhoto();
//            sendPhoto.setChatId(message.getChatId());
//            InputFile newFile = new InputFile();
//            newFile.setMedia(new File(localFileName));
//            sendPhoto.setPhoto(newFile);
//            sendPhoto.setCaption("Обработанное фото");
//////      Отправляем в ответ тоже самое, что прислал нам user
////            sendMessage.setText("Ты прислала мне это: " + message.getText() + " .А я вот, что тебе отвечу:");
//            try {
////                execute(sendMessage);
//                execute(sendPhoto);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }