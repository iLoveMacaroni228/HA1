package de.mcc.productManagement;

import de.mcc.Exceptions.InsufficientQuantityException;
import de.mcc.Exceptions.InvalidIDException;
import de.mcc.Exceptions.NoProductWithSuchIDException;
import de.mcc.cart.Cart;
import de.mcc.products.PhysicalProducts;
import de.mcc.products.Product;
import lombok.extern.java.Log;

@Log
public class AddingToCart {

    public static void addProductToCart(int productID, int quantity) {
        try {
            if (ProductManager.getProductByID(productID) == null) {
                throw new InvalidIDException("The entered ID does not exist");
            }
            if (checkSufficientQuantityInStock(productID, quantity) == false) {
                throw new InsufficientQuantityException("Insufficient quantity of item with ID: " + productID);
            }
            reduceStockAmount(ProductManager.getProductByID(productID), quantity);
            ifProductAlreadyInCartAndUpdateQuantity(productID, quantity);
        } catch (InvalidIDException e) {
            log.warning(e.getMessage());
        } catch (InsufficientQuantityException e) {
            log.warning(e.getMessage());
        } catch (NoProductWithSuchIDException e) {
            log.warning(e.getMessage());
        }
    }

    private static boolean checkSufficientQuantityInStock(int productID, int quantity) {
        Product product = null;
        try {
            product = ProductManager.getProductByID(productID);
        } catch (NoProductWithSuchIDException e) {
            log.warning(e.getMessage());
        }
        if(product instanceof PhysicalProducts){
            if(((PhysicalProducts) product).getStockAmount() >= quantity){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }

    private static void ifProductAlreadyInCartAndUpdateQuantity(int productID, int quantity){
        if(Cart.cartProductMap.containsKey(productID)){
            Cart.cartProductMap.put(productID, Cart.cartProductMap.get(productID) + quantity);
        }else{
            Cart.cartProductMap.put(productID, quantity);
        }
    }

    private static void reduceStockAmount(Product product, int quantity){
        if(product instanceof PhysicalProducts){
            int oldStockAmount = 0;
            try {
                oldStockAmount = ((PhysicalProducts)ProductManager.getProductByID(product.getId())).getStockAmount();
                ((PhysicalProducts)ProductManager.getProductByID(product.getId())).setStockAmount(oldStockAmount - quantity);
            } catch (NoProductWithSuchIDException e) {
                log.warning(e.getMessage());
            }
        }
    }
}
