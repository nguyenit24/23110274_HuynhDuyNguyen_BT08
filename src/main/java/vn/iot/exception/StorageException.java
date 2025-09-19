package vn.iot.exception;

public class StorageException extends RuntimeException{
	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Exception cause) {
		super(message, cause);
	}
}
