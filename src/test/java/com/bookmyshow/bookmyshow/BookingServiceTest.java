package com.bookmyshow.bookmyshow;
import com.bookmyshow.bookmyshow.models.*;
import com.bookmyshow.bookmyshow.repositories.ShowRepository;
import com.bookmyshow.bookmyshow.repositories.ShowSeatRepository;
import com.bookmyshow.bookmyshow.repositories.UserRepository;
import com.bookmyshow.bookmyshow.services.BookingService;
import com.bookmyshow.bookmyshow.services.PriceCalculatorService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private ShowSeatRepository showSeatRepository;

    @Mock
    private PriceCalculatorService priceCalculatorService;


    @Test
    public void bookMovie_Success() {
        // Arrange
        Long userId = 1L;
        Long showId = 1L;
        List<Long> showSeatIds = Arrays.asList(1L, 2L);

        User user = new User();
        user.setId(userId);

        Show show = new Show();
        show.setId(showId);

        ShowSeat showSeat1 = new ShowSeat();
        showSeat1.setId(1L);
        showSeat1.setStatus(ShowSeatStatus.AVAILABLE);

        ShowSeat showSeat2 = new ShowSeat();
        showSeat2.setId(2L);
        showSeat2.setStatus(ShowSeatStatus.AVAILABLE);

        List<ShowSeat> showSeats = Arrays.asList(showSeat1, showSeat2);

        Booking booking = new Booking();
        booking.setId(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(showRepository.findById(showId)).thenReturn(Optional.of(show));
        when(showSeatRepository.findAllById(showSeatIds)).thenReturn(showSeats);
        when(priceCalculatorService.calculatePrice(show, showSeats)).thenReturn(100);

        // Act
        Booking result = bookingService.bookMovie(showSeatIds, userId, showId);

        // Assert
        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
    }

    @Test(expected = RuntimeException.class)
    public void bookMovie_UserNotFound() {
        // Arrange
        Long userId = 1L;
        Long showId = 1L;
        List<Long> showSeatIds = Arrays.asList(1L, 2L);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        bookingService.bookMovie(showSeatIds, userId, showId);
    }
}

