package org.zerohan.exzero.sample;


import org.springframework.stereotype.Component;

@Component
public class Restaurant {

    private final Chef chef;

    public Restaurant(Chef chef) {
        this.chef = chef;
    }

    @Override
    public String toString() {
        return "Restaurant(" +
              "chef=" + chef +
              ')';
    }
}
