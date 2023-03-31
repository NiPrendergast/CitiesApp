package com.app.cities.service;

import com.app.cities.DTO.CityResponseDto;
import com.app.cities.DTO.CityUpdateRequestDto;
import com.app.cities.model.City;
import com.app.cities.repo.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CityService {

    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;

    public List<CityResponseDto> findByName(String name) {
        log.info("getting city with name {}", name);
        List<City> cities = cityRepository.findByNameLike(name);
        List<CityResponseDto> citiesDTOs = cities.stream()
                .map(city -> modelMapper.map(city, CityResponseDto.class))
                .collect(Collectors.toList());
        if (cities.isEmpty()) {
            log.warn("No city found with name {}", name);
            throw new EntityNotFoundException("The city with name " + name + " was not found");
        }
        return citiesDTOs;
    }

    public Map<String, Object> getAllCities(Integer page, Integer pageSize) {
        log.info("getting all cities");
        Pageable paging = PageRequest.of(page, pageSize);
        Page<City> pageCities = cityRepository.findAll(paging);
        List<CityResponseDto> cities = pageCities.getContent()
                .stream()
                .map(city -> modelMapper.map(city, CityResponseDto.class))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("cities", cities);
        response.put("currentPage", pageCities.getNumber());
        response.put("totalItems", pageCities.getTotalElements());
        response.put("totalPages", pageCities.getTotalPages());
        return response;
    }

    @Secured("ROLE_ALLOW_EDIT")
    public CityResponseDto updateCity(long id, CityUpdateRequestDto cityUpdateRequestDto) {
        log.info("updating city with id: {}", id);
        City updateCity = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("the city with id:" + id + " does not exist"));

        updateCity.setName(cityUpdateRequestDto.getName());
        updateCity.setPhoto(cityUpdateRequestDto.getPhoto());
        cityRepository.save(updateCity);
        log.info("city with id {} successfully updated", id);
        return modelMapper.map(updateCity, CityResponseDto.class);
    }

}
