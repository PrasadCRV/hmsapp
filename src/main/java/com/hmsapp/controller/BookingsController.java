package com.hmsapp.controller;

import com.hmsapp.entity.Booking;
import com.hmsapp.entity.Property;
import com.hmsapp.entity.RoomsAvailability;
import com.hmsapp.repository.BookingRepository;
import com.hmsapp.repository.PropertyRepository;
import com.hmsapp.repository.RoomsAvailabilityRepository;
import com.hmsapp.service.PDFGenerator;
import com.hmsapp.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingsController {
    @Autowired
    private RoomsAvailabilityRepository rAR;
    @Autowired
    private PropertyRepository pR;
    @Autowired
    private BookingRepository bR;
    @Autowired
    private PDFGenerator pdfGenerator;
    @Autowired
    private SMSService smsService;

    @GetMapping("/search/rooms")
    public ResponseEntity<?> searchRooms(
           @RequestParam LocalDate fromDate,
           @RequestParam LocalDate toDate,
           @RequestParam String roomType,
           @RequestParam long propertyId,
           @RequestBody Booking bookings

    ){
        List<RoomsAvailability> availableRooms = rAR.findAvailableRooms(fromDate, toDate, roomType, propertyId);

        Property property = pR.findById(propertyId).get();
        for(RoomsAvailability r : availableRooms){
            if(r.getTotalRooms()==0){
                return new ResponseEntity<>("No rooms available", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        bookings.setProperty(property);
        Booking saved = bR.save(bookings);
        pdfGenerator.generatePDF("D:\\test\\sample"+"_"+saved.getGuestName()+".pdf",saved);

//        String s = smsService.sendSMS("+917676150798", "Room booked!");
//        System.out.println(s);
        return new ResponseEntity<>(availableRooms, HttpStatus.OK);
    }

}
