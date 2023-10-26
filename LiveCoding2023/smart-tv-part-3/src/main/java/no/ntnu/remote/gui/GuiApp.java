package no.ntnu.remote.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import no.ntnu.message.Command;
import no.ntnu.message.Message;
import no.ntnu.message.OkMessage;
import no.ntnu.message.TurnOffCommand;
import no.ntnu.message.TurnOnCommand;
import no.ntnu.remote.TcpClient;

/**
 * Graphical User Interface (GUI) application for a remote control.
 */
public class GuiApp extends Application {
  private static final int WIDTH = 200;
  private static final int HEIGHT = 150;
  private static TcpClient tcpClient;
  private boolean isTvOn = false;

  private Parent channelPanel;
  private Button powerButton;

  public static void startApp(TcpClient tcpClient) {
    GuiApp.tcpClient = tcpClient;
    launch();
  }

  @Override
  public void start(Stage stage) throws Exception {
    Scene scene = new Scene(createContent(), WIDTH, HEIGHT);
    stage.setScene(scene);
    stage.show();
  }

  private Parent createContent() {
    VBox content = new VBox(createPowerPanel(), createChannelPanel());
    content.setSpacing(10);
    return content;
  }

  private Node createPowerPanel() {
    powerButton = new Button("Turn ON");
    powerButton.setOnMouseClicked(mouseEvent -> toggleTvPower());
    HBox panel = new HBox(powerButton);
    panel.setPadding(new Insets(10, 0, 0, 10));
    return panel;
  }

  private void toggleTvPower() {
    Command command = isTvOn ? new TurnOffCommand() : new TurnOnCommand();
    Message reply = tcpClient.sendCommand(command);
    if (reply instanceof OkMessage) {
      setTvState(!isTvOn);
    }
    updatePowerButtonText();
  }

  private void setTvState(boolean newState) {
    isTvOn = newState;
    channelPanel.setDisable(!isTvOn);
  }

  private void updatePowerButtonText() {
    String text = isTvOn ? "Turn off" : "Turn ON";
    powerButton.setText(text);
  }

  private Parent createChannelPanel() {
    channelPanel = new HBox(createChannelIndicatorPanel(),
        createChannelButtonPanel());
    channelPanel.setDisable(!isTvOn);
    return channelPanel;
  }

  private Node createChannelIndicatorPanel() {
    Label channelLabel = new Label("1");
    Label channelCountLabel = new Label("-");
    HBox channelIndicatorPanel = new HBox(new Label("Channel: "), channelLabel,
        new Label("/"), channelCountLabel);
    channelIndicatorPanel.setSpacing(5);
    channelIndicatorPanel.setPadding(new Insets(5, 0, 0, 10));
    return channelIndicatorPanel;
  }

  private Node createChannelButtonPanel() {
    Button plusButton = new Button("+");
    Button minusButton = new Button("-");
    HBox channelButtonPanel = new HBox(minusButton, plusButton);
    channelButtonPanel.setPadding(new Insets(0, 0, 0, 10));
    channelButtonPanel.setSpacing(5);
    return channelButtonPanel;
  }
}
