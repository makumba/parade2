package org.makumba.parade.model;

public class User {

    private long id;
    
    private String login;
    
    private String name;
    
    private String surname;
    
    private String nickname;
    
    private String email;
    
    private String PAptr;
    
    private Row row;

    private Parade parade;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPAptr() {
        return PAptr;
    }

    public void setPAptr(String aptr) {
        PAptr = aptr;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public User() {
        
    }
    
    public User(String login, String name, String surname, String nickname, String email) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.email = email;
    }
    
    public static User getUnknownUser() {
        return new User("unknown", "unknown", "unknown", "unknown", "unknown@unknown.com");
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Parade getParade() {
        return parade;
    }

    public void setParade(Parade parade) {
        this.parade = parade;
    }
    
}
