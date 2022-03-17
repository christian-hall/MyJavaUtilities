package util;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class LocalFile {
	/*
	 * 
	 * Essentially a wrapper class that would allow me to handle files with
	 * simplicity and shorter code
	 * 
	 * LocalFile localFile = new LocalFile(String filePath);
	 * 
	 */

	File file;
	String directory;
	String name;

	// ******* FULLY LOADED CONSTRUCTOR ********
	public LocalFile(String input) {
		super();
		this.file = new File(input);
		this.directory = parseDirectory(input);
		this.name = parseFileName(input);
	}

	// *********** EMPTY CONSTRUCTOR ***********
	public LocalFile() {
		super();
	}

	// ********** GETTERS AND SETTERS **********
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// ******** ATTRIBUTE CONSTRUCTORS *********
	public static String parseDirectory(String input) {
		String splitter = "/";
		if (input.contains("\\")) {
			splitter = "\\";
		}
		int idx = input.lastIndexOf(splitter);
		return input.substring(0, idx + 1);
	}

	public static String parseFileName(String input) {
		String splitter = "/";
		if (input.contains("\\")) {
			splitter = "\\";
		}
		int idx = input.lastIndexOf(splitter);
		return input.substring(idx);
	}

	/*
	 * *************************************************** 
	 * CREATING, DELETING, MOVING, RENAMING, COPYING FILES
	 * **************************************************/

	// creates an empty file and confirms that it exists
	public boolean create() {
		if (!this.exists()) {
			try {
				if (!this.file.createNewFile()) {
					return false;
				}
			} catch (IOException e) {
				// log the problem and stack trace
				return false;
			}
		}
		return true;
	}

	// deletes a file and confirms that it has been deleted
	public boolean delete() {
		if (this.exists()) {
			this.file.delete();
			if (this.exists()) {
				return false;
			}
		}
		return true;
	}

	// renames a file
	public boolean rename(String newName) {
		File newFile = new File(this.directory + newName);
		if (this.file.renameTo(newFile)) {
			setName(newName);
			setFile(newFile);
			return true;
		}
		return false;
	}

	// moves a file
	public boolean move(String newDirectory) {
		File newFile = new File(newDirectory + this.name);
		if (this.file.renameTo(newFile)) {
			setDirectory(newDirectory);
			setFile(newFile);
			return true;
		}
		return false;
	}

	// copies a file to a new name or new directory
	// - you need to declare a new file after for the copy
	public boolean copy(String copyName) {
		File newFile;
		if (copyName.contains("/") || copyName.contains("\\")) {
			newFile = new File(copyName + this.name);
		} else {
			newFile = new File(this.directory + copyName);
		}
		try {
			Files.copy(this.file.toPath(), newFile.toPath());
			return true;
		} catch (IOException e) {
			// log the error and print the stack trace
			return false;
		}

	}

	public boolean copy(String newDirectory, String newName) {
		File newFile = new File(newDirectory + newName);
		try {
			Files.copy(this.file.toPath(), newFile.toPath());
			return true;
		} catch (IOException e) {
			// log the error and stack trace
			return false;
		}

	}

	/* *************************************************** 
	 * ******** WRITING TO AND READING FROM A FILE *******
	 * **************************************************/

	public boolean write(String input) {
		FileWriter fw;
		try {
			fw = new FileWriter(this.file);
			if (!this.exists()) {
				this.create();
			}
			fw.write(input);
			fw.close();
			return true;
		} catch (IOException e) {
			// log here
			return false;
		}
	}
	
	public boolean append(String input) {
		String[] existingLines = this.getAll();
		if (existingLines.length > 0) {
			String formattedLines = "";
			for (int i = 0; i < existingLines.length; i++) {
				formattedLines += existingLines[i] + "\n";
			}
			input = formattedLines + input;
		}
		return this.write(input);
	}

	public String getLine(String contains) {
		try (BufferedReader br = new BufferedReader(new FileReader(this.file.getPath()))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(contains)) {
					return line;
				}
			}
			br.close();
		} catch (IOException e) {
			// log the error
		}
		return "";
	}

	public String[] getAll() {
		ArrayList<String> content = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(this.file.getPath()))) {
			String line;
			while ((line = br.readLine()) != null) {
				content.add(line);
			}
			br.close();
		} catch (IOException e) {
			// log errors here
		}
		return content.toArray(new String[content.size()]);
	}
	
	/* *************************************************** 
	 * ************ MISC FUNCTIONS & UTILITIES ***********
	 * **************************************************/
	
	public boolean exists() {
		return this.file.exists();
	}

}