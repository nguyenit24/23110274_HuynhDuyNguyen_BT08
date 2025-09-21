package vn.iot.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import vn.iot.enity.CategoryEntity;
import vn.iot.enity.ProductEntity;
import vn.iot.response.Response;
import vn.iot.service.ICategoryService;
import vn.iot.service.IProductServcie;
import vn.iot.service.IStorageService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    IProductServcie productServcie;

    @Autowired
	IStorageService storageService;

    @Autowired
    ICategoryService categoryService;

    @GetMapping("/getAllProduct")
    public ResponseEntity<?> getAllProduct() {
        return new ResponseEntity<Response>(new Response(true, "Thành công", productServcie.findAll()), HttpStatus.OK);
    }
    

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(
        @RequestParam("productName") String productName,
        @RequestParam("unitPrice") Double unitPrice,
        @RequestParam("quantity") Integer quantity,
        @RequestParam("description") String description,
        @RequestParam("categoryName") String categoryName,
        @RequestParam("status") Boolean status,
        @RequestParam("discount") Double discount,
        @RequestParam("image") MultipartFile image) {
        Optional<ProductEntity> prod = productServcie.findByProductName(productName);
        if (prod.isPresent()) {
            return ResponseEntity.badRequest().body("Product already exists");
        } else{
            if(categoryService.findByName(categoryName).isEmpty()){
                return ResponseEntity.badRequest().body("Category does not exist");
            }
            ProductEntity productEntity = new ProductEntity();
            productEntity.setProductName(productName);
            productEntity.setUnitPrice(unitPrice);
            productEntity.setQuantity(quantity);
            productEntity.setDescription(description);
            productEntity.setStatus(status);
            productEntity.setDiscount(discount);
            UUID uuid = UUID.randomUUID();
			if (!image.isEmpty()) {
				String uuString = uuid.toString();
				productEntity.setImages(storageService.getSorageFilename(image, uuString));
				storageService.store(image, productEntity.getImages());
			}
            productEntity.setCategory(categoryService.findByName(categoryName).orElse(null));
            productEntity.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
            productServcie.save(productEntity);
            return new ResponseEntity<Response>(new Response(true, "Thành công", productEntity), HttpStatus.OK);
        }
    }
    @PutMapping(path = "/updateProduct")
	public ResponseEntity<?> updateProduct(@Validated @RequestParam("productId") Long productId,
			@Validated @RequestParam("productName") String productName,
			@Validated @RequestParam("unitPrice") Double unitPrice,
			@Validated @RequestParam("quantity") Integer quantity,
			@Validated @RequestParam("description") String description,
			@Validated @RequestParam("categoryName") String categoryName,
			@Validated @RequestParam("image") MultipartFile image) {
		Optional<ProductEntity> optProduct = productServcie.findById(productId);
		if (optProduct.isEmpty()) {
			return new ResponseEntity<Response>(new Response(false, "Không tìm thấy sản phẩm", null),
					HttpStatus.BAD_REQUEST);
		} else if (optProduct.isPresent()) {
			if (!image.isEmpty()) {
				UUID uuid = UUID.randomUUID();
				String uuString = uuid.toString();
				optProduct.get().setImages(storageService.getSorageFilename(image, uuString));
				storageService.store(image,
						optProduct.get().getImages());
			}
			optProduct.get().setProductName(productName);
			optProduct.get().setUnitPrice(unitPrice);
			optProduct.get().setQuantity(quantity);
			optProduct.get().setDescription(description);
			optProduct.get().setCategory(categoryService.findByName(categoryName).orElse(null));
			productServcie.save(optProduct.get());
			return new ResponseEntity<Response>(new Response(true, "Cập nhật Thành công", optProduct.get()),
					HttpStatus.OK);
		}
		return null;
	}

	@DeleteMapping("/deleteProduct")
	public ResponseEntity<?> deleteProduct(@Valid @RequestParam("id") Long id) {
		Optional<ProductEntity> optProduct = productServcie.findById(id);
		if (optProduct.isEmpty()) {
			return new ResponseEntity<Response>(new Response(false, "Không tìm thấy sản phẩm", null),
					HttpStatus.BAD_REQUEST);
		} else if (optProduct.isPresent()) {
			productServcie.deleteById(id);
			return new ResponseEntity<Response>(new Response(true, "Xóa Thành công", optProduct.get()), HttpStatus.OK);
		}
		return null;
	}

}
