package com.hmsapp.controller;

import com.hmsapp.entity.Property;
import com.hmsapp.entity.PropertyImage;
import com.hmsapp.repository.PropertyImageRepository;
import com.hmsapp.repository.PropertyRepository;
import com.hmsapp.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class ImageController {

    private final S3Service s3Service;
    private PropertyRepository propertyRepository;
    private PropertyImageRepository propertyImageRepository;

    public ImageController(S3Service s3Service, PropertyRepository propertyRepository, PropertyImageRepository propertyImageRepository) {
        this.s3Service = s3Service;
        this.propertyRepository = propertyRepository;
        this.propertyImageRepository = propertyImageRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("bucketName") String bucketName) {
        String response = s3Service.uploadFile(file, bucketName);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/upload/property/{propertyId}")
    public ResponseEntity<String> uploadPropertyImage(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("bucketName") String bucketName,
                                                      @PathVariable long propertyId ) {
        Property property = propertyRepository.getById(propertyId);
        PropertyImage pi = new PropertyImage();
        pi.setProperty(property);
        String url = s3Service.uploadFile(file, bucketName);
        pi.setUrl(url);
        propertyImageRepository.save(pi);
        return ResponseEntity.ok("Image successfully uploaded - "+ url);
    }

    @GetMapping("{id}")
    public List<PropertyImage> getPropertyImages(
            @PathVariable long id
    ) {
        Property property = propertyRepository.findById(id).get();
        return propertyImageRepository.findByProperty(property);

    }

}