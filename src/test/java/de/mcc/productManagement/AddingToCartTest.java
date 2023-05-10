package de.mcc.productManagement;

import de.mcc.Exceptions.NoProductWithSuchIDException;
import de.mcc.Main;
import de.mcc.cart.Cart;
import de.mcc.products.PhysicalProducts;
import de.mcc.products.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class AddingToCartTest {

    Class addingToCartClass = null;
    @BeforeEach
    void init() throws ClassNotFoundException {
        Cart.totalPrice = 0;
        Cart.cartProductMap.clear();
        Cart.cartProductMap.put(12344, 5);
        Cart.cartProductMap.put(23555, 3);

        addingToCartClass = Class.forName("de.mcc.productManagement.AddingToCart");
        Main.fillTheProductList();
    }

    @Test
    void reduceStockAmountTest() throws NoSuchMethodException, NoProductWithSuchIDException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Method method = addingToCartClass.getDeclaredMethod("reduceStockAmount", Product.class, int.class);
        method.setAccessible(true);
        int initialStockAmount = ((PhysicalProducts)ProductManager.getProductByID(12344)).getStockAmount();
        method.invoke(addingToCartClass.newInstance(), ProductManager.getProductByID(12344), 5);
        assertEquals(initialStockAmount-5, ((PhysicalProducts)ProductManager.getProductByID(12344)).getStockAmount());
    }

    @Test
    void ifProductAlreadyInCartAndUpdateQuantityTestIfArleadyInCart() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = addingToCartClass.getDeclaredMethod("ifProductAlreadyInCartAndUpdateQuantity", int.class, int.class);
        method.setAccessible(true);
        int oldAmount = Cart.cartProductMap.get(12344);
        method.invoke(addingToCartClass.getClass(), 12344, 3);
        assertEquals(oldAmount + 3, Cart.cartProductMap.get(12344));
    }

    @Test
    void ifProductAlreadyInCartAndUpdateQuantityTestIfNotInCart() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = addingToCartClass.getDeclaredMethod("ifProductAlreadyInCartAndUpdateQuantity", int.class, int.class);
        method.setAccessible(true);
        method.invoke(addingToCartClass.getClass(), 897897, 3);
        assertEquals(3, Cart.cartProductMap.get(897897));
    }

    @Test
    void checkSufficientQuantityInStockTest() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Method method = addingToCartClass.getDeclaredMethod("checkSufficientQuantityInStock", int.class, int.class);
        method.setAccessible(true);
        assertTrue((Boolean) method.invoke(addingToCartClass.newInstance(), 12344, 25));
        assertFalse((Boolean) method.invoke(addingToCartClass.newInstance(), 12344, 100));
        assertTrue((Boolean) method.invoke(addingToCartClass.newInstance(), 32534, 100));
    }
}