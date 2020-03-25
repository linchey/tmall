package com.lin.toymall.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

public class PmsCatalog1 implements Serializable,Catalogs {
    @Id
    private  String id;
    @Column
    private  String name;
    @Transient
    private List<PmsCatalog2> catalog2List;
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

    public List<PmsCatalog2> getCatalog2List() {
        return catalog2List;
    }

    public void setCatalog2List(List<PmsCatalog2> catalog2List) {
        this.catalog2List = catalog2List;
    }
}
