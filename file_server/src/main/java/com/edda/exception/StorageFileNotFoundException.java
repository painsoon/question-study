/**
 * 
 */
package com.edda.exception;

/**
 * @author sn.pan
 *
 * @date 2018年8月24日 
 */
public class StorageFileNotFoundException extends StorageException {

	public StorageFileNotFoundException(String message) {
		 super(message);
	}
	
	public StorageFileNotFoundException(String message,Throwable cause) {
		super(message, cause);
	}
	
}
