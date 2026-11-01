package com.rental.dto;

import java.math.BigDecimal;

public record PaymentRequest(BigDecimal amount) {
}
