package no.ntnu.remote.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import no.ntnu.message.ChannelCountCommand;
import no.ntnu.message.Command;
import no.ntnu.message.GetChannelCommand;
import no.ntnu.message.SetChannelCommand;
import no.ntnu.message.TurnOffCommand;
import no.ntnu.message.TurnOnCommand;
import no.ntnu.remote.ClientMessageListener;
import no.ntnu.remote.TcpClient;

/**
 * Graphical User Interface (GUI) application for a remote control.
 */
public class GuiApp extends Application implements ClientMessageListener {
  private static final int WIDTH = 200;
  private static final int HEIGHT = 150;
  private static TcpClient tcpClient;
  private boolean isTvOn = false;

  private Parent channelPanel;
  private Button powerButton;
  private Label currentChannelLabel;
  private Label channelCountLabel;
  int currentChannel = -1;

  public static void startApp(TcpClient tcpClient) {
    GuiApp.tcpClient = tcpClient;
    launch();
  }

  @Override
  public void start(Stage stage) throws Exception {
    tcpClient.startListeningThread(this);
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
    tcpClient.sendCommand(command);
  }

  private void setTvState(boolean newState) {
    isTvOn = newState;
    Platform.runLater(() -> channelPanel.setDisable(!isTvOn));
  }

  private void updatePowerButtonText() {
    String text = isTvOn ? "Turn off" : "Turn ON";
    Platform.runLater(() -> powerButton.setText(text));
  }

  private Parent createChannelPanel() {
    channelPanel = new HBox(createChannelIndicatorPanel(),
        createChannelButtonPanel());
    channelPanel.setDisable(!isTvOn);
    return channelPanel;
  }

  private Node createChannelIndicatorPanel() {
    currentChannelLabel = new Label("1");
    channelCountLabel = new Label("-");
    HBox channelIndicatorPanel = new HBox(new Label("Channel: "), currentChannelLabel,
        new Label("/"), channelCountLabel);
    channelIndicatorPanel.setSpacing(5);
    channelIndicatorPanel.setPadding(new Insets(5, 0, 0, 10));
    return channelIndicatorPanel;
  }

  private Node createChannelButtonPanel() {
    Button channelPlusButton = new Button("+");
    Button channelMinusButton = new Button("-");
    channelPlusButton.setOnMouseClicked(mouseEvent -> updateChannel(1));
    channelMinusButton.setOnMouseClicked(mouseEvent -> updateChannel(-1));
    HBox channelButtonPanel = new HBox(channelMinusButton, channelPlusButton);
    channelButtonPanel.setPadding(new Insets(0, 0, 0, 10));
    channelButtonPanel.setSpacing(5);
    return channelButtonPanel;
  }

  private void updateChannel(int channelIncrease) {
    int desiredChannel = currentChannel + channelIncrease;
    tcpClient.sendCommand(new SetChannelCommand(desiredChannel));
  }

  @Override
  public void handleTvStateChange(boolean isOn) {
    setTvState(isOn);
    updatePowerButtonText();
    if (isTvOn) {
      tcpClient.sendCommand(new ChannelCountCommand());
      tcpClient.sendCommand(new GetChannelCommand());
    }
  }

  @Override
  public void handleChannelCount(int channelCount) {
    Platform.runLater(() -> channelCountLabel.setText("" + channelCount));
  }

  @Override
  public void handleChannelChange(int channel) {
    currentChannel = channel;
    Platform.runLater(() -> currentChannelLabel.setText("" + currentChannel));
  }
}
