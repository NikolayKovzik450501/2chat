package twoChat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private ListView<String> generalList;

    @FXML
    private Button backButton, sendButton, refreshButton;

    @FXML
    private TextArea messageField;

    private enum MenuState {GLOB, BOARD, THREAD}

    private MenuState clientState;

    private byte currentBoard, currentThread;

    private int port;

    private String host;

    private final static byte ESC = (byte) 0xF0, MESS_ESC = (byte) 0xF1, GET = (byte) 0xF2,
            BOARDS = (byte) 0xF3, THREADS = (byte) 0xF4, POSTS = (byte) 0xF5, SET = (byte) 0xF6;

    private ArrayList<Board> boards;

    private ArrayList<Thread> threads;

    public void initialize(URL location, ResourceBundle resources) {
        this.clientState = MenuState.GLOB;
        this.currentBoard = -1;
        this.currentThread = -1;
        port = 5005;
    }

    @FXML
    void onRefresh() {
        switch (clientState) {
            case GLOB:
                refreshBoards();
                break;
            case BOARD:
                refreshThreads(currentBoard);
                break;
            case THREAD:
                refreshPosts(currentBoard, currentThread);
                break;
        }
    }

    @FXML
    void onBack() {
        switch (clientState) {
            case BOARD:
                printBoards();
                sendButton.setDisable(true);
                backButton.setDisable(true);
                messageField.setDisable(true);
                break;
            case THREAD:
                sendButton.setText("Create");
                printThreads();
                break;
        }
    }

    @FXML
    void onListClicked() {
        if (generalList.getSelectionModel().getSelectedItem() != null) {
            String item = generalList.getSelectionModel().getSelectedItem();
            switch (clientState) {
                case GLOB:
                    currentBoard = getBoardId(item);
                    refreshThreads(currentBoard);
                    sendButton.setText("Create");
                    backButton.setDisable(false);
                    sendButton.setDisable(false);
                    messageField.setDisable(false);
                    break;
                case BOARD:
                    currentThread = getThreadId(item);
                    sendButton.setText("Send");
                    refreshPosts(currentBoard, currentThread);
                    break;
            }
        }
    }

    @FXML
    void onSend() {
        if (clientState == MenuState.GLOB) {
            host = messageField.getText();
            refreshBoards();
            sendButton.setDisable(true);
            refreshButton.setDisable(false);
            backButton.setDisable(true);
            messageField.setDisable(true);
            return;
        }
        byte data[] = messageField.getText().getBytes(StandardCharsets.US_ASCII);
        try {
            Socket s = new Socket(host, port);
            switch (clientState) {
                case BOARD:
                    byte boardInstruction[] = {SET, THREADS, currentBoard};
                    s.getOutputStream().write(boardInstruction);
                    s.getOutputStream().write(data);
                    s.getOutputStream().write(ESC);
                    s.close();
                    break;
                case THREAD:
                    byte threadInstruction[] = {SET, POSTS, currentBoard, currentThread};
                    s.getOutputStream().write(threadInstruction);
                    s.getOutputStream().write(data);
                    s.getOutputStream().write(ESC);
                    s.close();
                    break;
            }

        } catch (Exception exception) {
            showErrorMessage("Board Refresh", exception.getMessage());
        }
    }

    private byte getBoardId(String name) {
        for (Board board : boards) {
            if (board.getName().equals(name)) {
                return board.getId();
            }
        }
        return -1;
    }

    private byte getThreadId(String name) {
        for (Thread thread : threads) {
            if (thread.getHeader().equals(name)) {
                return thread.getId();
            }
        }
        return -1;
    }

    private void printBoards() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Board board : boards) {
            items.add(board.getName());
        }
        generalList.setItems(items);
        clientState = MenuState.GLOB;
    }

    private void printThreads() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Thread thread : threads) {
            items.add(thread.getHeader());
        }
        generalList.setItems(items);
        clientState = MenuState.BOARD;
    }

    private void refreshBoards() {
        boards = new ArrayList<>();
        try {
            Socket s = new Socket(host, port);
            byte instruction[] = {GET, BOARDS, ESC};
            s.getOutputStream().write(instruction);
            byte buf[] = readMessage(s);
            s.close();
            int i = 0;
            while (buf[i] != ESC) {
                byte id = buf[i];
                i++;
                String name = "";
                while (buf[i] != MESS_ESC) {
                    name += (char)buf[i];
                    i++;
                }
                boards.add(new Board(id, name));
                i++;
                printBoards();
            }
        } catch (Exception exception) {
            showErrorMessage("Board Refresh", exception.getMessage());
        }
    }

    private void refreshThreads(byte boardId) {
        threads = new ArrayList<>();
        try {
            Socket s = new Socket(host, port);
            byte instruction[] = {GET, THREADS, boardId, ESC};
            s.getOutputStream().write(instruction);
            byte buf[] = readMessage(s);
            s.close();
            int i = 0;
            while (buf[i] != ESC) {
                byte id = buf[i];
                i++;
                String name = "";
                while (buf[i] != MESS_ESC) {
                    name += (char)buf[i];
                    i++;
                }
                threads.add(new Thread(new Post(name), id));
                i++;
            }
            printThreads();
        } catch (Exception exception) {
            showErrorMessage("Thread Refresh", exception.getMessage());
        }
    }

    private void refreshPosts(byte boardId, byte threadId) {
        ArrayList<Post> posts = new ArrayList<>();
        try {
            Socket s = new Socket(host, port);
            byte instruction[] = {GET, POSTS, boardId, threadId, ESC};
            s.getOutputStream().write(instruction);
            byte buf[] = readMessage(s);
            s.close();
            int i = 0;
            while (buf[i] != ESC) {
                String header = "";
                while (buf[i] != MESS_ESC) {
                    header += (char)buf[i];
                    i++;
                }
                posts.add(new Post(header));
                i++;
            }
            ObservableList<String> items = FXCollections.observableArrayList();
            for (Post post : posts) {
                items.add(post.getHeader());
            }
            generalList.setItems(items);
            clientState = MenuState.THREAD;
        } catch (Exception exception) {
            showErrorMessage("Posts Refresh", exception.getMessage());
        }
    }

    private byte[] readMessage(Socket s) throws Exception {
        byte buf[] = new byte[1024], part[] = new byte[1024];
        int full_length = 0;
        while (full_length == 0  || buf[full_length - 1] != ESC) {
            int read = s.getInputStream().read(part);
            System.arraycopy(part, 0, buf, full_length, read);
            full_length += read;
        }
        return buf;
    }

    private static void showErrorMessage(String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.show();
    }

}