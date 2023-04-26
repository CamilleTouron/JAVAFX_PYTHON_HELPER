package pyhtonhelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ExportBox extends VBox {

	private BooleanProperty isUsbDetected;
	private StringProperty chosenUsb;
	private StringProperty chosenScript;
	private boolean isLinux;
	private Label infoLabel;

	public ExportBox(boolean isLinux) {
		this.isLinux = isLinux;
		this.isUsbDetected = new SimpleBooleanProperty(UsbDetector.isUsbDetected(isLinux));
		this.isUsbDetected.addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					getChildren().clear();
					getChildren().add(getBoxUsbDetected());
				}
			}
		});
		this.chosenUsb = new SimpleStringProperty("");
		this.chosenScript = new SimpleStringProperty("none");
		this.infoLabel = new Label();

		setUI();
	}

	private void setUI() {
		setAlignment(Pos.CENTER);
		setPadding(new Insets(10));

		if (isUsbDetected.get()) {
			getChildren().add(getBoxUsbDetected());
		} else {
			getChildren().add(getBoxNoUsbDetected());
		}

		this.isUsbDetected.addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					getChildren().clear(); 
					getChildren().add(getBoxUsbDetected());
				}
			}
		});
	}

	private VBox getBoxNoUsbDetected() {
		Label noUsbLabel = new Label("No USB drives detected.");
		Button detectButton = new Button("Detect");
		detectButton.setOnAction(e -> {
			this.isUsbDetected.set(UsbDetector.isUsbDetected(isLinux));
		});
		VBox noUsbVBox = new VBox(10, noUsbLabel, detectButton);
		noUsbVBox.setAlignment(Pos.CENTER);
		return noUsbVBox;
	}

	private VBox getBoxUsbDetected() {
		Button exportButton = new Button("Export");
		exportButton.setOnAction(e -> {
		    if (chosenScript.get() == null || chosenScript.get().isEmpty() || chosenScript.get().equals("none")) {
		        infoLabel.setText("No script selected.");
		        return;
		    }
		    if (chosenUsb.get() == null || chosenUsb.get().isEmpty()) {
		        infoLabel.setText("No USB selected.");
		        return;
		    }
		    File scriptFile = new File(chosenScript.get());
		    if (!scriptFile.exists()) {
		        infoLabel.setText("Script file does not exist.");
		        return;
		    }
		    try {
		        String destPath = chosenUsb.get() + "\\";
		        String scriptName = scriptFile.getName();
		        String destFile = destPath + scriptName;
		        if (isLinux) {
		            ProcessBuilder builder = new ProcessBuilder("cp", scriptFile.getAbsolutePath(), destFile);
		            builder.start();
		        } else {
		            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "copy", scriptFile.getAbsolutePath(), destFile);
		            builder.start();
		        }
		        infoLabel.setText("Script exported to USB drive.");
		    } catch (IOException ex) {
		        ex.printStackTrace();
		        infoLabel.setText("Error exporting script to USB drive.");
		    }
		});
		VBox bottom = new VBox(10, exportButton, infoLabel);
		bottom.setAlignment(Pos.CENTER);

		VBox right = getRightBox();
		VBox left = getLeftBox();
		VBox middle = new VBox();
		middle.setMinWidth(100);
		HBox top = new HBox(10, right, middle, left);
		top.setAlignment(Pos.CENTER);

		VBox all = new VBox(10, top, bottom);
		all.setAlignment(Pos.CENTER);
		return all;
	}

	private VBox getRightBox() {
		VBox right = new VBox();
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		VBox usbList = new VBox(10);
		scrollPane.setContent(usbList);
		scrollPane.setPrefViewportHeight(200);

		Button openInBrowserButton = new Button("Open in Browser");
		openInBrowserButton.setOnAction(e -> {
			try {
				if (chosenUsb.get().isEmpty()) {
					infoLabel.setText("No USB selected.");
				} else {
					ProcessBuilder builder = new ProcessBuilder("explorer.exe", chosenUsb.get() + "\\");
					builder.start();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
		Button detectButton = new Button("Detect");
		detectButton.setOnAction(e -> {
			System.out.println("help");

			List<Usb> usbs = UsbDetector.getUsb(this.isLinux);
			chosenUsb.set("");
			if (usbs == null || usbs.isEmpty()) {
				Label noUsbLabel = new Label("No USB drives detected.");
				usbList.getChildren().add(noUsbLabel);
			} else {
				usbList.getChildren().clear();
				for (Usb usb : usbs) {
					Label label = new Label(usb.getPath() + usb.getTitle());
					label.setCursor(Cursor.HAND);
					label.setOnMouseClicked(event -> {
						chosenUsb.set(label.getText().substring(0, 2));
						for (Node node : usbList.getChildren()) {
							if (node instanceof Label) {
								((Label) node).setTextFill(Color.BLACK);
							}
						}
						label.setTextFill(Color.BLUE);
						infoLabel.setText("Selected USB drive: " + chosenUsb.get());
					});
					usbList.getChildren().add(label);
				}
			}
		});
		right.getChildren().addAll(scrollPane, openInBrowserButton, detectButton);
		right.setAlignment(Pos.CENTER);
		return right;

	}

	private VBox getLeftBox() {
		Label chosenScriptLabel = new Label("Selected Script: ");
		Label chosenScriptValueLabel = new Label();
		chosenScriptValueLabel.textProperty().bind(chosenScript);

		Button selectScriptButton = new Button("Select");
		selectScriptButton.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select Python Script");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Python Files", "*.py"));
			File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());
			if (selectedFile != null) {
				chosenScript.set(selectedFile.getAbsolutePath());
			}
		});

		VBox left = new VBox(10, chosenScriptLabel, chosenScriptValueLabel, selectScriptButton);
		left.setPadding(new Insets(10));
		left.setAlignment(Pos.CENTER);

		return left;
	}

}