package com.lin.toymall.Service;

import com.lin.toymall.bean.PageBean;
import com.lin.toymall.bean.PmsProduct;
import com.lin.toymall.bean.PmsProductImage;

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
}
