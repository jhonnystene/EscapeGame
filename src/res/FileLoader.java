package res;

import java.io.InputStream;

public class FileLoader {
	public InputStream load(String filePath) {
		try {
			InputStream in = this.getClass().getResourceAsStream(filePath);
			return in;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
