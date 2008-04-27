package org.makumba.parade.model;

public class RowWebapp extends AbstractRowData implements RowData {

    private Long id;

    private String contextname;

    private String webappPath;

    private Integer status;

    public String getContextname() {
        return contextname;
    }

    public void setContextname(String name) {
        this.contextname = name;
    }

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

    public String getWebappPath() {
        return webappPath;
    }

    public void setWebappPath(String webappPath) {
        this.webappPath = webappPath;
    }

}
