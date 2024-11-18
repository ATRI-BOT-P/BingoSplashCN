package cn.bingosplash.updater;

import cn.bingosplash.loggers.BSLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckUpdate {
    public static boolean alreadyCheck = false;


    public static String StartCheck() {
        BSLogger.info("Start check update");
        try {
            // 不获取 pre release
            URL url = new URL("https://api.github.com/repos/ATRI-BOT-P/BingoSplashCN/releases/latest");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // 解析出tag_name
                return response.toString().split("\"tag_name\":\"")[1].split("\"")[0];
            } else {
                BSLogger.severe("GitHub API return worng status code: " + connection.getResponseCode());
                return null;
            }
        } catch (Exception e) {
            BSLogger.severe("CheckUpdate catch: " + e.getMessage());
            return null;
        }
    }
}
