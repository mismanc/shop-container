package com.simple.webshop.services;

import com.simple.webshop.domain.Product;
import com.simple.webshop.model.ProductDTO;
import com.simple.webshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findWithBrandAllBy();
        return products.stream().map(p -> new ProductDTO(p.getId(), p.getName(), p.getPrice(), p.getBrand().getName())).toList();
    }
}
