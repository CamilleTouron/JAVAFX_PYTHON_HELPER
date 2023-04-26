package pyhtonhelper;

public class Usb {
	private String title;
	private String path;
	private Long size;

	public Usb(String title, String path, Long size) {
		super();
		this.title = title;
		this.path = path;
		this.size = size;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("USB - ");
		sb.append("title = ");
		sb.append(title);
		sb.append(" - path = ");
		sb.append(path);
		sb.append(" - size = ");
		sb.append(size.toString());
		return sb.toString();
	}
	
}
