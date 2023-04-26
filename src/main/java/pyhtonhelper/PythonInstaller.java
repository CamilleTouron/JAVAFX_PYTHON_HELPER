package pyhtonhelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public abstract class PythonInstaller {
	private static ZipFile zipFile;

	public static void installPython(Boolean isLinux,String version) throws IOException {
		if (isLinux) {
			installPythonLinux(version);
		} else {
			installPythonWindows(version);
		}
	}

	private static void installPythonLinux(String version) {
		String downloadUrl = "https://www.python.org/ftp/python/"+version+"/Python-"+version+".tgz";
		String installerFilename = "Python-"+version+".tgz";
		String installCommand = "cd /tmp/Python-"+version+"/ && ./configure && make && sudo make install";

		try {
			URL url = new URL(downloadUrl);
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStream is = connection.getInputStream();
			Files.copy(is, Paths.get(installerFilename));
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Process process = Runtime.getRuntime().exec(installCommand, null);
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String[] commands = { "tar", "-xzf", installerFilename, "-C", "/tmp/" };
			Process process = new ProcessBuilder(commands).start();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void installPythonWindows(String version) {
		String downloadUrl = "https://www.python.org/ftp/python/"+version+"/python-"+version+"-embed-amd64.zip";
		String installCommand = "cmd /c start /wait /tmp/python.exe -m ensurepip";
		String installerFilename = "python-"+version+"-embed-amd64.zip";

		try {
			URL url = new URL(downloadUrl);
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStream is = connection.getInputStream();
			Files.copy(is, Paths.get(installerFilename), StandardCopyOption.REPLACE_EXISTING);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			zipFile = new ZipFile(installerFilename);
			zipFile.extractAll("./tmp");
		} catch (ZipException e) {
			e.printStackTrace();
		}

		try {
			Process process = Runtime.getRuntime().exec(installCommand, null);
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String pythonPath = new File("tmp").getAbsolutePath();
		try {
			String path = System.getenv("PATH");
			String newPath = pythonPath + ";" + path;
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "setx", "PATH", newPath);
			builder.redirectErrorStream(true);
			Process process = builder.start();
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
