package de.mcc.productManagement;

import de.mcc.products.Product;

import java.util.ArrayList;
import java.util.List;

public class SearchingForProduct {

    public static List<Product> searchForProducts(String preferableProductName) {
        List<Product> suitableProduct = new ArrayList<>();
        for (Product product : ProductManager.productList) {
            if (product.getClass().getSimpleName().equals(preferableProductName)) {
                suitableProduct.add(product);
            }
        }
        return suitableProduct;
    }
}
