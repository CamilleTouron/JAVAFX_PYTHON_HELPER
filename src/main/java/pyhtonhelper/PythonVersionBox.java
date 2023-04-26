package pyhtonhelper;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PythonVersionBox extends HBox {
	private WebView webView;
	private VBox versionBox;
	private boolean isVersionListSet;	
	private StringProperty chosenVersion;
	private Label infoLabel;
	private boolean isLinux;
	private StringProperty currentVersion;



	public PythonVersionBox(boolean isLinux) {
		super();
		this.isLinux = isLinux;
		this.isVersionListSet = false;
		this.chosenVersion = new SimpleStringProperty("none");
		this.infoLabel = new Label("Selected version : "+chosenVersion.get());
		this.currentVersion = new SimpleStringProperty(PythonVersionChecker.executeCommand(isVersionListSet));
		setupUI();
		setupWebView();
	}

	private void setupUI() {
		setSpacing(10);
		setPadding(new Insets(10));
		webView = new WebView();
		webView.setPrefWidth(400);
		webView.setPrefHeight(400);
		webView.setOnMouseClicked(event -> webView.requestFocus());
		versionBox = new VBox();

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(webView);
		scrollPane.setFitToWidth(true);
		if (checkConnection()) {
			getChildren().addAll(scrollPane, versionBox);
		} else {
			Label errorLabel = new Label("No internet connection");
			Button retryButton = new Button("Retry");
			retryButton.setOnAction(event -> {
				if (checkConnection()) {
					getChildren().clear();
					getChildren().addAll(scrollPane, versionBox);
				} else {

				}
			});
			setAlignment(Pos.CENTER);
			getChildren().addAll(errorLabel, retryButton);
		}

	}

	private boolean checkConnection() {
		try {
			Connection connection = Jsoup.connect("https://www.python.org/downloads/");
			connection.timeout(5000);
			connection.execute();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private void setupWebView() {
		WebEngine webEngine = webView.getEngine();
		webEngine.load("https://www.python.org/downloads/");

		webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
			Button update = new Button("Update version");
			Label currentVersionLabel = new Label("Current Python Version : "+currentVersion.get());
			if (newValue == Worker.State.SUCCEEDED && !isVersionListSet) {
				// extract available versions
				List<String> availableVersions = new ArrayList<>();
				try {
					Document doc = Jsoup.connect("https://www.python.org/downloads/").get();
					Elements versions = doc.select(".release-number a");
					for (int i = 0; i < 6; i++) {
						availableVersions.add(versions.get(i).text());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				ScrollPane scrollPane = new ScrollPane();
				scrollPane.setFitToWidth(true);
				VBox versionList = new VBox(10);
				scrollPane.setContent(versionList);
				scrollPane.setPrefViewportHeight(200);
				for (String version : availableVersions) {
					Label label = new Label(version);
					label.setCursor(Cursor.HAND);
					label.setOnMouseClicked(event -> {
						chosenVersion.set(version);
						for (Node node : versionList.getChildren()) {
							if (node instanceof Label) {
								((Label) node).setTextFill(Color.BLACK);
							}
						}
						label.setTextFill(Color.BLUE);
						infoLabel.setText("Selected version : "+chosenVersion.get());
						update.setText("Install "+chosenVersion.get());
					});
					versionList.getChildren().add(label);
				}
				update.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						if(chosenVersion.get().equals("none")) {
							infoLabel.setText("No selected version.");
						}else {
							infoLabel.setText("Please wait.");
							try {
								String version = chosenVersion.get().substring(7, chosenVersion.get().length());
								PythonInstaller.installPython(isLinux, version);
								currentVersion.set(version);
								currentVersionLabel.setText("Current Python Version : "+currentVersion.get());
								infoLabel.setText("Version well installed.");
							} catch (IOException ex) {
								infoLabel.setText("Version installation went wrong.");
								Logger.getLogger(NoPythonBox.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
						
					}
				});
				versionBox.setSpacing(10);
				versionBox.setPadding(new Insets(10));
				versionBox.setAlignment(Pos.CENTER);
				versionBox.getChildren().addAll(currentVersionLabel,scrollPane,infoLabel,update);
			}
			isVersionListSet = true;
		});
	}
}
