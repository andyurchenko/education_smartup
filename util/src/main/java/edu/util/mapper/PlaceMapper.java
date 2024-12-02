package edu.util.mapper;

import edu.dto.place.DtoPlaceRequest;
import edu.dto.place.DtoPlaceResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PlaceMapper {
    PlaceMapper INSTANCE = Mappers.getMapper(PlaceMapper.class);

    @Mapping(source = "dtoPlaceRequest.orderId", target = "orderId")
    @Mapping(source = "dtoPlaceRequest.firstName", target = "firstName")
    @Mapping(source = "dtoPlaceRequest.lastName", target = "lastName")
    @Mapping(source = "dtoPlaceRequest.passport", target = "passport")
    @Mapping(source = "dtoPlaceRequest.place", target = "place")
    @Mapping(source = "ticket", target = "ticket")
    DtoPlaceResponse toDtoResponse(DtoPlaceRequest dtoPlaceRequest, String ticket);
}