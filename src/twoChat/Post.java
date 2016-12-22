package twoChat;

public class Post {

    private String header, body;

    Post(String header, String body) {
        this.header = header;
        this.body = body;
    }

    String getHeader() {
        return header;
    }

    String getBody() {
        return body;
    }

}
