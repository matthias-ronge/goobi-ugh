package converter.processing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ValidateBackup implements Validatable {

	String myID = null;
	String myBaseFolder = null;
	
	
	
	public void setBaseFolder(String path) {
		myBaseFolder = path;
	}

	public void setID(String id) {
		myID = id;
	}

	public void setSearchString(String searchExpression) {

	}

	public void validate() throws ContentLibException {
		try {
			if (new FileReader(myBaseFolder + myID + "/meta.xml.bak").read()>1){
				return;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new ContentLibException("Validate Error - backup of metafile not found, inconsistent data",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new ContentLibException("Validate Error - backup of metafile not found, inconsistent data",e);
		}
	}

}
