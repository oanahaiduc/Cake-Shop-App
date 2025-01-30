package cakeApp.repository;

import cakeApp.domain.Identifiable;

import java.util.Optional;

public interface IRepository<ID, T extends Identifiable<ID>> {
    ID add(T item);

    Iterable<T> findAll();

    Optional<T> findById(ID id);

    void modify(T updatedItem);

    void delete(ID id);
}
