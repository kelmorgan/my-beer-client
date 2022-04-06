package com.kelmorgan.mybeerclient.client;

import com.kelmorgan.mybeerclient.config.WebClientProperties;
import com.kelmorgan.mybeerclient.model.BeerDto;
import com.kelmorgan.mybeerclient.model.BeerPageList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    private final WebClient client;

    @Override
    public Mono<BeerDto> getBeerById(UUID id, Boolean showInventoryOnHand) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_V1_PATH + "/" + id.toString())
                        .queryParamIfPresent("showInventoryOnHand", Optional.ofNullable(showInventoryOnHand))
                        .build()).retrieve().bodyToMono(BeerDto.class);
    }

    @Override
    public Mono<BeerPageList> listBeers(Integer pageNumber, Integer pageSize, String beerName, String beerStyle, Boolean showInventoryOnHand) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_V1_PATH)
                        .queryParamIfPresent("pageNumber", Optional.ofNullable(pageNumber))
                        .queryParamIfPresent("pageSize", Optional.ofNullable(pageSize))
                        .queryParamIfPresent("beerName", Optional.ofNullable(beerName))
                        .queryParamIfPresent("beerStyle", Optional.ofNullable(beerStyle))
                        .queryParamIfPresent("showInventoryOnHand", Optional.ofNullable(showInventoryOnHand))
                        .build())
                .retrieve()
                .bodyToMono(BeerPageList.class);
    }

    @Override
    public Mono<ResponseEntity> createBeer(BeerDto beerDto) {
        return null;
    }

    @Override
    public Mono<ResponseEntity> updateBeer(BeerDto beerDto) {
        return null;
    }

    @Override
    public Mono<ResponseEntity> deleteById(UUID id) {
        return null;
    }

    @Override
    public Mono<BeerDto> getBeerByUPC(String upc) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_BY_UPC + "/" + upc)
                        .build()).retrieve().bodyToMono(BeerDto.class);
    }
}
