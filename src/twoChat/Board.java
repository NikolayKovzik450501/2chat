package twoChat;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Board {

    private ArrayList<Thread> threads;
    private String name;
    private byte id;

    Board(byte id, String name) {
        threads = new ArrayList<>();
        this.name = name;
        this.id = id;
    }

    String getName() {
        return name;
    }

    byte getId() {
        return id;
    }

    public ArrayList<Thread> getThreads() {
        return threads;
    }

}
