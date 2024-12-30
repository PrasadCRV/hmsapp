package com.hmsapp.repository;

import com.hmsapp.entity.Property;
import com.hmsapp.entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {
    List<PropertyImage> findByProperty(Property property);
}