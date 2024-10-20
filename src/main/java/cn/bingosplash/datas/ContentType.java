package cn.bingosplash.datas;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ContentType {
    public Integer Status;
    public String Type;
    public String Content;

    public ContentType(String json) {
        JsonObject gson = new Gson().fromJson(json, JsonElement.class).getAsJsonObject();
        Status = gson.get("status").getAsInt();
        Type = gson.get("type").getAsString();
        Content = gson.get("content").getAsString();
    }
}
