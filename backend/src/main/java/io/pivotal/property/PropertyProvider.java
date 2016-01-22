package io.pivotal.property;

public interface PropertyProvider {
    Iterable<Property> find();
    Property findOne(Long id);
    Property save(Property property);
    Property update(Property property);
    void remove(Long id);
}
