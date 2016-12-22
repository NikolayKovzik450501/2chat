package twoChat;

import java.util.ArrayList;

public class Thread {

    private ArrayList<Post> posts;

    private byte id;

    private String header;

    Thread(Post initialPost, byte id) {
        this.id = id;
        posts = new ArrayList<>();
        posts.add(initialPost);
        header = initialPost.getHeader();
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public byte getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

}
