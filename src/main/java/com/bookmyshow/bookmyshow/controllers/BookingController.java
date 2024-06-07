package com.bookmyshow.bookmyshow.controllers;


import com.bookmyshow.bookmyshow.dto.BookMovieRequestDto;
import com.bookmyshow.bookmyshow.dto.BookMovieResponseDto;
import com.bookmyshow.bookmyshow.dto.ResponseStatus;
import com.bookmyshow.bookmyshow.models.Booking;
import com.bookmyshow.bookmyshow.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller // Just a tag, using which Spring get to know that it has to create an object for it in the spring registry
public class BookingController {

    private BookingService bookingService;

    @Autowired  // Automatically find the objects of the parameters, created it, send it to function ahead
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public BookMovieResponseDto bookMovie(BookMovieRequestDto request) {

        BookMovieResponseDto bookMovieResponseDto = new BookMovieResponseDto();
        try {
            Booking booking = bookingService.bookMovie(request.getShowSeatIds(), request.getUserId(), request.getShowId());

            bookMovieResponseDto.setBookingId(booking.getId());
            bookMovieResponseDto.setAmount(booking.getAmount());
            bookMovieResponseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            bookMovieResponseDto.setStatus(ResponseStatus.FAILURE);
        }
        return bookMovieResponseDto;
    }
}
