package edu.util.mapper;

import edu.model.additional.TripDate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface TripDateMapper {
    TripDateMapper INSTANCE = Mappers.getMapper(TripDateMapper.class);
    default String toString(TripDate tripDate) {
        return tripDate.getDate().toString();
    }

    default TripDate toTripDate(String date) {
        TripDate tripDate = new TripDate();
        tripDate.setDate(LocalDate.parse(date));

        return tripDate;
    }
}
