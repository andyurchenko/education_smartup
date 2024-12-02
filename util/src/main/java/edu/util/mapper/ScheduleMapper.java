package edu.util.mapper;

import edu.dto.trip.DtoSchedule;
import edu.model.additional.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    Schedule toModel(DtoSchedule dtoRequest);
    DtoSchedule toModel(Schedule schedule);
}
