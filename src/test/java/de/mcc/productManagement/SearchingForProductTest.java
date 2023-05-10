package de.mcc.productManagement;

import de.mcc.Main;
import de.mcc.products.MP3;
import de.mcc.products.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchingForProductTest {
    List<Product> productList = null;

    @BeforeEach
    void init(){
        Main.fillTheProductList();
        productList = new ArrayList<>();
        productList.add(new MP3(32534, "Retro Beats", 8.99,
                List.of("Electronics", "Music", "MP3")));
        productList.add(new MP3(8678, "Relaxing Ocean Sounds", 10.5,
                List.of("Electronics", "Music", "MP3")));
        productList.add(new MP3(23555, "Meditation Melodies", 4.5,
                List.of("Electronics", "Music", "MP3")));
        productList.add(new MP3(768678, "Classical Favorites", 6,
                List.of("Electronics", "Music", "MP3")));
    }

    @Test
    void searchForProductsTest() {
        List<Product> preferableProductList = SearchingForProduct.searchForProducts("MP3");
        for(int i=0;i<productList.size();i++){
            assertEquals(productList.get(i).getId(), preferableProductList.get(i).getId());
        }
    }
}