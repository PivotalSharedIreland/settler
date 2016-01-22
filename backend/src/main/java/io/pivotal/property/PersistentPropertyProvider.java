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
        if(propertyRepository.exists(property.getId())){
            throw new PropertyAlreadyExistsException("Property already exists");
        }
        return propertyRepository.save(property);
    }
}