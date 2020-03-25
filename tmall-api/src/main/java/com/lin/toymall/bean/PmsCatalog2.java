package com.lin.toymall.bean;

import javax.persistence.Id;
import javax.persistence.Transient;

import java.io.Serializable;
import java.util.List;

public class PmsCatalog2 implements Serializable,Catalogs {
    @Id
    private  String id;
    private  String name;
    private  String catalog1Id;
    @Transient
    private  String catalog1Name;

    public String getCatalog1Name() {
        return catalog1Name;
    }

    public void setCatalog1Name(String catalog1Name) {
        this.catalog1Name = catalog1Name;
    }

    @Transient
    private List<PmsCatalog3> catalog3List;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalog1Id() {
        return catalog1Id;
    }

    public void setCatalog1Id(String catalog1Id) {
        this.catalog1Id = catalog1Id;
    }

    public List<PmsCatalog3> getCatalog3List() {
        return catalog3List;
    }

    public void setCatalog3List(List<PmsCatalog3> catalog3List) {
        this.catalog3List = catalog3List;
    }
}
