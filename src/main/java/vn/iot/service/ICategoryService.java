package vn.iot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.iot.enity.CategoryEntity;
import vn.iot.repository.CategoryRepository;

public interface ICategoryService {

	<S extends CategoryEntity> S save(S entity);

	<S extends CategoryEntity> Optional<S> findOne(Example<S> example);

	Page<CategoryEntity> findByNameContaining(String name, Pageable pageable);

	List<CategoryEntity> findByNameContaining(String name);

	Optional<CategoryEntity> findById(Long id);

	List<CategoryEntity> findAll(Sort sort);

	Page<CategoryEntity> findAll(Pageable pageable);

	List<CategoryEntity> findAll();

	void deleteById(Long id);

	void deleteAllById(Iterable<? extends Long> ids);

	<S extends CategoryEntity> long count(Example<S> example);

	long count();

	
}
