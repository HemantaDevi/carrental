package com.accenture.carrental.hemanta.devi.huril.exceptions;

public class StorageException extends RuntimeException {
	private static final long serialVersionUID = -4329670201317675827L;

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
