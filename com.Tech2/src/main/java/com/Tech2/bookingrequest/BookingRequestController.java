package com.Tech2.bookingrequest;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Tech2.bookingrequest.BookingRequest.Status;

@RestController
@CrossOrigin(origins = "*") 
@RequestMapping("/booking-requests")
@Validated
public class BookingRequestController {

    @Autowired
    private BookingRequestRepository repository;

    @PostMapping
    public ResponseEntity<BookingRequest> create(@Valid @RequestBody BookingRequest request) {
        request.setStatus(Status.PENDING);
        return ResponseEntity.ok(repository.save(request));
    }

    @GetMapping
    public Page<BookingRequest> getPending(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return repository.findByStatus(Status.PENDING, PageRequest.of(page, size));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
    	BookingRequest  request=repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("BookingRequest not found:"+id)); 
        if (request.getStatus() != Status.PENDING) return ResponseEntity.badRequest().body("Already processed");
        request.setStatus(Status.APPROVED);
        return ResponseEntity.ok(repository.save(request));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id, @RequestBody Map<String, String> payload) {
    	String reason = payload.get("reason");
    	BookingRequest request = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("BookingRequest not found:"+id)); 
        if (request.getStatus() != Status.PENDING) return ResponseEntity.badRequest().body("Already processed");
        request.setStatus(Status.REJECTED);
        request.setRejectionReason(reason);
        return ResponseEntity.ok(repository.save(request));
    }
}
