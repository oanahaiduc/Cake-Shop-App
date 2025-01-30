package cakeApp.repository.memory;
import cakeApp.domain.Identifiable;
import cakeApp.filters.Filter;
import cakeApp.repository.IRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryFilteredRepository<ID, T extends Identifiable<ID>> extends InMemoryRepository<ID, T> {
    private IRepository<ID, T> repository;
    private Filter<T> filter;

    public InMemoryFilteredRepository(IRepository<ID, T> repository, Filter<T> filter) {
        this.repository = repository;
        this.filter = filter;
    }

    @Override
    public Iterable<T> findAll() {
        List<T> filteredItems = new ArrayList<>();
        for(T item : this.repository.findAll()) {
            if(filter.accept(item)) {
                filteredItems.add(item);
            }
        }
        if(filteredItems.isEmpty()) {
            throw new RuntimeException("No items found!");
        }
        return filteredItems;
    }
}
