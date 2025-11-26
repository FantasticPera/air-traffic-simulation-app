package fileOperations;

import java.io.File;

public interface FileOperations<T>{
	
	void saveFile(File file, T item);
	void loadFile(File file, T item);

}
