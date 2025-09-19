package vn.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iot.response.Response;
import vn.iot.service.ICategoryService;
import vn.iot.service.IStorageService;


@RestController
@RequestMapping("/api/categories")
public class CategoryAPIController {
	@Autowired
	ICategoryService categoryService;
	@Autowired
	IStorageService storageService;
	
	@GetMapping
	public ResponseEntity<?> getAllCategory(){
		return new ResponseEntity<Response>(
				new Response(true, "Thành công", categoryService.findAll()), 
				HttpStatus.OK);
	}
}
