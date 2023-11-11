package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.BookingDataException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveRequest(long requesterId, ItemRequestDto itemRequestDto) {
        return post("", requesterId, itemRequestDto);
    }

    public ResponseEntity<Object> getAllRequesterRequests(long requesterId) {
        return get("", requesterId);
    }

    public ResponseEntity<Object> getRequesterItemRequest(long requesterId, long requestId) {
        return get("/" + requestId, requesterId);
    }

    public ResponseEntity<Object> getAllRequests(long requesterId, int from, int size) {
        if (from < 0 || size < 0) {
            throw new IllegalArgumentException("not valid data");
        }
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", requesterId, parameters);
    }
}
