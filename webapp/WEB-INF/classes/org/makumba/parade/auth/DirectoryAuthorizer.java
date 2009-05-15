package org.makumba.parade.auth;


public interface DirectoryAuthorizer extends Authorizer {
    
    public String getDisplayName();

    public String getGivenName();

    public String getEmployeeType();

    public String getSn();

    public String getMail();

    public String getCn();

    public byte[] getJpegPhoto();


}
