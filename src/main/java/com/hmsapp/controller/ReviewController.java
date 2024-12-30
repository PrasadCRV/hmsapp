package com.hmsapp.controller;

import com.hmsapp.entity.Property;
import com.hmsapp.entity.Reviews;
import com.hmsapp.entity.User;
import com.hmsapp.repository.PropertyRepository;
import com.hmsapp.repository.ReviewsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    private PropertyRepository propertyRepository;
    private ReviewsRepository reviewsRepository;

    public ReviewController(PropertyRepository propertyRepository, ReviewsRepository reviewsRepository) {
        this.propertyRepository = propertyRepository;
        this.reviewsRepository = reviewsRepository;
    }

    @PostMapping
    public String addReview(
            @RequestBody Reviews review,
            @RequestParam long propertyId,
            @AuthenticationPrincipal User user
    ) {
        Property property = propertyRepository.findById(propertyId).get();
        Reviews reviewStatus = reviewsRepository.findByPropertyAndUser(property, user);
        if (reviewStatus != null) {
            review.setProperty(property);
            review.setUser(user);
            reviewsRepository.save(review);
            return "review added";
        }
        return "Review already exists";
    }

    @GetMapping("user/reviews")
    public List<Reviews> viewMyReviews(
            @AuthenticationPrincipal User user
    ) {
        return reviewsRepository.findByUser(user);
    }
}
