package io.pivotal.property;

import com.google.common.collect.Lists;
import io.pivotal.error.EntityAlreadyExistsException;
import io.pivotal.error.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PersistentPropertyProvider implements PropertyProvider {

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public List<Property> findAll() {
        return Lists.newArrayList(propertyRepository.findAll());
    }

    @Override
    public Property findOne(Long id) {
        return propertyRepository.findOne(id);
    }

    @Override
    public Property save(Property property) {
        if (propertyAlreadyExists(property)) {
            throw new EntityAlreadyExistsException("Property already exists");
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
            throw new EntityNotFoundException("Property id " + property.getId() + " does not exist");
        }
        return propertyRepository.save(property);
    }
}