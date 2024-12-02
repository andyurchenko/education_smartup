package edu.util.mapper;

import edu.dto.order.DtoOrderRequest;
import edu.dto.order.DtoOrderResponse;
import edu.model.Client;
import edu.model.Order;
import edu.model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mapper(componentModel = "spring", uses = {PassengerMapper.class, TripDateMapper.class})
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(source = "dtoOrderRequest.date", target = "tripDate")
    Order toModel(DtoOrderRequest dtoOrderRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(source = "trip", target = "trip")
    @Mapping(source = "dtoOrderRequest.date", target = "tripDate")
    @Mapping(source = "dtoOrderRequest.passengers", target = "passengers")
    @Mapping(source = "client", target = "client")
    Order toModel(DtoOrderRequest dtoOrderRequest, Trip trip, Client client);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.trip.id", target = "tripId")
    @Mapping(source = "order.trip.fromStation.name", target = "fromStation")
    @Mapping(source = "order.trip.toStation.name", target = "toStation")
    @Mapping(source = "order.trip.bus.brandName", target = "busName")
    @Mapping(source = "order.tripDate.date", target = "date")
    @Mapping(source = "order.trip.start", target = "start")
    @Mapping(source = "order.trip.duration", target = "duration")
    @Mapping(source = "order.trip.price", target = "price")
    @Mapping(source = "order.totalPrice", target = "totalPrice")
    @Mapping(source = "order.passengers", target = "passengers")
    DtoOrderResponse toDto(Order order);

    List<DtoOrderResponse> toDtoList(List<Order> order);

    default Duration toDuration(String str) {
        Duration duration = null;
        Pattern pattern = Pattern.compile("^(?<hours>\\d{2}):(?<minutes>\\d{2})$");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            int hours = Integer.parseInt(matcher.group("hours"));
            int minutes = Integer.parseInt(matcher.group("minutes"));
            duration = Duration.ofHours(hours);
            duration = duration.plusMinutes(minutes);
        }

        return duration;
    }

    default String toString(Duration duration) {
        long hours = duration.toHours();
        int minutes = duration.toMinutesPart();
        String s;
        if (hours < 10) {
            s = "0" + hours + ":";
        } else {
            s = hours + ":";
        }

        if (minutes < 10) {
            s = s + "0" + minutes;
        } else {
            s = s + minutes;
        }

        return s;
    }

}
