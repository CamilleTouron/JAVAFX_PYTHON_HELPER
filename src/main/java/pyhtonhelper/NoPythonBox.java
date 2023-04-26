package pyhtonhelper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class NoPythonBox extends VBox{

	private boolean isLinux;
	private BooleanProperty isPythonInstalled;
	private StringProperty pythonVersionProp;

	public NoPythonBox(boolean isLinux) {
		this.isLinux = isLinux;
		this.isPythonInstalled = new SimpleBooleanProperty(false);
		this.pythonVersionProp = new SimpleStringProperty("no version");
		setUI();
	}

	public void setUI() {
		Label titleLabel = new Label("Welcome to python helper");
		titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16pt;");
		Label messageLabel = new Label("You do not have python installed");
		final Button installButton = new Button("Install Python");

		installButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				installButton.setDisable(true);
				try {
					PythonInstaller.installPython(isLinux,"3.10.11");
					String pythonVersion = PythonVersionChecker.executeCommand(isLinux);

					if (pythonVersion != null) {
						isPythonInstalled.set(true);
						pythonVersionProp.set(pythonVersion);
					}else {
						try {
						    Thread.sleep(5000); // wait for 5 seconds
						} catch (InterruptedException error) {
							error.printStackTrace();
						}
						pythonVersion = PythonVersionChecker.executeCommand(isLinux);

						if (pythonVersion != null) {
							isPythonInstalled.set(true);
							pythonVersionProp.set(pythonVersion);
						}
					}
				} catch (IOException ex) {
					Logger.getLogger(NoPythonBox.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});

		setPadding(new Insets(10));
		setAlignment(Pos.CENTER);
		getChildren().addAll(titleLabel, messageLabel, installButton);
	}

	public BooleanProperty isPythonInstalledProperty() {
		return this.isPythonInstalled;
	}

	public StringProperty pythonVersionProperty() {
		return this.pythonVersionProp;
	}
}
