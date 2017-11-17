package com.dc.moments.bean;

/**
 * Created by chenzhiwei on 17/11/17.
 */

public class UserInfo {
    /**
     * profile_image :
     * avatar : http://info.thoughtworks.com/rs/thoughtworks2/images/glyph_badge.png
     * nick : John Smith
     * username : jsmith
     */

    private String profile_image;
    private String avatar;
    private String nick;
    private String username;

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNick() {
        return nick;
    }

    public String getUsername() {
        return username;
    }
}
