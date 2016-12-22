package twoChat;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Board {

    private ArrayList<Thread> threads;
    private String name;
    private int id;

    Board(int id, String name) {
        threads = new ArrayList<>();
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Thread> getThreads() {
        return threads;
    }

}
