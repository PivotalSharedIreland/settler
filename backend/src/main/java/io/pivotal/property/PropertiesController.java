package io.pivotal.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/properties")
public class PropertiesController {

    private PropertyProvider propertyProvider;

    @Autowired
    public PropertiesController(PropertyProvider propertyProvider) {
        this.propertyProvider = propertyProvider;
    }

    @RequestMapping
    public Iterable<Property> getList() {
        return propertyProvider.find();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Property> get(@PathVariable Long id) {
        Property property = propertyProvider.findOne(id);
        HttpStatus statusCode = exists(property) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(property, statusCode);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(CREATED)
    public Property save(@Valid @RequestBody Property property) {
          return propertyProvider.save(property);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        propertyProvider.remove(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Property update(@PathVariable Long id, @RequestBody Property property) {
        if(!Objects.equals(id, property.getId())){
            throw new ForbiddenActionException("Request body does not match the property id");
        }
        return propertyProvider.update(property);
    }

    private boolean exists(Property property) {
        return property != null;
    }

    @ExceptionHandler(PropertyAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    private void handlePropertyAlreadyExistException(){
        //TODO for something
    }

    @ExceptionHandler(PropertyDoesNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void handlePropertyDoesNotExistsException(){
        //TODO for something
    }

    @ExceptionHandler(ForbiddenActionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    private void handleForbiddenException(){
        //TODO for something
    }

}