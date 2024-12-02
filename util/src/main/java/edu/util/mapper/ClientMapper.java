package edu.util.mapper;

import edu.dto.client.*;
import edu.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userType", constant = "CLIENT")
    @Mapping(target = "accountActive", ignore = true)
    Client toModelFromRegisterDtoRequest(DtoRegisterNewClientRequest dtoRequest);

    DtoRegisterNewClientResponse toDtoRegisterResponse(Client client);

    DtoGetClientInfoResponse toDtoGetInfoResponse(Client client);
    DtoClientResponse toDtoLoginResponse(Client client);
    DtoUpdateClientResponse toDtoUpdateInfoResponse(Client client);
    List<DtoGetClientInfoResponse> toDtoGetClientListResponse(List<Client> clients);
}
