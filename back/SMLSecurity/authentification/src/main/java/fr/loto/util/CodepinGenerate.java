package fr.loto.util;

import java.security.SecureRandom;
import java.time.Instant;

public class CodepinGenerate {
    private static final int PIN_CODE_MIN = 1000;
    private static final int PIN_CODE_MAX = 9999;
    private static final long PIN_VALIDITY_DURATION = 5 * 60 * 1000; // 5 minutes in milliseconds
    public static PinCodeWithValidity generateCodePin() {
        SecureRandom random = new SecureRandom();
        int pin = PIN_CODE_MIN + random.nextInt(PIN_CODE_MAX - PIN_CODE_MIN + 1);
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTimeMillis = currentTimeMillis + PIN_VALIDITY_DURATION;
        return new PinCodeWithValidity(pin, expirationTimeMillis);
    }
    public static class PinCodeWithValidity {
        private final Integer pinCode;
        private final long expirationTimeMillis;

        public PinCodeWithValidity(Integer pinCode, long expirationTimeMillis) {
            this.pinCode = pinCode;
            this.expirationTimeMillis = expirationTimeMillis;
        }

        public Integer getPinCode() {
            return pinCode;
        }

        public boolean isExpired() {
            return Instant.now().toEpochMilli() > expirationTimeMillis;
        }
    }
}
