package org.makumba.parade.view.beans;

import org.makumba.Pointer;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.CVSManager;
import org.makumba.parade.tools.CVSRevisionComparator;

/**
 * Bean that provides data for the file browser view
 * 
 * @author Frederik Van Everbroeck
 * 
 */

public class CVSRevisionBean {
    
    /* Usage of CVSRevision Comparisation in fileBrowserCVS.jspf 
     * Inserted in place of the <c:set var="cvsNewerExists">false</c:set>
     *
    <%  CVSRevisionBean cvsRevisionBean = new CVSRevisionBean(); 
        cvsRevisionBean.setRowPath(rowpath);
        cvsRevisionBean.setApplicationId(applicationId); %>
        <c:set var="fileCvsPath"><% cvsRevisionBean.getFileCvsPath(relativeFilePath); %></c:set>
    <mak:object from="Application a" where="a.id = r.application.id AND a.filename = :fileCvsPath">
        <mak:value expr="a.name" printVar="appname" />
        <mak:value expr="a.fileversion" printVar="repositoryRevision" />
    <%  cvsRevisionBean.setApplicationName(appname); %>

    <c:set var="cvsNewerExists"><%=cvsRevisionBean.getCvsNewerExists(relativeFilePath, cvsRevision, repositoryRevision) %></c:set>
    <c:if test="${cvsNewerExists}">
      <c:set var="isCvsConflictOnUpdate"><%=cvsRevisionBean.isCvsConflictOnUpdate(fileName, absolutePath) %></c:set>
      <c:set var="cvsNewRevision"><%=cvsRevisionBean.getCvsNewRevision(relativeFilePath) %></c:set>
    </c:if>
    </mak:object>
    --%>
    */
    
    private CVSRevisionComparator c = new CVSRevisionComparator();
    
    private Pointer applicationId = null;
    
    private String applictionName = null;
        
    private String rowpath = null;
    
    private Row row = null;
   
    public void setApplicationId(Object value){
        this.applicationId = (Pointer)value;
    }
    
    public void setApplicationName(String value){
        this.applictionName = value;
    }
    
    public void setRowPath(String value){
        this.rowpath = value;
    }
    
    public String getFileCvsPath(String relativePath){
        return applictionName + relativePath.substring(rowpath.length());
    }
    
    public boolean getCvsNewerExists(String relativePath, String rowRevision, String repositoryRevision) {
        // let's see if there's a newer version of this on the repository
 
        boolean newerExists = false;

        if (applicationId != null) {

            if (repositoryRevision != null && rowRevision != null) {

                if (repositoryRevision.equals("1.1.1.1")) {
                    repositoryRevision = "1.1";
                }
                if (rowRevision.equals("1.1.1.1")) {
                    rowRevision = "1.1";
                }

                newerExists = c.compare(repositoryRevision, rowRevision) == 1;

            } else {
                return false;
            }
        }

        return newerExists;
        
    }
    
    public boolean isCvsConflictOnUpdate(String fileName, String absolutePath) {
        return CVSManager.cvsConflictOnUpdate(fileName, absolutePath);
    }
    
    public String getCvsNewRevision(String relativePath) {
        return row.getApplication().getCvsfiles().get(getFileCvsPath(relativePath));        
    }
    
}
