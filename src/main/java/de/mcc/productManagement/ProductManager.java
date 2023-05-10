package de.mcc.productManagement;

import de.mcc.Exceptions.NoProductWithSuchIDException;
import de.mcc.cart.Cart;
import de.mcc.products.Product;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

@Log
public class ProductManager {
    public static List<Product> productList = new ArrayList<>();

    public static Product getProductByID(int productID) throws NoProductWithSuchIDException {
        for (Product p : productList) {
            if (p.getId() == productID) {
                return p;
            }
        }
        throw new NoProductWithSuchIDException("Product with ID: " + productID + " does not exist");
    }

    //DELETE
    public void deleteProductFromCartImpl(Logger log, Scanner scanner){
        log.warning("Product ID: ");
        int productID = scanner.nextInt();
        log.warning("Quantity: ");
        int quantity = scanner.nextInt();
        DeletingFromCart.deleteProductFromCart(productID, quantity);
        log.info("Your current cart is: " + Cart.cartProductMap);
    }

    //CHECKOUT
    public void checkout(Logger log, Scanner scanner){
        CheckingOut.checkout(log, scanner);
    }

    //ADD
    public void addProductToCartImpl(Logger log, Scanner scanner){
        log.info("Product ID: ");
        int productID = scanner.nextInt();
        log.info("Quantity: ");
        int quantity = scanner.nextInt();
        AddingToCart.addProductToCart(productID, quantity);
        log.info("Your current cart is: " + Cart.cartProductMap);
    }

    //SEARCH
    public void searchForProductsImpl(Logger log, Scanner scanner){
        log.info("Input a search item: ");
        String preferableProductName = scanner.next();
        List<Product> suitableProductList = SearchingForProduct.searchForProducts(preferableProductName);
        listAllProducts(suitableProductList);
    }

    //LIST
    public void listAllProducts(List<?> list) {
        StringBuilder st = new StringBuilder("\n");
        for(Object o:list){
            st.append(o.toString() + "\n");
        }
        log.info(String.valueOf(st));
    }
}
