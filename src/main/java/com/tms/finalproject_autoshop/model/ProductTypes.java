package com.tms.finalproject_autoshop.model;

public class ProductTypes {
    public static final String SPARE_PART = "spare-parts";
    public static final String SERVICE_PART = "service-parts";
    public static final String OIL_PART = "oil-parts";

    public static boolean isValidProductType(ProductTypes type) {
        return SPARE_PART.equals(type) ||
                SERVICE_PART.equals(type) ||
                OIL_PART.equals(type);
    }


}
