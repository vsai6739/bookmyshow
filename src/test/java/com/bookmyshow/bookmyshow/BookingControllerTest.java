package com.bookmyshow.bookmyshow;

import com.bookmyshow.bookmyshow.controllers.BookingController;
import com.bookmyshow.bookmyshow.dto.BookMovieRequestDto;
import com.bookmyshow.bookmyshow.dto.BookMovieResponseDto;
import com.bookmyshow.bookmyshow.dto.ResponseStatus;
import com.bookmyshow.bookmyshow.models.Booking;
import com.bookmyshow.bookmyshow.services.BookingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    @Test
    public void bookMovie_Success() {
        // Arrange
        BookMovieRequestDto request = new BookMovieRequestDto();
        request.setShowSeatIds(Arrays.asList(1L, 2L));
        request.setUserId(1L);
        request.setShowId(1L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setAmount(100);

        when(bookingService.bookMovie(anyList(), anyLong(), anyLong())).thenReturn(booking);

        // Act
        BookMovieResponseDto response = bookingController.bookMovie(request);

        // Assert
        assertEquals(ResponseStatus.SUCCESS, response.getStatus());
        assertEquals(booking.getId(), response.getBookingId());
        assertEquals(booking.getAmount(), response.getAmount());
    }

    @Test
    public void bookMovie_Failure() {
        // Arrange
        BookMovieRequestDto request = new BookMovieRequestDto();
        request.setShowSeatIds(Arrays.asList(1L, 2L));
        request.setUserId(1L);
        request.setShowId(1L);

        when(bookingService.bookMovie(anyList(), anyLong(), anyLong())).thenThrow(new RuntimeException());

        // Act
        BookMovieResponseDto response = bookingController.bookMovie(request);

        // Assert
        assertEquals(ResponseStatus.FAILURE, response.getStatus());
    }
}
