import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
//        Различия во взаимодействии между нашим сервером и сервером телеграмма.
//        1) TelegramLongPollingBot - мы обращаемся к серверу телеграмму и он отвечает (более современный).
//        2) WebHookBot - телеграм обращаемся к нашему серверу (изначальный).

//    DefaultBotSession-отсутвует действующая сессия, которую можно продолжить.
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        BotSession botSession = api.registerBot(new Bot.MyTelegramBot());
    }
}
