package io.pivotal;

import java.util.List;

public interface PropertyProvider {
    List<Property> find();
    Property findOne(Long id);
    Property save(Property property);
}
