package edu.util.mapper;

import edu.dto.trip.station.DtoStationRequest;
import edu.dto.trip.station.DtoStationResponse;
import edu.model.additional.Station;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StationMapper {
    StationMapper INSTANCE = Mappers.getMapper(StationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "stationName", target = "name")
    Station toModel(String stationName);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    Station toModel(DtoStationRequest dtoRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    void updateModelFromDto(@MappingTarget Station model, DtoStationRequest dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    DtoStationResponse toDto(Station station);
}
