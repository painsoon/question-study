/**
 * 
 */
package com.edda.exception;

/**
 * @author sn.pan
 *
 * @date 2018年8月24日 
 */
public class StorageException extends RuntimeException{

	public StorageException(String message) {
		super(message);
	}
	
	public StorageException(String message,Throwable cause) {
		super(message,cause);
	}
	
}
