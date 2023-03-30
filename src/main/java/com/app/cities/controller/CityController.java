package com.app.cities.controller;

import com.app.cities.DTO.CityResponseDto;
import com.app.cities.DTO.CityUpdateRequestDto;
import com.app.cities.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(CityController.URL)
@Tag(name = "City API")
@Validated
public class CityController {

    public static final String URL = "/cities";
    private final CityService cityService;

    @Operation(summary = "Find city by name")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200",
            content = {@Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CityResponseDto.class)))})})
    @GetMapping("/{name}")
    public List<CityResponseDto> getCity(@PathVariable @NotBlank String name) {
        return cityService.findByName(name);
    }

    @Operation(summary = "Get all cities")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200",
            content = {@Content(mediaType = "application/json")})})
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCities(@RequestParam @Min(0) int pageNumber,
                                                            @RequestParam @Min(1) @Max(50)
                                                            int pageSize) {
        return cityService.getAllCities(pageNumber, pageSize);
    }

    @Operation(summary = "Update city")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200",
            content = {@Content(mediaType = "application/json")})})
    @PutMapping("/{id}")
    public ResponseEntity<CityResponseDto> updateCity(@PathVariable @Positive Long id, @Parameter(description = "City DTO to update", required = true)  @Valid @RequestBody CityUpdateRequestDto cityUpdateRequestDto) {
        return cityService.updateCity(id, cityUpdateRequestDto);
    }


}
