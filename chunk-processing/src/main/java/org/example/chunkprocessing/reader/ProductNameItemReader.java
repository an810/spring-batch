package org.example.chunkprocessing.reader;

import org.springframework.batch.item.NonTransientResourceException;

import org.springframework.batch.item.ItemReader;
import java.rmi.UnexpectedException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

public class ProductNameItemReader implements ItemReader<String> {

    private Iterator<String> productListIterator;
    public ProductNameItemReader(List<String> productList) {
        this.productListIterator = productList.iterator();
    }
    @Override
    public String read() throws Exception, UnexpectedException, ParseException, NonTransientResourceException {
        return this.productListIterator.hasNext() ? this.productListIterator.next() : null;
    }
}
