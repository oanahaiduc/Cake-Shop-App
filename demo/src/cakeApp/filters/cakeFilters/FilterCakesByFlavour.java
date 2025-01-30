package cakeApp.filters.cakeFilters;

import cakeApp.domain.Cake;
import cakeApp.filters.Filter;


public class FilterCakesByFlavour<ID> implements Filter<Cake<ID>> {
    private final String filterFlavour;

    public FilterCakesByFlavour(String filterFlavour) {
        this.filterFlavour = filterFlavour;
    }

    @Override
    public boolean accept(Cake<ID> cake){
        return cake.getCakeFlavour().equalsIgnoreCase(filterFlavour);
    }
}
