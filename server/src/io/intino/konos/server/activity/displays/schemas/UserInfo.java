package io.intino.konos.server.activity.displays.schemas;

public class UserInfo implements java.io.Serializable {

    private String fullName = "";
    private String photo = "";

    public String fullName() {
        return this.fullName;
    }

    public String photo() {
        return this.photo;
    }

    public UserInfo fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public UserInfo photo(String photo) {
        this.photo = photo;
        return this;
    }
}
