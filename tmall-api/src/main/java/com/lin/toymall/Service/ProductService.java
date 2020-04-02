package com.lin.toymall.Service;

import com.lin.toymall.bean.*;

import java.util.List;

public interface ProductService {
    void addProduct(PmsProduct product);

    String findIdByProName(String productName);

    void addProductImag(PmsProductImage productImage);

    boolean isExitProduct(String productName);
    PageBean<PmsProduct> findProductByPage(int currPage);
    PageBean wrapPage(int totalCount, int currentPage);

    void modifyProName(String id, String newValue);

    void modifyProPrice(String id, String newValue);

    void modifyProStock(String id, String newValue);

    void modifyProNote(String id, String newValue);

    void delProduct(String id);

    List<PmsProduct> qurProduct(String catalog1Id, String catalog2Id, String catalog3Id);

    void delProductImges(String id);

    List<PmsProductImage> findImgeByProId(String productId);

    PmsProductImage findImageById(String id);

    PmsProduct findProductById(String productId);

    void modifyDefImg(PmsProduct product);

    void delImge(String id);

    PmsProduct findProByName(String name);

    PmsCatalog1 findCatalog1ById(String id);

    PmsCatalog2 findCatalog2ById(String id);

    PmsCatalog3 findCatalog3ById(String id);
}
