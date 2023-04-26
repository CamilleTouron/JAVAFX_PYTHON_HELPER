package pyhtonhelper;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		final StackPane root = new StackPane();
		if (PythonVersionChecker.executeCommand(isLinux()) != null) {
			root.getChildren().clear();
			root.getChildren().add(getHomeBox());
		} else {
			NoPythonBox box = new NoPythonBox(isLinux());
			root.getChildren().add(box);
			box.isPythonInstalledProperty().addListener(new ChangeListener<Boolean>() {
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						root.getChildren().clear();
						root.getChildren().add(getHomeBox());
					}
				} 
			});
		}

		Scene scene = new Scene(root, 800, 600);

		primaryStage.setTitle("Python helper");
		Image icon = new Image("icon.ico");
		primaryStage.getIcons().add(icon);		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	private boolean isLinux() {
		String osName = System.getProperty("os.name");

		boolean isLinux = false;
		if (osName.toLowerCase().contains("linux")) {
			isLinux = true;
		} else if (osName.toLowerCase().contains("windows")) {
			isLinux = false;
		}
		return isLinux;
	}
	
	private HomeBox getHomeBox(){
		boolean isUsbDetected = UsbDetector.isUsbDetected(isLinux());
		HomeBox homeBox = new HomeBox(PythonVersionChecker.executeCommand(isLinux()),isUsbDetected,isLinux());
		homeBox.setAlignment(Pos.CENTER);
		return homeBox;
	}
}
