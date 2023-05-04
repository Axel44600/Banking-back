package com.apply.banking.dto;

import com.apply.banking.entity.Address;
import com.apply.banking.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AddressDto {

    private Long id;

    private String street;

    private Integer houseNumber;

    private Integer zipCode;

    private String city;

    private String country;

    private Long userId;

    public static AddressDto fromEntity(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .city(address.getCity())
                .houseNumber(address.getHouseNumber())
                .zipCode(address.getZipCode())
                .city(String.valueOf(address.getZipCode()))
                .country(address.getCountry())
                .userId(address.getUser().getId())
                .build();
    }

    public static Address toEntity(AddressDto address) {
        return Address.builder()
                .id(address.getId())
                .city(address.getCity())
                .houseNumber(address.getHouseNumber())
                .zipCode(address.getZipCode())
                .city(String.valueOf(address.getZipCode()))
                .country(address.getCountry())
                .user(User.builder().id(address.getId()).build())
                .build();
    }
}
