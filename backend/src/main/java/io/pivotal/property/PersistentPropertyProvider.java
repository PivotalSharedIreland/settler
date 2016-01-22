package io.pivotal.property;

import org.springframework.beans.factory.annotation.Autowired;

public class PersistentPropertyProvider implements PropertyProvider {

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public Iterable<Property> find() {
        return propertyRepository.findAll();
    }

    @Override
    public Property findOne(Long id) {
        return propertyRepository.findOne(id);
    }

    @Override
    public Property save(Property property) {
        if (propertyAlreadyExists(property)) {
            throw new PropertyAlreadyExistsException("Property already exists");
        }
        return propertyRepository.save(property);
    }

    private boolean propertyAlreadyExists(Property property) {
        return property.getId() != null && propertyRepository.exists(property.getId());
    }

    @Override
    public void remove(Long id) {
        Property property = this.findOne(id);
        if (property != null) {
            propertyRepository.delete(property);
        }
    }

    @Override
    public Property update(Property property) {
        if (!propertyRepository.exists(property.getId())) {
            throw new PropertyDoesNotExistsException("Property id " + property.getId() + " does not exist");
        }
        return propertyRepository.save(property);
    }
}