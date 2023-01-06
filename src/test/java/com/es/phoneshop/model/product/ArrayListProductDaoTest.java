package com.es.phoneshop.model.product;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao =  ArrayListProductDao.getInstance();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts("a", SortField.description, SortOrder.asc).isEmpty());
    }

    @Test
    public void testSaveNewProduct(){
        Product testProduct = new Product();
        productDao.save(testProduct);
        assertTrue(testProduct.getId()>=0);

        Product res = productDao.getProduct(testProduct.getId());
        assertNotNull(res);
    }

    @Test
    public void testNewProductsId() {
        Currency usd = Currency.getInstance("USD");

        List<PriceHistory> priceHistories = new ArrayList<>();
        priceHistories.add(new PriceHistory(new BigDecimal(100), usd, LocalDate.now()));
        priceHistories.add(new PriceHistory(new BigDecimal(110), usd, LocalDate.now()));
        priceHistories.add(new PriceHistory(new BigDecimal(150), usd, LocalDate.now()));

        Product product1 = new Product("testproduct1", "Iphone X", new BigDecimal(100), usd, 100, "", priceHistories);
        Product product2 = new Product("testproduct2", "Iphone XL", new BigDecimal(100), usd, 100, "", priceHistories);
        productDao.save(product1);
        productDao.save(product2);

        assertEquals(1L, product2.getId() - product1.getId());
    }
}
