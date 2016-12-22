package twoChat;

import java.util.ArrayList;

public class Thread {

    private ArrayList<Post> posts;

    private Thread(Post initialPost) {
        posts = new ArrayList<>();
        posts.add(initialPost);
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

}
