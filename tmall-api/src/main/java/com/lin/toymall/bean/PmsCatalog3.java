package com.lin.toymall.bean;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

public class PmsCatalog3 implements Serializable,Catalogs {
    @Id
    private  String id;
    private  String name;
    private  String catalog2Id;
    @Transient
    private  String catalog2Name;

    public String getCatalog2Name() {
        return catalog2Name;
    }

    public void setCatalog2Name(String catalog2Name) {
        this.catalog2Name = catalog2Name;
    }

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

    public String getCatalog2Id() {
        return catalog2Id;
    }

    public void setCatalog2Id(String catalog2Id) {
        this.catalog2Id = catalog2Id;
    }
}
