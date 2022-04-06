package com.kelmorgan.mybeerclient.client;

import com.kelmorgan.mybeerclient.model.BeerDto;
import com.kelmorgan.mybeerclient.model.BeerPageList;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BeerClient {

    Mono<BeerDto> getBeerById(UUID id, Boolean showInventoryOnHand);

    Mono<BeerPageList> listBeers(Integer pageNumber, Integer pageSize, String beerName,String beerStyle, Boolean showInventoryOnHand);

    Mono<ResponseEntity<Void>> createBeer (BeerDto beerDto);

    Mono<ResponseEntity> updateBeer(BeerDto beerDto);

    Mono<ResponseEntity> deleteById(UUID id);

    Mono<BeerDto> getBeerByUPC(String upc);


}
