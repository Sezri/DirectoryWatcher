package directoryWatcher;

import java.io.File;
import java.io.FilenameFilter;

public class TextFileFilter implements FilenameFilter{
	
	private String extension;
	
	public TextFileFilter(String extension) {
		this.extension = extension;
	}

	@Override
	public boolean accept(File dir, String name) {
		if(name.endsWith(extension))
			return true;
		else
			return false;
	}

}
