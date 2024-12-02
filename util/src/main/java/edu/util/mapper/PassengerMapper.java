package edu.util.mapper;

import edu.dto.order.DtoPassenger;
import edu.dto.place.DtoPlaceRequest;
import edu.model.additional.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    PassengerMapper INSTANCE = Mappers.getMapper(PassengerMapper.class);
    @Mapping(target = "id", ignore = true)
    Passenger toModel(DtoPassenger dtoPassenger);
    Passenger toModel(DtoPlaceRequest dtoPassenger);
    DtoPassenger toDto(Passenger passenger);
}
