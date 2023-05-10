package de.mcc.productManagement;

import de.mcc.Exceptions.NoProductWithSuchIDException;
import de.mcc.Main;
import de.mcc.products.MP3;
import de.mcc.products.T_Shirt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductManagerTest {

    ProductManager productManager = null;

    @BeforeEach
    void init(){
        productManager = new ProductManager();
        Main.fillTheProductList();
    }

    @Test
    void getProductByIDCheckID() throws NoProductWithSuchIDException {
        assertEquals(new MP3(8678, "Relaxing Ocean Sounds", 10.5,
                List.of("Electronics", "Music", "MP3")).getId(), ProductManager.getProductByID(8678).getId());
    }
    @Test
    void getProductByIDCheckName() throws NoProductWithSuchIDException {
        assertEquals(new T_Shirt(12344,"I'd rather be sleeping",16.55, 0.62, 56,
                List.of("Clothing", "T-Shirt", "Cotton")).getName(), ProductManager.getProductByID(12344).getName());
    }

    @Test
    void getProductByIDCheckException(){
        assertThrows(NoProductWithSuchIDException.class,()-> ProductManager.getProductByID(1111));
    }
}