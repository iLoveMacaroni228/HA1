package de.mcc.productManagement;

import de.mcc.Exceptions.NoProductWithSuchIDException;
import de.mcc.Exceptions.NoProductsInCartException;
import de.mcc.Main;
import de.mcc.cart.Cart;
import de.mcc.customers.Customer;
import de.mcc.customers.CustomerManager;
import de.mcc.products.PhysicalProducts;
import de.mcc.products.Product;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@Log
class CheckingOutTest {

    Customer customer = null;
    Class checkingOutClass = null;
    @BeforeEach
    void init() throws ClassNotFoundException {
        Cart.totalPrice = 0;
        Cart.cartProductMap.clear();
        Cart.cartProductMap.put(12344, 5);
        Cart.cartProductMap.put(23555, 3);

        CustomerManager.customerList.add(new Customer("Nazar", "Goerzallee 131", "nazar.gorelik@gmail.com"));
        checkingOutClass = Class.forName("de.mcc.productManagement.CheckingOut");
        customer = CustomerManager.customerList.get(0);
        Main.fillTheProductList();
    }

    @Test
    void checkoutOrderForEmailProductsWithEmailProducts() throws  NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Method method = checkingOutClass.getDeclaredMethod("checkoutOrderForEmailProducts");
        method.setAccessible(true);
        assertEquals(String.valueOf(new StringBuilder("3 of 23555 ")),String.valueOf(method.invoke(checkingOutClass.newInstance())));
    }

    @Test
    void checkoutOrderForEmailProductsWithNoEmailProducts() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        DeletingFromCart.deleteProductFromCart(23555, 3);
        Method method = checkingOutClass.getDeclaredMethod("checkoutOrderForEmailProducts");
        method.setAccessible(true);
        assertEquals(String.valueOf(new StringBuilder("")),String.valueOf(method.invoke(checkingOutClass.newInstance())));
    }

    @Test
    void checkoutOrderForPhysicalProductsWithPhysicalProducts() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Method method = checkingOutClass.getDeclaredMethod("checkoutOrderForPhysicalProducts");
        method.setAccessible(true);
        assertEquals(String.valueOf(new StringBuilder("5 of 12344 ")),String.valueOf(method.invoke(checkingOutClass.newInstance())));
    }

    @Test
    void checkoutOrderForPhysicalProductsWithNoPhysicalProducts() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        DeletingFromCart.deleteProductFromCart(12344, 5);
        Method method = checkingOutClass.getDeclaredMethod("checkoutOrderForPhysicalProducts");
        method.setAccessible(true);
        assertEquals(String.valueOf(new StringBuilder("")),String.valueOf(method.invoke(checkingOutClass.newInstance())));
    }

    @Test
    void combineFinalOrderStringWithPhysical() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        StringBuilder st = new StringBuilder("Telling postal operator to send 5 of 12344  to " + customer.getUserName() + "," + customer.getAdress());
        DeletingFromCart.deleteProductFromCart(23555, 3);
        Method method = checkingOutClass.getDeclaredMethod("combineFinalOrderString");
        method.setAccessible(true);
        assertEquals(String.valueOf(st), String.valueOf(method.invoke(checkingOutClass.newInstance())));
    }

    @Test
    void combineFinalOrderStringWithEmail() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        StringBuilder st = new StringBuilder("\nSending the Link to the digital download 3 of 23555 to " + customer.getEmail());
        DeletingFromCart.deleteProductFromCart(12344, 5);
        Method method = checkingOutClass.getDeclaredMethod("combineFinalOrderString");
        method.setAccessible(true);
        assertEquals(String.valueOf(st), String.valueOf(method.invoke(checkingOutClass.newInstance())));
    }

    @Test
    void combineFinalOrderStringWithPhysicalAndEmail() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        StringBuilder st = new StringBuilder("Telling postal operator to send 5 of 12344  to " + customer.getUserName() + "," +
                customer.getAdress());
        st.append("\nSending the Link to the digital download 3 of 23555 to " + customer.getEmail());
        Method method = checkingOutClass.getDeclaredMethod("combineFinalOrderString");
        method.setAccessible(true);
        assertEquals(String.valueOf(st), String.valueOf(method.invoke(checkingOutClass.newInstance())));
    }

    @Test
    void countDeliveryPriceTest() throws  NoSuchMethodException, NoProductWithSuchIDException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Method method = checkingOutClass.getDeclaredMethod("countDeliveryPrice");
        method.setAccessible(true);
        Product product = ProductManager.getProductByID(12344);
        assertEquals((float)((PhysicalProducts)product).getWeight() * Cart.cartProductMap.get(12344),method.invoke(checkingOutClass.newInstance()));
    }

    @Test
    void countThePriceWithoutDeliveryTest() throws  NoSuchMethodException, NoProductWithSuchIDException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Method method = checkingOutClass.getDeclaredMethod("countThePriceWithoutDelivery");
        method.setAccessible(true);
        Product product1 = ProductManager.getProductByID(12344);
        Product product2 = ProductManager.getProductByID(23555);
        float firstPrice = (float)product1.getPrice() * Cart.cartProductMap.get(12344);
        float secondPrice = (float)product2.getPrice() * Cart.cartProductMap.get(23555);
        assertEquals(firstPrice + secondPrice, method.invoke(checkingOutClass.newInstance()));
    }

    //source https://www.baeldung.com/java-lang-reflect-invocationtargetexception
    @Test
    void countThePriceWithoutDeliveryTestWithoutProducts() throws NoSuchMethodException {
        Cart.cartProductMap.clear();
        Method method = checkingOutClass.getDeclaredMethod("countThePriceWithoutDelivery");
        method.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, ()->method.invoke(checkingOutClass.newInstance()));
        assertEquals(NoProductsInCartException.class, exception.getCause().getClass());
    }
}