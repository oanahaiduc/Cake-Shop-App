package cakeApp.filters.cakeFilters;

import cakeApp.domain.Cake;
import cakeApp.filters.Filter;

public class FilterCakeByPriceRange<ID> implements Filter<Cake<ID>> {
    private final double minPrice;
    private final double maxPrice;

    public FilterCakeByPriceRange(double minPrice, double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public boolean accept(Cake<ID> cake) {
        return cake.getPrice() >= minPrice && cake.getPrice() <= maxPrice;
    }
}
