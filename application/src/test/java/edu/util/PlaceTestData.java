package edu.util;

import edu.dto.place.DtoPlaceRequest;

public class PlaceTestData {
    public static DtoPlaceRequest createValidDtoPlaceRequest() {
        DtoPlaceRequest dto = new DtoPlaceRequest();
        dto.setPlace(1);
        dto.setFirstName("Иван");
        dto.setLastName("Иванов");
        dto.setPassport("паспорт");
        dto.setOrderId(1L);

        return dto;
    }
}
