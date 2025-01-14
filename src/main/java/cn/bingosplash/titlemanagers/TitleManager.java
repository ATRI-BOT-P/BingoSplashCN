package cn.bingosplash.titlemanagers;

public final class TitleManager {

    private String lastMessage;

    public String getLastMessage() {
        synchronized (this) {
            return lastMessage;
        }
    }

    // 实时渲染title, 设置null停止渲染
    public void setLastMessage(String lastMessage) {
        synchronized (this) {
            this.lastMessage = lastMessage;
        }
    }
}
