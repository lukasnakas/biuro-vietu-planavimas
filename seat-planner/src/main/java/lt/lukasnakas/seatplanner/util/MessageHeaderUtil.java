package lt.lukasnakas.seatplanner.util;

import lt.lukasnakas.seatplanner.model.exception.MessageHeaderNotFoundException;
import org.springframework.messaging.MessageHeaders;

import java.util.Optional;

public final class MessageHeaderUtil {

    private MessageHeaderUtil() {
    }

    public static <T> T getHeader(String name, Class<T> t, MessageHeaders messageHeaders) {
        return Optional.ofNullable(messageHeaders.get(name, t))
                .orElseThrow(() -> new MessageHeaderNotFoundException(String.format("Message header [%s] not found", name)));
    }

}