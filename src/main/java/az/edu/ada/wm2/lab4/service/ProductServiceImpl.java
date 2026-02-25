package az.edu.ada.wm2.lab4.service;

import az.edu.ada.wm2.lab4.model.Product;
import az.edu.ada.wm2.lab4.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

private final ProductRepository productRepository;

//constructor injection
public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
}
    @Override
    public Product createProduct(Product product) {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID());
        }
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(UUID id) {
        Optional<Product> opt = productRepository.findById(id);

        if (opt.isPresent()) {
            return opt.get(); // product exists
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(UUID id, Product product) {
        if (id == null || !productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        product.setId(id);
        return productRepository.save(product);

    }

    @Override
    public void deleteProduct(UUID id) {
    if (id == null || !productRepository.existsById(id)) {
        throw new RuntimeException("Product not found with id: " + id);
    }
    productRepository.deleteById(id);
    }

    @Override
    public List<Product> getProductsExpiringBefore(LocalDate date) {
        return productRepository.findAll().stream()
                .filter(p -> p.getExpirationDate() != null && p.getExpirationDate().isBefore(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findAll().stream()
                .filter(p -> p.getPrice() != null)
                .filter(p -> p.getPrice().compareTo(minPrice) >= 0
                        && p.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }
}
