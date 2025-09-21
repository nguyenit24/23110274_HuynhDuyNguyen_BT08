package vn.iot.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.common.util.StringUtils;

import vn.iot.enity.ProductEntity;
import vn.iot.repository.ProductRepository;
import vn.iot.service.IProductServcie;

@Service
public class ProductServiceImpl implements IProductServcie {

    @Autowired
    private ProductRepository productRepository;
    @Override
    public long count() {
        return productRepository.count();
    }
    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
    @Override
    public Optional<ProductEntity> findById(Long id) {
        return productRepository.findById(id);
    }
    @Override
    public List<ProductEntity> findByProductNameContaining(String name) {
        return productRepository.findByProductNameContaining(name);
    }
    @Override
    public Optional<ProductEntity> findByProductName(String name) {
        return productRepository.findByProductName(name);
    }

    @Override
    public <S extends ProductEntity> S save(S entity) {
        if (entity.getProductId() == null) {
			return productRepository.save(entity);
		} else {
			Optional<ProductEntity> opt = findById(entity.getProductId());
			if (opt.isPresent()) {
				if (StringUtils.isEmpty(entity.getImages())) {
					entity.setImages(opt.get().getImages());
				} else {
					entity.setImages(entity.getImages());
				}
			}
		}
        return productRepository.save(entity);
    }
    @Override
    public Object findAll() {
        return productRepository.findAll();
    }

}
