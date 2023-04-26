package pyhtonhelper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HomeBox extends VBox {

	private String pythonVersion;
	private boolean isUsbDetected;
	private boolean isLinux;

	public HomeBox(String pythonVersion, boolean isUsbDetected, boolean isLinux) {
		this.pythonVersion = pythonVersion;
		this.isUsbDetected = isUsbDetected;
		this.isLinux = isLinux;
		setUI();
	}

	private void setUI() {
		Label welcomeLabel = new Label("Welcome to Python Helper");
		welcomeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16pt;");
		Button newScriptButton = new Button("New Script");
		Button exportScriptButton = new Button("Export Script");
		Button pythonVersionButton = new Button("Python Version");
		Label pythonVersionLabel = new Label("Python Version: " + pythonVersion);
		Label usbLabel = new Label(isUsbDetected ? "USB Detected" : "No USB Detected");
		Label osLabel = new Label(isLinux ? "Linux" : "Windows");
		Label developedByLabel = new Label("Developed by Camille Touron");

		newScriptButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				getChildren().clear();
				setAlignment(Pos.TOP_CENTER);
				getChildren().add(MainTabPane.getUI(pythonVersion, isUsbDetected, isLinux, 0, getScene()));
			}
		});

		exportScriptButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				getChildren().clear();
				setAlignment(Pos.TOP_CENTER);
				getChildren().add(MainTabPane.getUI(pythonVersion, isUsbDetected, isLinux, 1, getScene()));
			}
		});

		pythonVersionButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				getChildren().clear();
				setAlignment(Pos.TOP_CENTER);
				getChildren().add(MainTabPane.getUI(pythonVersion, isUsbDetected, isLinux, 2, getScene()));
			}
		});
		setAlignment(Pos.TOP_CENTER);
		setPadding(new Insets(10));
		setSpacing(10);

		getChildren().addAll(welcomeLabel, newScriptButton, exportScriptButton,
				pythonVersionButton, pythonVersionLabel, usbLabel, osLabel, developedByLabel);
	}
}
