package com.example.csis3175_project_rims.Helpers;

public class ProductsHelperClass {
    private String sku, name, desc;
    private int quantity;

    public ProductsHelperClass(){};

    public ProductsHelperClass(String sku, String name, String desc, int quantity) {
        this.sku = sku;
        this.name = name;
        this.desc = desc;
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
