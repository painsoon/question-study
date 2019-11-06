package com.psn.springboot.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface StorageService {

	void init();
	
	void store(MultipartFile file);
	
	void deleteAll();
	
	Stream<Path> loadAll();
	
	Path load(String filename);
	
	Resource loadAsResource(String filename);
	
}
