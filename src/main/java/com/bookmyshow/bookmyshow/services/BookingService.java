package com.bookmyshow.bookmyshow.services;

import com.bookmyshow.bookmyshow.models.*;
import com.bookmyshow.bookmyshow.repositories.BookingRepository;
import com.bookmyshow.bookmyshow.repositories.ShowRepository;
import com.bookmyshow.bookmyshow.repositories.ShowSeatRepository;
import com.bookmyshow.bookmyshow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class BookingService {

    private UserRepository userRepository;
    private ShowRepository showRepository;
    private ShowSeatRepository showSeatRepository;
    private BookingRepository bookingRepository;
    private PriceCalculatorService priceCalculatorService;
    private RedisTemplate<String, Objects> redisTemplate;

    @Autowired
    public BookingService(UserRepository userRepository,
                          ShowRepository showRepository,
                          ShowSeatRepository showSeatRepository,
                          BookingRepository bookingRepository,
                          PriceCalculatorService priceCalculatorService,
                          RedisTemplate redisTemplate) {
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
        this.bookingRepository = bookingRepository;
        this.priceCalculatorService = priceCalculatorService;
        this.redisTemplate = redisTemplate;
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)

    public Booking bookMovie(List<Long> showSeatIds, Long userId, Long showId) {

        /*
            1. Get the user using the userId
            2. Get the show using the showId
            3. Fetch the ShowSeats using the showSeatIds
            4. Check if all the seats are available or not
            5. If not, throw error
            6. If yes, mark the show seats status as LOCKED
            7. Save the updated show seats in the database
            8. Create a booking object
            9. Save the booking object
            10. Return the saved booking object
        */

        // Step 1
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User bookedBy = userOptional.get();


        // Step 2
        Show redisShow = (Show) redisTemplate.opsForHash().get("SHOW","SHOW_"+showId);
        Show bookedShow;
        if(redisShow!=null){
            bookedShow = redisShow;
        }
        Optional<Show> showOptional = showRepository.findById(showId);

        if (showOptional.isEmpty()) {
            throw new RuntimeException("Show not found");
        }

        bookedShow = showOptional.get();

        // Step 3
        List<ShowSeat> showSeats = showSeatRepository.findAllById(showSeatIds);

        // Step 4

        for (ShowSeat showSeat : showSeats) {
            if (!isShowSeatAvailable(showSeat)) {
                throw new RuntimeException("Some of the show seats are not available!");
            }

            showSeat.setStatus(ShowSeatStatus.BLOCKED);
        }

        List<ShowSeat> updateShowSeats = showSeatRepository.saveAll(showSeats);

        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setShowSeats(updateShowSeats);
        booking.setUser(bookedBy);
        booking.setBookedAt(new Date());
        booking.setShow(bookedShow);
        booking.setAmount(priceCalculatorService.calculatePrice(bookedShow, updateShowSeats));
        booking.setPayments(new ArrayList<>());

        return bookingRepository.save(booking);
    }

    private boolean isShowSeatAvailable(ShowSeat showSeat) {
        return showSeat.getStatus().equals(ShowSeatStatus.AVAILABLE) ||
                (showSeat.getStatus().equals(ShowSeatStatus.BLOCKED) &&
                        ChronoUnit.MINUTES.between(new Date().toInstant(), showSeat.getBlockedAt().toInstant()) > 15);
    }
}
