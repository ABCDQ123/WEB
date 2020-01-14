package com.comic.mario.ui.bean;

import java.io.Serializable;

public class ComicEpisodeBean implements Serializable {

    private String link;
    private String episode;
    private String histEpisode;
    private int histIndex;
    private boolean hist;

    public boolean isHist() {
        return hist;
    }

    public void setHist(boolean hist) {
        this.hist = hist;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getHistEpisode() {
        return histEpisode;
    }

    public void setHistEpisode(String histEpisode) {
        this.histEpisode = histEpisode;
    }

    public int getHistIndex() {
        return histIndex;
    }

    public void setHistIndex(int histIndex) {
        this.histIndex = histIndex;
    }
}
