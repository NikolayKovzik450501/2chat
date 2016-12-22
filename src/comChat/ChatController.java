package comChat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private TextField message1, message2, portField1, portField2;

    @FXML
    private Button sendButton1, sendButton2, connectButton1, connectButton2;

    @FXML
    private TextArea chatArea;

    @FXML
    Slider baudRateSlider1, baudRateSlider2;

    @FXML
    Button parityButton1, parityButton2;

    public void initialize(URL location, ResourceBundle resources) {
    }

    static void showErrorMessage(String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void onSend1() {
//        try {
//            portManager1.writeString(message1.getText());
//        } catch (InterruptedException ex) {
//            showErrorMessage("Left send interrupted", ex.getMessage());
//        }
    }

    @FXML
    private void onSend2() {
//        try {
//            portManager2.writeString(message2.getText());
//        } catch (InterruptedException ex) {
//            showErrorMessage("Right send interrupted", ex.getMessage());
//        }
    }

    void onStop() {
//        if (portManager1 != null && portManager1.isOpened()) portManager1.closePort();
//        if (portManager2 != null && portManager2.isOpened()) portManager2.closePort();
    }

    @FXML
    void onConnect1() {
//        portManager1 = onConnect(message1, sendButton1, portField1, connectButton1, portManager1,
//                baudRateSlider1, parityButton1);
    }

    @FXML
    void onConnect2() {
//        portManager2 = onConnect(message2, sendButton2, portField2, connectButton2, portManager2,
//                baudRateSlider2, parityButton2);
    }

    @FXML
    void onBaudRateChange1() {
//        portManager1.setBaudRate((int) baudRateSlider1.getValue());
    }

    @FXML
    void onBaudRateChange2() {
//        portManager2.setBaudRate((int) baudRateSlider2.getValue());
    }

    @FXML
    void onParitySet1() {
//        portManager1.changeParity();
//        parityButton1.setText(portManager1.getParity());
    }

    @FXML
    void onParitySet2() {
//        portManager2.changeParity();
//        parityButton2.setText(portManager2.getParity());
    }

    void addMessageToArea(String message) {
        Platform.runLater(() -> chatArea.setText(chatArea.getText() + message));
    }

//    private PortManager onConnect(TextField message,
//                                  Button sendButton,
//                                  TextField portField,
//                                  Button connectButton,
//                                  PortManager portManager,
//                                  Slider baudRateSlider,
//                                  Button parityButton) {
//        if (portManager != null && portManager.isOpened()) {
//            portManager.closePort();
//            Platform.runLater(() -> {
//                message.setDisable(true);
//                sendButton.setDisable(true);
//                baudRateSlider.setDisable(true);
//                parityButton.setDisable(true);
//                portField.setDisable(false);
//                connectButton.setText("Connect");
//            });
//            return null;
//        } else {
//            portManager = new PortManager(this, portField.getText());
//            Platform.runLater(() -> {
//                message.setDisable(false);
//                sendButton.setDisable(false);
//                baudRateSlider.setDisable(false);
//                parityButton.setDisable(false);
//                portField.setDisable(true);
//                connectButton.setText("Disconnect");
//            });
//            return portManager;
//        }
//    }


}