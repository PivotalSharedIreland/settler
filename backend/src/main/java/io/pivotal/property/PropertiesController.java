package io.pivotal.property;

import io.pivotal.error.ForbiddenActionException;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/properties")
public class PropertiesController {

    private final PropertyManager propertyManager;

    @Autowired
    public PropertiesController(@NotNull PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    @RequestMapping
    public List<Property> getList() { return propertyManager.findAll(); }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Property> get(@PathVariable Long id) {
        Property property = propertyManager.findOne(id);
        HttpStatus statusCode = exists(property) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(property, statusCode);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(CREATED)
    public Property save(@Valid @RequestBody Property property) {
          return propertyManager.save(property);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        propertyManager.remove(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Property update(@PathVariable Long id, @RequestBody Property property) {
        if(!Objects.equals(id, property.getId())){
            throw new ForbiddenActionException("Request body does not match the property id");
        }
        return propertyManager.update(property);
    }

    private boolean exists(Property property) {
        return property != null;
    }
}
