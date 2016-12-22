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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private ListView<String> generalList;

    private final static byte ESC = (byte) 0xF0, MESS_ESC = (byte) 0xF1, GET = (byte) 0xF2,
            BOARDS = (byte) 0xF3, THREADS = (byte) 0xF4, POSTS = (byte) 0xF5, SET = (byte) 0xF6;

    private ArrayList<Board> boards;

    public void initialize(URL location, ResourceBundle resources) {
        refreshBoards();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Board board : boards) {
            items.add(board.getName());
        }
        generalList.setItems(items);
    }

    private void refreshBoards() {
        boards = new ArrayList<>();
        try {
            Socket s = new Socket("localhost", 5005);
            byte instruction[] = {GET, BOARDS, ESC};
            s.getOutputStream().write(instruction);
            byte buf[] = new byte[64 * 1024];
            int r = s.getInputStream().read(buf);
            int i = 0;
            while (buf[i] != ESC) {
                int id = buf[i];
                i++;
                String name = "";
                while (buf[i] != MESS_ESC) {
                    name += (char)buf[i];
                    i++;
                }
                boards.add(new Board(id, name));
                i++;
            }
        } catch (Exception exception) {
            showErrorMessage("Board", exception.getMessage());
        }
    }

    static void showErrorMessage(String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.show();
    }

}