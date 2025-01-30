package cakeApp.repository.file.text;

import cakeApp.domain.Cake;

public class TextFileCakeRepository extends TextFileRepository<Integer, Cake<Integer>> {
    public TextFileCakeRepository(String filename) {
        super(filename);
        super.readFromFile();
    }

    @Override
    protected Cake<Integer> parseEntity(String line) {
        String[] tokens = line.split(",");

        Integer cakeId = Integer.parseInt(tokens[0].trim());
        String cakeName = tokens[1].trim();
        String cakeFlavour = tokens[2].trim();
        double cakePrice = Double.parseDouble(tokens[3].trim());
        int cakeLayers = Integer.parseInt(tokens[4].trim());

        return new Cake<>(cakeId, cakeName, cakeFlavour, cakePrice, cakeLayers);
    }

}
