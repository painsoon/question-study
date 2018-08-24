/**
 * 
 */
package com.edda.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author sn.pan
 *
 * @date 2018年8月24日 
 */
public interface StorageService {
	
	void init();
	
	void store(MultipartFile file);
	
	Stream<Path> loadAll();
	
	Path load(String filename);
	
	Resource loadAsResource(String filename);
	
	void deleteAll();

}
