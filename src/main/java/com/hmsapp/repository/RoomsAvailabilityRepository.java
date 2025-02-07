package com.hmsapp.repository;

import com.hmsapp.entity.RoomsAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomsAvailabilityRepository extends JpaRepository<RoomsAvailability, Long> {

    @Query("SELECT r FROM RoomsAvailability r " +
            "WHERE r.date BETWEEN :fromDate AND :toDate " +
            "AND r.roomType = :roomType " +
            "AND r.property.id = :propertyId")
    List<RoomsAvailability> findAvailableRooms(@Param("fromDate") LocalDate fromDate,
                                               @Param("toDate") LocalDate toDate,
                                               @Param("roomType") String roomType,
                                               @Param("propertyId") Long propertyId);

}