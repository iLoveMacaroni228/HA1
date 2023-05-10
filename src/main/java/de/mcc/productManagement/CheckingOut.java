package de.mcc.productManagement;

import de.mcc.Exceptions.NoProductWithSuchIDException;
import de.mcc.Exceptions.NoProductsInCartException;
import de.mcc.cart.Cart;
import de.mcc.customers.Customer;
import de.mcc.customers.CustomerManager;
import de.mcc.products.PhysicalProducts;
import de.mcc.products.Product;
import lombok.extern.java.Log;

import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

@Log
public class CheckingOut {

    public static void checkout(Logger log, Scanner scanner) {
        try {
            getPriceInfo(log);
            getOrderInfo(log,scanner);
        } catch (NoProductsInCartException e) {
            log.warning(e.getMessage());
        }
    }

    private static void getPriceInfo(Logger log) throws NoProductsInCartException {
        float priceWithoutDelivery = 0;
        float priceForDelivery = 0;
        try {
            priceWithoutDelivery = countThePriceWithoutDelivery();
            log.info("The price of the products is " + priceWithoutDelivery + ".");
            priceForDelivery = countDeliveryPrice();
        } catch (NoProductWithSuchIDException e) {
            log.warning(e.getMessage());
        }
        log.info("The shipping of physical products costs " + priceForDelivery + " (1€ pro KG).");
        float totalCost = priceWithoutDelivery + priceForDelivery;
        log.info("Total cost is " + totalCost);
    }

    private static float countThePriceWithoutDelivery() throws NoProductWithSuchIDException, NoProductsInCartException {
        for(Map.Entry<Integer, Integer> entry : Cart.cartProductMap.entrySet()){
            Cart.totalPrice+=ProductManager.getProductByID(entry.getKey()).getPrice() * entry.getValue();
        }
        if(Cart.totalPrice == 0){
            throw new NoProductsInCartException("You haven't got anything in your cart");
        }
        return Cart.totalPrice;
    }

    private static float countDeliveryPrice() throws NoProductWithSuchIDException {
        float weight = 0;
        float priceForKilo = 1;
        for(Map.Entry<Integer, Integer> entry : Cart.cartProductMap.entrySet()){
            Product product = ProductManager.getProductByID(entry.getKey());
            if(product instanceof PhysicalProducts){
                weight+=((PhysicalProducts) product).getWeight() * entry.getValue();
            }
        }
        return weight *priceForKilo;
    }


    private static void getOrderInfo(Logger log, Scanner scanner){
        log.warning("Do you want to continue (Y/n)?");
        String answer = scanner.next();
        if(answer.equals("Y")){
            log.warning("Please enter your IBAN to charge:");
            String iban = scanner.next();
            Customer customer = CustomerManager.customerList.get(0);
            log.info("\nTelling payment processor to charge " +Cart.totalPrice +  " from IBAN: " + iban +  " with name: "
                    + customer.getUserName() + " at Address: " + customer.getAdress() + "\n" +
                    String.valueOf(combineFinalOrderString()));
        }else if(answer.equals("n")){
            log.info("See you soon");
        }else{
            log.info("Wrong message");
        }
    }

    private static StringBuilder combineFinalOrderString(){
        StringBuilder st = new StringBuilder();
        StringBuilder physicalOrder = null;
        StringBuilder emailOrder = null;
        try {
            physicalOrder = checkoutOrderForPhysicalProducts();
            emailOrder = checkoutOrderForEmailProducts();
        } catch (NoProductWithSuchIDException e) {
            log.warning(e.getMessage());
        }
        Customer customer = CustomerManager.customerList.get(0);

        if(!(physicalOrder.isEmpty())){
            st.append("Telling postal operator to send ");
            st.append(physicalOrder);
            st.append(" to " + customer.getUserName() + "," + customer.getAdress() + "\n");
        }
        if(!(emailOrder.isEmpty())){
            st.append("Sending the Link to the digital download ");
            st.append(emailOrder);
            st.append("to " + customer.getEmail());
        }
        return st;
    }

    private static StringBuilder checkoutOrderForPhysicalProducts() throws NoProductWithSuchIDException {
        StringBuilder st = new StringBuilder();
        for(Map.Entry<Integer, Integer> entry: Cart.cartProductMap.entrySet()){
            Product product = ProductManager.getProductByID(entry.getKey());
            if(product instanceof PhysicalProducts){
                st.append(entry.getValue() + " of " + entry.getKey() + " ");
            }
        }
        return st;
    }

    private static StringBuilder checkoutOrderForEmailProducts() throws NoProductWithSuchIDException {
        StringBuilder st = new StringBuilder();
        for(Map.Entry<Integer, Integer> entry: Cart.cartProductMap.entrySet()){
            Product product = ProductManager.getProductByID(entry.getKey());
            if(!(product instanceof PhysicalProducts)){
                st.append(entry.getValue() + " of " + entry.getKey() + " ");
            }
        }
        return st;
    }
}
