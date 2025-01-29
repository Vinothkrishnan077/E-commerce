package com.assessment.ecommerce.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.assessment.ecommerce.model.Product;
import com.assessment.ecommerce.model.User;
import com.assessment.ecommerce.repository.ProductRepository;
import com.assessment.ecommerce.repository.UserRepository;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String name, String category, Double minPrice, Double maxPrice) {
        return productRepository.searchProducts(name, category, minPrice, maxPrice);
    }

    public Product createProduct(Product product, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid email:"));
        if (user == null || user.getRole().equals("User")) {
            throw new RuntimeException("Only Admin, SuperAdmin, or Staff can create products");
        }
        if (productRepository.existsByProductUrl(product.getProductUrl())) {
            throw new RuntimeException("Product URL must be unique");
        }
        if (product.isFreeDelivery()) {
            product.setDeliveryAmount(0.0); // Force deliveryAmount to 0 for free delivery
        } else if (product.getDeliveryAmount() <= 100) {
            throw new RuntimeException("Delivery amount must be greater than 100 when freeDelivery is false");
        }
        product.setVendor_id(user.getUserId());
        product.setStartDate(LocalDateTime.now());
        product.setExpiryDate(product.getStartDate().plusDays(7));
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Map<String, Object> values) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        values.forEach((key, value) -> {
            switch (key) {
                case "name":
                    product.setName((String) value);
                    break;
                case "description":
                    product.setDescription((String) value);
                    break;
                case "category":
                    product.setCategory((String) value);
                    break;
                case "startDate":
                    product.setStartDate(LocalDateTime.parse((String) value));
                    product.setExpiryDate(LocalDateTime.parse((String) value).plusDays(7)); // Update expiry date too
                    break;
                case "freeDelivery":
                    product.setFreeDelivery((Boolean) value);
                    break;
                case "deliveryAmount":
                    product.setDeliveryAmount(Double.parseDouble(value.toString()));
                    break;
                case "oldPrice":
                    product.setOldPrice(Double.parseDouble(value.toString()));
                    break;
                case "newPrice":
                    product.setNewPrice(Double.parseDouble(value.toString()));
                    break;
                case "productUrl":
                    if (productRepository.existsByProductUrl((String) value) &&
                            !product.getProductUrl().equals(value)) {
                        throw new RuntimeException("Product URL must be unique");
                    }
                    product.setProductUrl((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });
        return productRepository.saveAndFlush(product);
    }

    public Product updateProduct(Long productId, MultipartFile productImage)
            throws IOException {
        Optional<Product> product = productRepository.findById(productId);
        Product productImages = product.get();
        productImages.setImages(productImage.getBytes());
        return productRepository.saveAndFlush(productImages);
    }

}
