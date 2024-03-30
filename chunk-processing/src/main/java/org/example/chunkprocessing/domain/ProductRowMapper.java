package org.example.chunkprocessing.domain;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {
        System.out.println("ResultSet object = " + resultSet);
        Product product = new Product();
        product.setProductId(resultSet.getInt("product_id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setProductCategory(resultSet.getString("product_category"));
        product.setProductPrice(resultSet.getInt("product_price"));
        return product;
    }
}
