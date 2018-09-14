package directoryWatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DirectoryWatcher {
	
	private HashMap<String, FileStats> directoryContents;
	private HashSet<File> directoryFiles;
	private File directory;
	private TextFileFilter filter;
	
	public DirectoryWatcher(String pathName, String fileExtension) throws NullPointerException {
		
		directoryContents = new HashMap<String, FileStats>();
		directoryFiles = new HashSet<File>();
		directory = new File(pathName);
		filter = new TextFileFilter(fileExtension);
		
		for(File file : directory.listFiles(filter)) {
			directoryContents.put(file.getName(), new FileStats(file.lastModified(), countLines(file)));
			directoryFiles.add(file);
		}
	}
	
	public void checkDirectory() {
		checkDeletions();
		checkAdditions();
		checkUpdates();
	}

	private void checkUpdates() { 
		for(File file : directory.listFiles(filter)) {
			if(file.lastModified() != directoryContents.get(file.getName()).getDateModified()) {
				int lines = countLines(file);
				int changeInLines = lines - directoryContents.get(file.getName()).getNumberOfLines();
				System.out.println('\r' + file.getName() + " has been changed, " +
						(changeInLines > 0 ? "+" : "") + changeInLines + " lines         ");
				directoryContents.replace(file.getName(), new FileStats(file.lastModified(), lines));
			}
		}
	}

	private void checkAdditions() {
		for(File file : directory.listFiles(filter)) {
			if(!directoryFiles.contains(file)) {
			
				int lines = countLines(file);
				System.out.println('\r' + file.getName() + " has been created, file has " + lines + " lines         ");
				directoryFiles.add(file);
				directoryContents.put(file.getName(), new FileStats(file.lastModified(), lines));
				
			}
		}
	}

	private int countLines(File file) {
	
		int lines = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while (reader.readLine() != null) lines++;
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	private void checkDeletions() {
		
		ArrayList<File> toDelete = new ArrayList<File>();
		
		for(File file : directoryFiles) {
			if(!file.exists()) {
				System.out.println('\r' + file.getName() + " was deleted              ");
				toDelete.add(file);
				directoryContents.remove(file.getName());
			}
		}
		directoryFiles.removeAll(toDelete);
	}
	
	private class FileStats {
		
		long modified;
		int lines;
		
		public FileStats(long dateModified, int numberOfLines) {
			modified = dateModified;
			lines = numberOfLines;
		}
		
		public long getDateModified() {
			return modified;
		}
		
		public int getNumberOfLines() {
			return lines;
		}
	}
}
