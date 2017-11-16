package com.dc.moments.bean;

import java.util.List;

/**
 * Created by chenzhiwei on 17/11/17.
 */

public class Moments {
    /**
     * content : 沙发！
     * images : [{"url":"https://encrypted-tbn1.gstatic
     * .com/images?q=tbn:ANd9GcRDy7HZaHxn15wWj6pXE4uMKAqHTC_uBgBlIzeeQSj2QaGgUzUmHg"},
     * {"url":"https://encrypted-tbn1.gstatic
     * .com/images?q=tbn:ANd9GcTlJRALAf-76JPOLohBKzBg8Ab4Q5pWeQhF5igSfBflE_UYbqu7"},
     * {"url":"http://i.ytimg.com/vi/rGWI7mjmnNk/hqdefault.jpg"}]
     * sender : {"username":"jport","nick":"Joe Portman","avatar":"https://encrypted-tbn3.gstatic
     * .com/images?q=tbn:ANd9GcRJm8UXZ0mYtjv1a48RKkFkdyd4kOWLJB0o_l7GuTS8-q8VF64w"}
     * comments : [{"content":"Good.","sender":{"username":"outman","nick":"Super hero",
     * "avatar":"https://encrypted-tbn3.gstatic
     * .com/images?q=tbn:ANd9GcRJm8UXZ0mYtjv1a48RKkFkdyd4kOWLJB0o_l7GuTS8-q8VF64w"}},
     * {"content":"Like it too","sender":{"username":"inman","nick":"Doggy Over",
     * "avatar":"https://encrypted-tbn3.gstatic
     * .com/images?q=tbn:ANd9GcRJm8UXZ0mYtjv1a48RKkFkdyd4kOWLJB0o_l7GuTS8-q8VF64w"}}]
     */

    private String content;
    /**
     * username : jport
     * nick : Joe Portman
     * avatar : https://encrypted-tbn3.gstatic
     * .com/images?q=tbn:ANd9GcRJm8UXZ0mYtjv1a48RKkFkdyd4kOWLJB0o_l7GuTS8-q8VF64w
     */

    private SenderEntity sender;
    /**
     * url : https://encrypted-tbn1.gstatic
     * .com/images?q=tbn:ANd9GcRDy7HZaHxn15wWj6pXE4uMKAqHTC_uBgBlIzeeQSj2QaGgUzUmHg
     */

    private List<ImagesEntity> images;
    /**
     * content : Good.
     * sender : {"username":"outman","nick":"Super hero","avatar":"https://encrypted-tbn3.gstatic
     * .com/images?q=tbn:ANd9GcRJm8UXZ0mYtjv1a48RKkFkdyd4kOWLJB0o_l7GuTS8-q8VF64w"}
     */

    private List<CommentsEntity> comments;

    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(SenderEntity sender) {
        this.sender = sender;
    }

    public void setImages(List<ImagesEntity> images) {
        this.images = images;
    }

    public void setComments(List<CommentsEntity> comments) {
        this.comments = comments;
    }

    public String getContent() {
        return content;
    }

    public SenderEntity getSender() {
        return sender;
    }

    public List<ImagesEntity> getImages() {
        return images;
    }

    public List<CommentsEntity> getComments() {
        return comments;
    }

    public static class SenderEntity {
        private String username;
        private String nick;
        private String avatar;

        public void setUsername(String username) {
            this.username = username;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUsername() {
            return username;
        }

        public String getNick() {
            return nick;
        }

        public String getAvatar() {
            return avatar;
        }
    }

    public static class ImagesEntity {
        private String url;

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class CommentsEntity {
        private String content;
        /**
         * username : outman
         * nick : Super hero
         * avatar : https://encrypted-tbn3.gstatic
         * .com/images?q=tbn:ANd9GcRJm8UXZ0mYtjv1a48RKkFkdyd4kOWLJB0o_l7GuTS8-q8VF64w
         */

        private SenderEntity sender;

        public void setContent(String content) {
            this.content = content;
        }

        public void setSender(SenderEntity sender) {
            this.sender = sender;
        }

        public String getContent() {
            return content;
        }

        public SenderEntity getSender() {
            return sender;
        }

        public static class SenderEntity {
            private String username;
            private String nick;
            private String avatar;

            public void setUsername(String username) {
                this.username = username;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getUsername() {
                return username;
            }

            public String getNick() {
                return nick;
            }

            public String getAvatar() {
                return avatar;
            }
        }
    }
}
