package io.pivotal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/properties")
public class PropertiesController {

    private PropertyProvider propertyProvider;

    @Autowired
    public PropertiesController(PropertyProvider propertyProvider) {
        this.propertyProvider = propertyProvider;
    }

    private final Map<Long, Property> properties = new HashMap<Long, Property>() {{
        for (long currentId = 0; currentId < 3; currentId++) {
            Property p = new Property();
            p.setId(currentId);
            p.setAddress("Address " + currentId);
            put(p.getId(), p);
        }
    }};

    @RequestMapping
    public List<Property> getList() {
        return propertyProvider.find();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Property> get(@PathVariable Long id) {
        Property property = propertyProvider.findOne(id);
        HttpStatus statusCode = exists(property) ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(property, statusCode);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Property> save(@RequestBody Property property) {
        HttpStatus statusCode = property.getId() == null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

        if (property.getId() == null) {
            property = propertyProvider.save(property);
        }

        return new ResponseEntity<>(property, statusCode);
    }

    private boolean exists(Property property) {
        return property != null;
    }

    // delete

    // update
}
