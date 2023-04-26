package pyhtonhelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class NewScriptBox extends HBox {

	private static Label errorLabel;
	private static Scene scene;

	@SuppressWarnings("static-access")
	public NewScriptBox(Scene scene) {
		this.setScene(scene);
		setUI();
	}

	private void setUI() {
		VBox leftVBox = new VBox(5);
		leftVBox.setPadding(new Insets(10));
		leftVBox.setAlignment(Pos.TOP_LEFT);
		errorLabel = new Label();
		Label titleLabel = new Label("Script Title");
		TextField titleInput = new TextField();

		Label contentLabel = new Label("Script Content");
		TextArea contentInput = new TextArea();

		Button saveButton = new Button("Save");
		saveButton.setOnAction(e -> {
			errorLabel.setText("");
			saveScript(titleInput.getText(), contentInput.getText());
		});

		Button openButton = new Button("Existing");
		openButton.setOnAction(e -> {
			errorLabel.setText("");
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Python Script");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Python files (*.py)", "*.py"));
			File file = fileChooser.showOpenDialog(getScene().getWindow());
			if (file != null) {
				titleInput.setText(file.getName().replaceFirst("[.][^.]+$", ""));
				try {
					contentInput.setText(java.nio.file.Files.readString(file.toPath()));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});

		Button clearButton = new Button("Clear");
		clearButton.setOnAction(e -> {
			titleInput.setText("");
			contentInput.setText("");
		});

		leftVBox.getChildren().addAll(titleLabel, titleInput, contentLabel, contentInput, saveButton, openButton,
				clearButton, errorLabel);
		VBox rightVBox = new VBox(5);
		rightVBox.setPadding(new Insets(10));
		rightVBox.setAlignment(Pos.TOP_LEFT);

		Label resultLabel = new Label("Result");
		TextArea resultContent = new TextArea();

		Button testButton = new Button("Test");
		testButton.setOnAction(e -> {
			String content = contentInput.getText();
			String result = executeScript(content);
			resultContent.setText(result);
		});

		rightVBox.getChildren().addAll(resultLabel, resultContent, testButton);

		setPadding(new Insets(10));
		getChildren().addAll(leftVBox, rightVBox);
	}

	private static void saveScript(String title, String content) {
		if (title == null || title.isEmpty() || content == null || content.isEmpty()) {
			errorLabel.setText("Please enter script title and content.");
			return;
		} else {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Python Script");
			fileChooser.setInitialFileName(title.replaceAll("[^a-zA-Z0-9]", "_") + ".py");
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Python files (*.py)", "*.py");
			fileChooser.getExtensionFilters().add(extFilter);
			File file = fileChooser.showSaveDialog(scene.getWindow());
			if (file != null) {
				try (FileWriter writer = new FileWriter(file)) {
					writer.write(content);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static String executeScript(String content) {
		String result = "";

		if (content == null || content.isEmpty()) {
			errorLabel.setText("Please enter script title and content.");
			return "";
		} else {
			try {
				File tempFile = File.createTempFile("tempfile", ".py");
				tempFile.deleteOnExit();
				FileWriter writer = new FileWriter(tempFile);
				writer.write(content);
				writer.close();
				ProcessBuilder processBuilder = new ProcessBuilder("python", tempFile.getAbsolutePath());
				Process process = processBuilder.start();
				java.io.InputStream inputStream = process.getInputStream();
				java.util.Scanner scanner = new java.util.Scanner(inputStream).useDelimiter("\\A");
				if (scanner.hasNext()) {
					result = scanner.next();
				}
				scanner.close();
			} catch (Exception e) {
				e.printStackTrace();
				result = "Error: " + e.getMessage();
			}
		}
		return result;
	}

	public static void setScene(Scene scene) {
		NewScriptBox.scene = scene;
	}

}
