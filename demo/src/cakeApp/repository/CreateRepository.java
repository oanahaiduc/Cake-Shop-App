package cakeApp.repository;

import cakeApp.domain.Cake;
import cakeApp.domain.Order;

import cakeApp.repository.database.CakeDBRepository;
import cakeApp.repository.database.OrderDBRepository;
import cakeApp.repository.file.binary.BinaryFileCakeRepository;
import cakeApp.repository.file.binary.BinaryFileOrderRepository;
import cakeApp.repository.file.text.TextFileCakeRepository;
import cakeApp.repository.file.text.TextFileOrderRepository;
import cakeApp.repository.memory.InMemoryCakeRepository;
import cakeApp.repository.memory.InMemoryOrderRepository;


public class CreateRepository {
    public static IRepository<Integer, Cake<Integer>> createCakeRepository(String repositoryType, String filename) {
        return switch (repositoryType.toLowerCase()) {
            case "binary" -> new BinaryFileCakeRepository(filename);
            case "text" -> new TextFileCakeRepository(filename);
            case "database" -> new CakeDBRepository(filename);
            default -> new InMemoryCakeRepository();
        };
    }

    public static IRepository<Integer, Order<Integer>> createOrderRepository(String repositoryType, String filename) {
        return switch (repositoryType.toLowerCase()) {
            case "binary" -> new BinaryFileOrderRepository(filename);
            case "text" -> new TextFileOrderRepository(filename);
            case "database" -> new OrderDBRepository(filename);
            default -> new InMemoryOrderRepository();
        }
        ;
    }
}
