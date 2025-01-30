package cakeApp.repository.file.binary;

import cakeApp.domain.Order;

public class BinaryFileOrderRepository<Integer> extends BinaryFileRepository<Integer,Order<Integer>>{
    public BinaryFileOrderRepository(String filename) {
        super(filename);
    }
}
