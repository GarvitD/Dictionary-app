package com.garvitd.dictionaryapp;

public class HistoryModel {
    public String userId;
    public String historyWord;

    public HistoryModel(){

    }

    public HistoryModel(String userId, String historyWord) {
        this.userId = userId;
        this.historyWord = historyWord;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHistoryWord() {
        return historyWord;
    }

    public void setHistoryWord(String historyWord) {
        this.historyWord = historyWord;
    }
}
