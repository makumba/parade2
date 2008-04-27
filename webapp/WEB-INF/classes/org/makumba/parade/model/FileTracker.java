package org.makumba.parade.model;

public class FileTracker extends AbstractFileData {

    private Long id;

    private boolean tracked;

    // short description which has will appear in the browser
    private String title;

    private String description;

    private String author;

    // "Specification"=0,"DesignApproved"=1,"Started"=2,"Developing"=3,"Stable"=4,"Frozen"=5,"Dropped"=6
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isTracked() {
        return tracked;
    }

    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }

}
