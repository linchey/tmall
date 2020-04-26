package com.lin.toymall.Service;


import com.lin.toymall.bean.*;

import java.util.List;

public interface CatalogService {
    List<PmsCatalog1> getCatalog1();

    List<PmsCatalog2> getCatalog2(String catalog1Id);

    List<PmsCatalog3> getCatalog3(String catalog2Id);

    List<PmsCatalog2> getCatalog2();

    List<PmsCatalog3> getCatalog3();

    PageBean wrapPage(int totalCount, int currentPage);

    PageBean<PmsCatalog1> findByCatalog1Page(int currentPage);

    PageBean<PmsCatalog2> findByCatalog2Page(int currentPage);

    PageBean<PmsCatalog3> findByCatalog3Page(int currentPage);

    void saveCatalog3(PmsCatalog3 catalog3);

    void delCatalog3(String catalog3Id);

    String findNameByCatlog2Id(String catalog2Id);

    void modifyCatalog3(PmsCatalog3 catalog3);

    boolean isNoExist(String catalog2Id);

    String findIdByName(String catalog2Name);

    String findNameByCatlog1Id(String catalog1Id);

    void delCatalog2(String catalog2Id);

    String findIdByCatalog1Name(String catalog1Name);

    void modifyCatalog2(PmsCatalog2 catalog2);

    void saveCatalog2(PmsCatalog2 catalog2);

    boolean isNoExistCatalog1(String catalog1Id);

    void saveCatalog1(PmsCatalog1 catalog1);

    void modifyCatalog1(PmsCatalog1 catalog1);

    void delCatalog1(String catalog1Id);

    String findNameByCatlog3Id(String catalog3Id);
}
