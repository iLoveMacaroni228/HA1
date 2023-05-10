package de.mcc.productManagement;

import de.mcc.Exceptions.InsufficientQuantityException;
import de.mcc.Exceptions.NoProductWithSuchIDException;
import de.mcc.Exceptions.ProductNotInCartException;
import de.mcc.cart.Cart;
import de.mcc.products.PhysicalProducts;
import de.mcc.products.Product;
import lombok.extern.java.Log;

@Log
public class DeletingFromCart {

    public static void deleteProductFromCart(int productID, int quantity) {
        Product product;
        try{
            product = ProductManager.getProductByID(productID);
            if(Cart.cartProductMap.containsKey(product.getId())){
                if(checkSufficientQuantityInCart(product, quantity) == false){
                    throw new InsufficientQuantityException("Quantity of product with ID: " + productID + " in cart is insufficient");
                }
                reduceCartAmount(product, quantity);
                increaseStockAmount(product, quantity);
            }else{
                throw new ProductNotInCartException("Product with ID: " + productID + " is not in the card!");
            }
        } catch (ProductNotInCartException e) {
            log.warning(e.getMessage());
        } catch (InsufficientQuantityException e) {
            log.warning(e.getMessage());
        } catch (NoProductWithSuchIDException e) {
            log.warning(e.getMessage());
        }
    }

    private static void increaseStockAmount(Product product, int quantity) throws NoProductWithSuchIDException {
        if(product instanceof PhysicalProducts){
            int oldStockAmount = ((PhysicalProducts)ProductManager.getProductByID(product.getId())).getStockAmount();
            ((PhysicalProducts)ProductManager.getProductByID(product.getId())).setStockAmount(oldStockAmount + quantity);
        }
    }

    private static boolean checkSufficientQuantityInCart(Product product, int quantity){
        if(Cart.cartProductMap.get(product.getId()) >= quantity){
            return true;
        }else{
            return false;
        }
    }

    private static void reduceCartAmount(Product product, int quantity) {
        int oldCartAmount = Cart.cartProductMap.get(product.getId());
        if (oldCartAmount - quantity == 0) {
            Cart.cartProductMap.remove(product.getId());
        } else {
            Cart.cartProductMap.put(product.getId(), oldCartAmount - quantity);
        }
    }
}
