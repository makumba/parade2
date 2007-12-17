package org.makumba.parade.view;

public class TickerTapeData {
    
    private String linkText;
    
    private String url;
    
    private String title;
    
    public TickerTapeData(String linkText, String url, String title) {
        this.linkText = linkText;
        this.url = url;
        this.title = title;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}
