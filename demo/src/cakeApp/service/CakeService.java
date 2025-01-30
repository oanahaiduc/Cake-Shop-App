package cakeApp.service;

import cakeApp.domain.Cake;
import cakeApp.filters.cakeFilters.FilterCakesByFlavour;
import cakeApp.filters.cakeFilters.FilterCakeByPriceRange;
import cakeApp.repository.IRepository;
import cakeApp.repository.memory.InMemoryFilteredRepository;
import cakeApp.validators.CakeValidator;

import java.util.List;
import java.util.Optional;

public class CakeService {
    private IRepository<Integer, Cake<Integer>> cakeRepository;
    private CakeValidator<Integer> cakeValidator;
    private Integer currentCakeId;

    public CakeService(IRepository<Integer, Cake<Integer>> repository) {
        this.cakeRepository = repository;
        this.currentCakeId = ((List<Cake<Integer>>)cakeRepository.findAll()).size()+1;
        this.cakeValidator = new CakeValidator<>();
    }

    public Integer addCake(Cake<Integer> cake) {
        cakeValidator.validate(cake);
        cake.setId(currentCakeId++);
        cakeRepository.add(cake);
        return cake.getId();
    }

    public Iterable<Cake<Integer>> getCakes() {
        return cakeRepository.findAll();
    }

    public Optional<Cake<Integer>> getCakeByID(Integer idToGet) {
        return cakeRepository.findById(idToGet);
    }

    public void updateCake(Cake<Integer> cake) {
        cakeValidator.validate(cake);
        if(cakeRepository.findById(cake.getId()).isEmpty()) {
            throw new IllegalArgumentException("Cake with ID " + cake.getId() + " does not exist");
        }
        cakeRepository.modify(cake);
    }

    public void deleteCake(Integer idToDelete) {
        if(cakeRepository.findById(idToDelete).isEmpty()){
            throw new IllegalArgumentException("Cake does not exist");
        }
        cakeRepository.delete(idToDelete);
    }

    public Iterable<Cake<Integer>> filterByFlavour(String filterFlavour) {
        FilterCakesByFlavour<Integer> flavourFilter = new FilterCakesByFlavour<>(filterFlavour);

        InMemoryFilteredRepository<Integer, Cake<Integer>> filteredRepository =
                new InMemoryFilteredRepository<>(cakeRepository, flavourFilter);
        return filteredRepository.findAll();
    }

    public Iterable<Cake<Integer>> filterByPriceRange(double minPrice, double maxPrice) {
        if(minPrice < 0 || maxPrice < 0){
            throw new IllegalArgumentException("Price cannot be negative!");
        }
        if(minPrice > maxPrice){
            throw new IllegalArgumentException("Min price cannot be greater than max price!");
        }

        FilterCakeByPriceRange<Integer> priceFilter =
                new FilterCakeByPriceRange<>(minPrice, maxPrice);

        InMemoryFilteredRepository<Integer, Cake<Integer>> filteredRepository = new InMemoryFilteredRepository<>(cakeRepository, priceFilter);

        return filteredRepository.findAll();
    }

}
