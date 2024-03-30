package org.example.chunkprocessing.domain;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class Product {
    private Integer productId;
    private String productName;
    @Pattern(regexp = "Mobile Phones|Tablets|Televisions|Sports Accessories")
    private String productCategory;
    @Max(100000)
    private Integer productPrice;

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName=" + productName +
                ", productCategory=" + productCategory +
                ", productPrice=" + productPrice +
                '}';
    }
}
