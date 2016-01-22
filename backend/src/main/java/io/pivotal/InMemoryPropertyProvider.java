package io.pivotal;

import java.util.List;

public class InMemoryPropertyProvider implements PropertyProvider {
    @Override
    public List<Property> find() {
        return null;
    }

    @Override
    public Property findOne(Long id) {
        return null;
    }

    @Override
    public Property save(Property property) {
        return null;
    }
}