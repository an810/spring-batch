package org.example.chunkprocessing.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OSProduct extends Product{
    private Integer taxPercent;
    private String sku;
    private Integer shippingRate;

    @Override
    public String toString() {
        return "OSProduct{" +
                "taxPercent=" + taxPercent +
                ", sku= " + sku +
                ", shippingRate=" + shippingRate +
                "} " + super.toString();
    }
}
