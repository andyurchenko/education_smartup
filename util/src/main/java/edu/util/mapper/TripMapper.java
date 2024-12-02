package edu.util.mapper;

import edu.dto.trip.DtoTripRequest;
import edu.dto.trip.DtoTripResponse;
import edu.model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mapper(componentModel = "spring", uses = {BusMapper.class, StationMapper.class, ScheduleMapper.class, TripDateMapper.class})
public interface TripMapper {
    TripMapper INSTANCE = Mappers.getMapper(TripMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fromStation", ignore = true)
    @Mapping(target = "toStation", ignore = true)
    @Mapping(target = "bus", ignore = true)
    @Mapping(target = "approved", ignore = true)
    @Mapping(target = "tripDates", ignore = true)
    Trip toModel(DtoTripRequest dtoRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fromStation", ignore = true)
    @Mapping(target = "toStation", ignore = true)
    @Mapping(target = "bus", ignore = true)
    @Mapping(target = "approved", ignore = true)
    @Mapping(target = "tripDates", ignore = true)
    void updateModelFromDto(@MappingTarget Trip model, DtoTripRequest dto);

    @Mapping(source = "trip.id", target = "tripId")
    @Mapping(source = "trip.bus", target = "bus")
    @Mapping(source = "trip.fromStation.name", target = "fromStation")
    @Mapping(source = "trip.toStation.name", target = "toStation")
    @Mapping(source = "trip.start", target = "start")
    @Mapping(source = "trip.duration", target = "duration")
    @Mapping(source = "trip.price", target = "price")
    @Mapping(source = "trip.approved", target = "approved")
    @Mapping(source = "trip.schedule", target = "schedule")
    @Mapping(source = "trip.tripDates", target = "dates")
    DtoTripResponse toDtoForAdmins(Trip trip);

    @Mapping(source = "trip.id", target = "tripId")
    @Mapping(source = "trip.bus", target = "bus")
    @Mapping(source = "trip.fromStation.name", target = "fromStation")
    @Mapping(source = "trip.toStation.name", target = "toStation")
    @Mapping(source = "trip.start", target = "start")
    @Mapping(source = "trip.duration", target = "duration")
    @Mapping(source = "trip.price", target = "price")
    @Mapping(target = "approved", ignore = true)
    @Mapping(source = "trip.schedule", target = "schedule")
    @Mapping(source = "trip.tripDates", target = "dates")
    DtoTripResponse toDtoForClients(Trip trip);

    default List<DtoTripResponse> toDtoListForAdmins(List<Trip> trips) {
        return trips.stream().map(this::toDtoForAdmins).toList();
    }

    default List<DtoTripResponse> toDtoListForClients(List<Trip> trips) {
        return trips.stream().map(this::toDtoForClients).toList();
    }



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
        return duration.toHours() + ":" + duration.toMinutesPart();
    }
}
