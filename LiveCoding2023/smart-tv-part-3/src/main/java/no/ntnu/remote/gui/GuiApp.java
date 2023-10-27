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
import no.ntnu.message.ChannelCountCommand;
import no.ntnu.message.ChannelCountMessage;
import no.ntnu.message.Command;
import no.ntnu.message.CurrentChannelMessage;
import no.ntnu.message.GetChannelCommand;
import no.ntnu.message.Message;
import no.ntnu.message.SetChannelCommand;
import no.ntnu.message.TurnOffCommand;
import no.ntnu.message.TurnOnCommand;
import no.ntnu.message.TvStateMessage;
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
  private Label currentChannelLabel;
  private Label channelCountLabel;
  int currentChannel = -1;

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
    if (reply instanceof TvStateMessage tvStateMessage) {
      setTvState(tvStateMessage.isOn());
      updatePowerButtonText();
      if (isTvOn) {
        queryChannelCount();
        queryCurrentChannel();
      }
    }
  }

  private void setTvState(boolean newState) {
    isTvOn = newState;
    channelPanel.setDisable(!isTvOn);
  }

  private void updatePowerButtonText() {
    String text = isTvOn ? "Turn off" : "Turn ON";
    powerButton.setText(text);
  }

  private void queryCurrentChannel() {
    Message response = tcpClient.sendCommand(new GetChannelCommand());
    if (response instanceof CurrentChannelMessage currentChannelMessage) {
      currentChannel = currentChannelMessage.getChannel();
      updateCurrentChannel();
    }
  }

  private void queryChannelCount() {
    Message response = tcpClient.sendCommand(new ChannelCountCommand());
    if (response instanceof ChannelCountMessage channelCountMessage) {
      updateChannelCount(channelCountMessage.getChannelCount());
    }
  }

  private void updateCurrentChannel() {
    currentChannelLabel.setText("" + currentChannel);
  }

  private void updateChannelCount(int channelCount) {
    channelCountLabel.setText("" + channelCount);
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
    Message response = tcpClient.sendCommand(new SetChannelCommand(desiredChannel));
    if (response instanceof CurrentChannelMessage) {
      currentChannel = desiredChannel;
      updateCurrentChannel();
    }
  }
}
