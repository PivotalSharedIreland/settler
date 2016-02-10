package io.pivotal.property;

import io.pivotal.contact.Contact;
import io.pivotal.contact.ContactsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

@Transactional
public class PropertyManager {

    private PropertyProvider propertyProvider;
    public ContactsProvider contactProvider;

    @Autowired
    public PropertyManager(@NotNull PropertyProvider propertyProvider, @NotNull ContactsProvider contactProvider) {
        this.propertyProvider = propertyProvider;
        this.contactProvider = contactProvider;
    }

    public List<Property> findAll() {
        return propertyProvider.findAll();
    }

    public Property findOne(Long id) {
        return propertyProvider.findOne(id);
    }

    public Property save(Property property) {
        property.getContacts().forEach((contact) -> {
            if (contact.getId() == null) {
                Contact saved = contactProvider.save(contact);
                contact.setId(saved.getId());
            }
        });
        return propertyProvider.save(property);
    }

    public void remove(Long id) {
        propertyProvider.remove(id);
    }

    public Property update(Property property) {
        return propertyProvider.update(property);
    }


}


//the same contact twice for one property