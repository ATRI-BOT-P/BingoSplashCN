package cn.bingosplash.messagemanagers;

public final class MessageManager {
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
