package exception;

public class DuplicateKeyException extends ValidationException {

	public DuplicateKeyException(String item, String key) {
		super(item + " with key: " + key + " already exists.");
		
	}

}
