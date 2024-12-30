package com.hmsapp.controller;

import com.hmsapp.entity.Property;
import com.hmsapp.repository.PropertyRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {
    private PropertyRepository propertyRepository;

    public PropertyController(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }
    @PostMapping("/addProperty")
    public String addProperty() {
        // Add property logic here
        return "Property added successfully";
    }
    @DeleteMapping("/deleteProperty")
    public String deleteProperty() {
        // Add property logic here
        return "Property deleted successfully";
    }
    @GetMapping("/{searchParam}")
    public List<Property> searchProperty(@PathVariable String searchParam) {
        return propertyRepository.searchProperty(searchParam);
    }
}
