package com.Tech2.bookingrequest;


import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Tech2.bookingrequest.BookingRequest.Status;

public interface BookingRequestRepository extends JpaRepository<BookingRequest,Long> {

    Page<BookingRequest> findByStatus(Status status, PageRequest pageRequest);

}
