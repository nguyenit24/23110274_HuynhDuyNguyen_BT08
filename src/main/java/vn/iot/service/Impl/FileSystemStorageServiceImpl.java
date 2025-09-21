package vn.iot.service.Impl;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vn.iot.config.StorageProperties;
import vn.iot.exception.StorageException;
import vn.iot.service.IStorageService;

@Service
class FileSystemStorageServiceImpl implements IStorageService {

	private Path rootLocation = null;

	public FileSystemStorageServiceImpl(StorageProperties properties) {
		super();
		this.rootLocation = Path.of(properties.getLocation());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
			System.out.println(rootLocation.toString());
		} catch (Exception e) {
			throw new StorageException("Could not read file: ", e);
		}

	}

	@Override
	public void delete(String storeFilename) throws Exception {
		Path destinationFile = rootLocation.resolve(java.nio.file.Paths.get(storeFilename)).normalize()
				.toAbsolutePath();
		Files.delete(destinationFile);
	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			throw new StorageException("Can not read file: " + filename);
		} catch (Exception e) {
			throw new StorageException("Could not read file: " + filename);
		}
	}

	@Override
	public void store(MultipartFile file, String storeFilename) {
		try {
			if (file.isEmpty()) {
				throw new Exception("Failed to store empty file " + storeFilename);
			}
			Path destinationFile = this.rootLocation.resolve(Path.of(storeFilename)).normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				throw new Exception("Cannot store file outside current directory");
			}
			try (var inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				throw new Exception("Failed to store file " + storeFilename, e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public String getSorageFilename(MultipartFile file, String id) {
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		return "p" + id + "." + ext;
	}

}
