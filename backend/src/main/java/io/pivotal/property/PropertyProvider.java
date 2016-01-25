package io.pivotal.property;

import java.util.List;

public interface PropertyProvider {
    List<Property> findAll();
    Property findOne(Long id);
    Property save(Property property);
    Property update(Property property);
    void remove(Long id);
}
