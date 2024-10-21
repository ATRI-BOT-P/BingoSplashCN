package cn.bingosplash.titlemanagers;

public final class TitleManager {
    private String lastMessage;

    public String getLastMessage() {
        synchronized (this) {
            return lastMessage;
        }
    }

    public void setLastMessage(String lastMessage) {
        synchronized (this) {
            this.lastMessage = lastMessage;
        }
    }
}
