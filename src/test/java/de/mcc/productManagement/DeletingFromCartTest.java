package de.mcc.productManagement;

import de.mcc.Exceptions.NoProductWithSuchIDException;
import de.mcc.Main;
import de.mcc.cart.Cart;
import de.mcc.products.MP3;
import de.mcc.products.PhysicalProducts;
import de.mcc.products.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeletingFromCartTest {

    DeletingFromCart deletingFromCart = null;

    @BeforeEach
    void init() {
        deletingFromCart = new DeletingFromCart();
        Main.fillTheProductList();
        Cart.cartProductMap.put(768678, 2);
    }

    @Test
    void reduceCartAmountTestNull() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        assertEquals(Cart.cartProductMap.get(768678), 2);
        Class deletingClass = Class.forName("de.mcc.productManagement.DeletingFromCart");
        Method method = deletingClass.getDeclaredMethod("reduceCartAmount", Product.class, int.class);
        method.setAccessible(true);
        method.invoke(deletingClass.newInstance(), new MP3(768678, "Classical Favorites", 6,
                List.of("Electronics", "Music", "MP3")), 2);
        assertEquals(Cart.cartProductMap.get(768678), null);
    }

    @Test
    void reduceCartAmountTestNotNull() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        assertEquals(Cart.cartProductMap.get(768678), 2);
        Class deletingClass = Class.forName("de.mcc.productManagement.DeletingFromCart");
        Method method = deletingClass.getDeclaredMethod("reduceCartAmount", Product.class, int.class);
        method.setAccessible(true);
        method.invoke(deletingClass.newInstance(), new MP3(768678, "Classical Favorites", 6,
                List.of("Electronics", "Music", "MP3")), 1);
        assertEquals(Cart.cartProductMap.get(768678), 1);
    }

    @Test
    void checkSufficientQuantityInCartYes() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class deletingClass = Class.forName("de.mcc.productManagement.DeletingFromCart");
        Method method = deletingClass.getDeclaredMethod("checkSufficientQuantityInCart", Product.class, int.class);
        method.setAccessible(true);
        assertTrue((Boolean) method.invoke(deletingClass.newInstance(), new MP3(768678, "Classical Favorites", 6,
                List.of("Electronics", "Music", "MP3")), 1));
    }

    @Test
    void checkSufficientQuantityInCartNot() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class deletingClass = Class.forName("de.mcc.productManagement.DeletingFromCart");
        Method method = deletingClass.getDeclaredMethod("checkSufficientQuantityInCart", Product.class, int.class);
        method.setAccessible(true);
        assertFalse((Boolean) method.invoke(deletingClass.newInstance(), new MP3(768678, "Classical Favorites", 6,
                List.of("Electronics", "Music", "MP3")), 3));
    }

    @Test
    void increaseStockAmountTest() throws NoProductWithSuchIDException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        assertEquals(((PhysicalProducts)ProductManager.getProductByID(14567)).getStockAmount(), 82);
        Class deletingClass = Class.forName("de.mcc.productManagement.DeletingFromCart");
        Method method = deletingClass.getDeclaredMethod("increaseStockAmount", Product.class, int.class);
        method.setAccessible(true);
        method.invoke(deletingClass.newInstance(), ProductManager.getProductByID(14567), 4);
        assertEquals(((PhysicalProducts)ProductManager.getProductByID(14567)).getStockAmount(), 86);
    }

    @Test
    void deleteProductFromCartTest(){
        assertEquals(Cart.cartProductMap.get(768678), 2);
        DeletingFromCart.deleteProductFromCart(768678, 1);
        assertEquals(Cart.cartProductMap.get(768678), 1);
    }

    @Test
    void deleteProductFromCartTest1(){
        assertEquals(Cart.cartProductMap.get(768678), 2);
        DeletingFromCart.deleteProductFromCart(768678, 3);
        assertEquals(Cart.cartProductMap.get(768678), 2);
    }

    @Test
    void deleteProductFromCartTest2(){
        assertEquals(Cart.cartProductMap.get(768678), 2);
        DeletingFromCart.deleteProductFromCart(1111, 2);
        assertEquals(Cart.cartProductMap.get(768678), 2);
    }

    @Test
    void deleteProductFromCartTest3(){
        assertEquals(Cart.cartProductMap.get(768678), 2);
        DeletingFromCart.deleteProductFromCart(897897, 1);
        assertEquals(Cart.cartProductMap.get(768678), 2);
    }
}