package cakeApp.filters;

public interface Filter<T>{
    boolean accept(T entity);
}
