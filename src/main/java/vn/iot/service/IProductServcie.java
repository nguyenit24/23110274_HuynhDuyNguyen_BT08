package vn.iot.service;

import java.util.List;
import java.util.Optional;

import vn.iot.enity.ProductEntity;

public interface IProductServcie {

    long count();

    void deleteById(Long id);

    Optional<ProductEntity> findById(Long id);

    List<ProductEntity> findByProductNameContaining(String name);

    Optional<ProductEntity> findByProductName(String name);

    <S extends ProductEntity> S save(S entity);

    Object findAll();
}
