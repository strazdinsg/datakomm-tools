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
import no.ntnu.remote.TcpClient;

/**
 * Graphical User Interface (GUI) application for a remote control.
 */
public class GuiApp extends Application {
  private static final int WIDTH = 200;
  private static final int HEIGHT = 150;
  private static TcpClient tcpClient;

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
    Button powerButton = new Button("Turn ON");
    HBox panel = new HBox(powerButton);
    panel.setPadding(new Insets(10, 0, 0, 10));
    return panel;
  }

  private Node createChannelPanel() {
    return new HBox(createChannelIndicatorPanel(),
        createChannelButtonPanel());
  }

  private Node createChannelIndicatorPanel() {
    Label channelLabel = new Label("1");
    Label channelCountLabel = new Label("20");
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
