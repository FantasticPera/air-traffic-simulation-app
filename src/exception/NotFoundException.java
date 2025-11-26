package exception;

public class NotFoundException extends ValidationException {

	public NotFoundException(String item, String key) {
		super(item + " with key: " + key + " not found.");
	}

}
