package io.pivotal.property;

public interface PropertyProvider {
    Iterable<Property> find();
    Property findOne(Long id);
    Property save(Property property);
}
