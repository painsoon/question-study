package com.psn.springboot.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.psn.springboot.exception.StorageException;
import com.psn.springboot.exception.StorageFileNotFoundException;
import com.psn.springboot.service.StorageService;

@Service
public class StorageServiceImpl implements StorageService {

	//指定文件服务器目录
	private final Path rootLocation = Paths.get("E://D//");
		
	/* 
	 * 初始化
	 */
	@Override
	public void init() {
		
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
	/**
     * 保存文件
     *
     * @param file 文件
     */
	@Override
	public void store(MultipartFile file) {

		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		
		 if (file.isEmpty())
         {
             throw new StorageException("Failed to store empty file " + filename);
         }
		 if(filename.contains("..")){
			// This is a security check
             throw new StorageException("Cannot store file with relative path outside current directory " + filename);
         }
		 try {
			Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));
		} catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
		 
	}
	 /**
     * 删除upload-dir目录所有文件
     */
	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(this.rootLocation.toFile());
	}

	 /**
     * 列出upload-dir下面所有文件
     * @return
     */
	@Override
	public Stream<Path> loadAll() {
		
		try {
			return Files.walk(this.rootLocation, 1)
			.filter(new Predicate<Path>() 
			{
				@Override
				public boolean test(Path t) {
					return !t.equals(rootLocation);
				}
			});
			
		} catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}
	}

	@Override
	public Path load(String filename) {
		return this.rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		
		Path file = load(filename);
		
		try {
			
			Resource resource = new UrlResource(file.toUri());
			
			if(resource.exists() || resource.isReadable()){
				return resource;
			}else{
				 throw new StorageFileNotFoundException("Could not read file: " + filename);
			}
			
		} catch (MalformedURLException e) {
			 throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
		
	}

}
