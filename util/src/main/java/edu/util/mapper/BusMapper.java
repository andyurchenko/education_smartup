package edu.util.mapper;

import edu.dto.trip.bus.DtoBusRequest;
import edu.dto.trip.bus.DtoBusResponse;
import edu.dto.trip.bus.DtoBusWithIdResponse;
import edu.model.Bus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BusMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "busName", target = "brandName")
    @Mapping(source = "placeCount", target = "placeCount")
    Bus toModel(DtoBusRequest dtoBusRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "placeCount", ignore = true)
    @Mapping(source = "busName", target = "brandName")
    Bus toModel(String busName);

    @Mapping(source = "brandName", target = "busName")
    DtoBusWithIdResponse toDtoWithId(Bus bus);

    @Mapping(source = "brandName", target = "busName")
    DtoBusResponse toDto(Bus bus);

    List<DtoBusResponse> toDtoList(List<Bus> bus);
}
