package cn.bingosplash.updater;

import cn.bingosplash.loggers.BSLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckUpdate {
    public static boolean alreadyCheck = false;

    public static String CheckUpdate() {
        BSLogger.info("开始检查更新");
        try {
            URL url = new URL("https://api.github.com/repos/Sn0wo2/BingoSplashCN/releases/latest");
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

                // 解析JSON以提取最新的tag
                String jsonResponse = response.toString();
                return jsonResponse.split("\"tag_name\":\"")[1].split("\"")[0];
            } else {
                BSLogger.severe("CheckUpdate错误的状态码: " + connection.getResponseCode());
                return null;
            }
        } catch (Exception e) {
            BSLogger.severe("CheckUpdate出现错误: " + e.getMessage());
            return null;
        }
    }
}
