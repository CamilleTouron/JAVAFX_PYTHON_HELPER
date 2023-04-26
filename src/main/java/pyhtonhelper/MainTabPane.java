package pyhtonhelper;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;

public class MainTabPane {

	public static TabPane getUI(String pythonVersion, boolean isUsbDetected, boolean isLinux, int i, Scene scene) {

		Tab newScriptTab = new Tab("New Script");
		NewScriptBox newScriptBox = new NewScriptBox(scene);
		newScriptBox.setAlignment(Pos.CENTER);
		newScriptTab.setContent(newScriptBox);

		Tab exportScriptTab = new Tab("Export Script");
		ExportBox exportBox = new ExportBox(isLinux);
		newScriptBox.setAlignment(Pos.CENTER);
		exportScriptTab.setContent(exportBox);

		Tab pythonVersionTab = new Tab("Python Version");
		HBox pythonVersionBox = new PythonVersionBox(isLinux);
		pythonVersionBox.setAlignment(Pos.CENTER);
		pythonVersionTab.setContent(pythonVersionBox);

		TabPane tabPane = new TabPane();
		tabPane.getTabs().addAll(newScriptTab, exportScriptTab, pythonVersionTab);

		switch (i) {
		case 0:
			tabPane.getSelectionModel().select(newScriptTab);
			break;
		case 1:
			tabPane.getSelectionModel().select(exportScriptTab);
			break;
		case 2:
			tabPane.getSelectionModel().select(pythonVersionTab);
			break;
		}
		return tabPane;
	}

}
