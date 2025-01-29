package com.assessment.ecommerce.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String name;
    private String description;
    private String category;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;
    private Boolean freeDelivery;
    private double deliveryAmount;
    @Column(columnDefinition = "LONGBLOB")
    private byte[] images;
    private double oldPrice;
    private double newPrice;
    private String productUrl;

    @Transient
    private double discountAmount;

    @Transient
    private double discountPercentage;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vendor_id", insertable = false, updatable = false, referencedColumnName = "userId")
    private User vendor;
    private Long vendor_id;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean isFreeDelivery() {
        return freeDelivery;
    }

    public void setFreeDelivery(Boolean freeDelivery) {
        this.freeDelivery = freeDelivery;
    }

    public double getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(double deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public double getOldPrice() {
        return truncateToTwoDecimalPlaces(oldPrice);
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = truncateToTwoDecimalPlaces(oldPrice);
    }

    public double getNewPrice() {
        return truncateToTwoDecimalPlaces(newPrice);
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = truncateToTwoDecimalPlaces(newPrice);
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public User getVendor() {
        return vendor;
    }

    public void setVendor(User vendor) {
        this.vendor = vendor;
    }

    public Long getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(Long vendor_id) {
        this.vendor_id = vendor_id;
    }

    public byte[] getImages() {
        return images;
    }

    public void setImages(byte[] images) {
        this.images = images;
    }

    public double getDiscountAmount() {
        return truncateToTwoDecimalPlaces(oldPrice - newPrice);
    }

    public double getDiscountPercentage() {
        return truncateToTwoDecimalPlaces(((oldPrice - newPrice) / oldPrice) * 100);
    }

    private double truncateToTwoDecimalPlaces(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        return bd.setScale(2, RoundingMode.DOWN).doubleValue();
    }

}
