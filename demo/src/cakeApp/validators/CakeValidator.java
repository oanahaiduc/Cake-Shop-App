package cakeApp.validators;

import cakeApp.domain.Cake;

public class CakeValidator<ID> {
    public void validate(Cake<ID> cake){
        validateCake(cake);
    }

    private void validateCake(Cake<ID> cake) {
        if(cake.getCakeName() == null || cake.getCakeName().isEmpty()){
            throw new IllegalArgumentException("Cake name cannot be empty!");
        }
        if(cake.getCakeFlavour() == null || cake.getCakeFlavour().isEmpty()){
            throw new IllegalArgumentException("Cake flavour cannot be empty!");
        }
        if(cake.getPrice() <= 0){
            throw new IllegalArgumentException("Price must be grater than 0!");
        }
        if(cake.getCakeLayers() <= 0){
            throw new IllegalArgumentException("Cake must have at least 1 layer!");
        }
    }
}
