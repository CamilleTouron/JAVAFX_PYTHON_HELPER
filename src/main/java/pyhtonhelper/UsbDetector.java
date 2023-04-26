package pyhtonhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsbDetector {

	public static boolean isUsbDetected(boolean isLinux) {
		if (isLinux) {
			return detectUsbOnLinux();
		} else {
			return detectUsbOnWindows();
		}
	}

	public static List<Usb> getUsb(boolean isLinux) {
		if (isLinux) {
			// TODO CHANGE TO LINUX
			return getUsbsWindows();
		} else {
			return getUsbsWindows();
		}
	}

	private static boolean detectUsbOnLinux() {
		try {
			String[] command = { "sh", "-c", "lsblk -J -b" };
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("\"type\": \"disk\",") && line.contains("\"size\": ")) {
					int index = line.indexOf("\"size\": ");
					int end = line.indexOf(",", index);
					String sizeStr = line.substring(index + 8, end).trim();
					long sizeBytes = Long.parseLong(sizeStr);
					if (sizeBytes >= 5L * 1024L * 1024L * 1024L) {
						return true;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean detectUsbOnWindows() {
		try {
			String[] command = { "cmd.exe", "/c", "wmic logicaldisk get volumename, description, size, caption" };
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				if (line.contains("Removable")) {
					String[] tokens = line.split("\\s+");
					if (tokens.length >= 3) {
						String sizeStr = tokens[3];
						long sizeBytes = Long.parseLong(sizeStr);
						System.out.print(sizeStr);
						if (sizeBytes > 0) {
							return true;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static List<Usb> getUsbsWindows() {
		List<Usb> usbList = new ArrayList<>();
		try {
			String[] command = { "cmd.exe", "/c", "wmic logicaldisk get volumename, description, size, caption" };
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String output = br.lines().collect(Collectors.joining("\n"));
			System.out.println("hello");
			usbList = transformWindowsOutputToUsbList(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return usbList;
	}

	private static List<Usb> transformWindowsOutputToUsbList(String output) {
		String[] lines = output.trim().split("\\r?\\n");
		List<Usb> usbList = new ArrayList<>();
		for (String line : lines) {
			System.out.println(line);
			String[] fields = line.trim().split("\\s{2,}");
			if (!line.contains("OS") && !line.contains("Caption")&& fields.length == 4 ) {
				String title = fields[3];
				String path = fields[0];
				Long size = Long.parseLong(fields[2]);
				Usb usb = new Usb(title, path, size);
				usbList.add(usb);
			}
		}
		return usbList;
	}
}
