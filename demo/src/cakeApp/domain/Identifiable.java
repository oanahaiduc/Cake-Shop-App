package cakeApp.domain;

public interface Identifiable<ID> {
    ID getId();
    void setId(ID id);
}
