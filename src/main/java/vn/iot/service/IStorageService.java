package vn.iot.service;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

public interface IStorageService {
	void init();
	void delete(String storeFilename) throws Exception;
	Path load(String filename);
	Resource loadAsResource(String filename);
	void store(MultipartFile file, String storeFilename);
	String getSorageFilename(MultipartFile file, String id);
}
