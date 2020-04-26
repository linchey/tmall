package com.lin.toymall.bean;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PmsProduct implements Serializable {
    @Id
    private String id;
    private  String productName;
    private BigDecimal price;
    private  String stock;
    private String catalog3Id;
    private String catalog2Id;
    private String catalog1Id;
    private  String note;
    private String defaultImg;



    @Transient
    public ArrayList<String> productImages;

    public ArrayList<String> getProductImages() {
        return productImages;
    }
    @Transient
    public List<PmsProductImage> pmsProductImageList;

    public List<PmsProductImage> getPmsProductImageList() {
        return pmsProductImageList;
    }

    public void setPmsProductImageList(List<PmsProductImage> pmsProductImageList) {
        this.pmsProductImageList = pmsProductImageList;
    }

    public void setProductImages(ArrayList<String> productImages) {
        this.productImages = productImages;
    }

    public String getCatalog2Id() {
        return catalog2Id;
    }

    public void setCatalog2Id(String catalog2Id) {
        this.catalog2Id = catalog2Id;
    }

    public String getCatalog1Id() {
        return catalog1Id;
    }

    public void setCatalog1Id(String catalog1Id) {
        this.catalog1Id = catalog1Id;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String getDefaultImg() {
        return defaultImg;
    }

    public void setDefaultImg(String defaultImg) {
        this.defaultImg = defaultImg;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
