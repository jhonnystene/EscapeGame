package res;

import java.io.BufferedInputStream;
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
	
	public BufferedInputStream loadBuffered(String filePath) {
		try {
			BufferedInputStream in = new BufferedInputStream(this.getClass().getResourceAsStream(filePath));
			return in;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
