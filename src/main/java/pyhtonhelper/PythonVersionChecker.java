package pyhtonhelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonVersionChecker {

    public static String executeCommand(boolean isLinux) {
    	if(isLinux) {
    		try {
                Process process = Runtime.getRuntime().exec("python3 --version");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = reader.readLine();
                reader.close();
                return line;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
    	}else {
			try {
				Process process = Runtime.getRuntime().exec("python --version");
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = reader.readLine();
				reader.close();
				return line;
			} catch (IOException e) {
				return null;
			}
    	}
        
    }
}
