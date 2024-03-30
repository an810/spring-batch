package org.example.chunkprocessing.processor;

import org.example.chunkprocessing.domain.Product;
import org.springframework.batch.item.ItemProcessor;

public class FilterDataItemProcessor implements ItemProcessor<Product, Product> {
    @Override
    public Product process(Product item) throws Exception {
        System.out.println("filterDataItemProcessor() executed !");
        if (item.getProductPrice() < 1000) {
            return item;
        }
        return null;
    }
}
