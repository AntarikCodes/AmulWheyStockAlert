package main;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TelegramAlert {
    private static final String BOT_TOKEN =ConfigReader.get("telegram.bot.token"); // from BotFather
    private static final String CHAT_ID = ConfigReader.get("telegram.chat.id");


    public static void sendMessage(String messageText) {
        try {
            String encodedMessage = URLEncoder.encode(messageText, "UTF-8");
            String urlString = "https://api.telegram.org/bot" + BOT_TOKEN +
                    "/sendMessage?chat_id=" + CHAT_ID +
                    "&text=" + encodedMessage;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream response = conn.getInputStream();
            response.close();
        } catch (IOException e) {
            System.out.println(" Failed to send Telegram message.");
            e.printStackTrace();
        }
    }
}
