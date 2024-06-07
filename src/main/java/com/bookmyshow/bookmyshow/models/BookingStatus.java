package com.bookmyshow.bookmyshow.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public enum BookingStatus {
    CONFIRMED,
    CANCELLED,
    PENDING;
}
