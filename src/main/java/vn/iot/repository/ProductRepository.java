package vn.iot.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.iot.enity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{
	List<ProductEntity> findByProductNameContaining(String name);
	
	Page<ProductEntity> findByProductNameContaining( String name, Pageable pageable); 
	
	Optional<ProductEntity> findByProductName(String name);
	
	Optional<ProductEntity> findByCreateDate(Date createAt);

}
