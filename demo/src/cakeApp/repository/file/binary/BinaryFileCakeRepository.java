package cakeApp.repository.file.binary;

import cakeApp.domain.Cake;

public class BinaryFileCakeRepository<Integer> extends BinaryFileRepository<Integer, Cake<Integer>> {
  public BinaryFileCakeRepository(String filename) {
    super(filename);
  }
}
