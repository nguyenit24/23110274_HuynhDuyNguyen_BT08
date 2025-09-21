package vn.iot.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.iot.enity.CategoryEntity;
import vn.iot.response.Response;
import vn.iot.service.ICategoryService;
import vn.iot.service.IStorageService;

@RestController
@Validated
@RequestMapping("/api/categories")
public class CategoryAPIController {
	@Autowired
	ICategoryService categoryService;
	@Autowired
	IStorageService storageService;

	@GetMapping
	public ResponseEntity<?> getAllCategory() {
		return new ResponseEntity<Response>(
				new Response(true, "Thành công", categoryService.findAll()),
				HttpStatus.OK);
	}

	@PostMapping("/getCategory")
	public ResponseEntity<?> getCategory(@Valid @RequestParam("id") Long id) {
		Optional<CategoryEntity> category = categoryService.findById(id);
		if (category.isPresent()) {
			return new ResponseEntity<Response>(new Response(true, "Thành công", category.get()), HttpStatus.OK);
		}
		return new ResponseEntity<Response>(new Response(false, "Thất bại", null), HttpStatus.NOT_FOUND);
	}

	@PostMapping("/addCategory")
	public ResponseEntity<?> addCategory(@RequestParam("CategoryName") String categoryName,
			@RequestParam("icon") MultipartFile icon) {
		Optional<CategoryEntity> category = categoryService.findByName(categoryName);
		if (category.isPresent()) {
			return new ResponseEntity<Response>(new Response(false, "Category đã tồn tại", null),
					HttpStatus.BAD_REQUEST);
		} else {
			CategoryEntity categoryEntity = new CategoryEntity();
			categoryEntity.setName(categoryName);
			UUID uuid = UUID.randomUUID();
			if (!icon.isEmpty()) {
				String uuString = uuid.toString();
				categoryEntity.setIcon(storageService.getSorageFilename(icon, uuString));
				storageService.store(icon, categoryEntity.getIcon());
			}
			categoryService.save(categoryEntity);
			return new ResponseEntity<Response>(new Response(true, "Thành công", categoryEntity), HttpStatus.CREATED);
		}
	}

	@PutMapping(path = "/updateCategory")
	public ResponseEntity<?> updateCategory(@Validated @RequestParam("categoryId") Long categoryId,
			@Validated @RequestParam("categoryName") String categoryName,
			@Validated @RequestParam("icon") MultipartFile icon) {
		Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);
		if (optCategory.isEmpty()) {
			return new ResponseEntity<Response>(new Response(false, "Không tìm thấy Category", null),
					HttpStatus.BAD_REQUEST);
		} else if (optCategory.isPresent()) {
			if (!icon.isEmpty()) {
				UUID uuid = UUID.randomUUID();
				String uuString = uuid.toString();
				optCategory.get().setIcon(storageService.getSorageFilename(icon, uuString));
				storageService.store(icon,
						optCategory.get().getIcon());
			}
			optCategory.get().setName(categoryName);
			categoryService.save(optCategory.get());
			return new ResponseEntity<Response>(new Response(true, "Cập nhật Thành công", optCategory.get()),
					HttpStatus.OK);
		}
		return null;
	}

	@DeleteMapping("/deleteCategory")
	public ResponseEntity<?> deleteCategory(@Valid @RequestParam("id") Long id) {
		Optional<CategoryEntity> optCategory = categoryService.findById(id);
		if (optCategory.isEmpty()) {
			return new ResponseEntity<Response>(new Response(false, "Không tìm thấy Category", null),
					HttpStatus.BAD_REQUEST);
		} else if (optCategory.isPresent()) {
			categoryService.deleteById(id);
			return new ResponseEntity<Response>(new Response(true, "Xóa Thành công", optCategory.get()), HttpStatus.OK);
		}
		return null;
	}

}
