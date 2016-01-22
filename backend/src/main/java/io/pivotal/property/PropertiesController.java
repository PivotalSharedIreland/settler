package io.pivotal.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

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

    private boolean exists(Property property) {
        return property != null;
    }

    @ExceptionHandler(PropertyAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    private void handlePropertyAlreadyExistException(PropertyAlreadyExistsException ex){
        //TODO for something
    }
}
