package com.kelmorgan.mybeerclient.client;

import com.kelmorgan.mybeerclient.config.WebClientConfig;
import com.kelmorgan.mybeerclient.model.BeerDto;
import com.kelmorgan.mybeerclient.model.BeerPageList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BeerClientImplTest {

    BeerClientImpl beerClient;

    @BeforeEach
    void setUp() {

        beerClient = new BeerClientImpl(new WebClientConfig().webClient());
    }

    @Test
    void getBeerById() {
        Mono<BeerPageList> beerPageListMono = beerClient.listBeers(null, null, null,
                null, null);
        BeerPageList pageList = beerPageListMono.block();

        UUID id = pageList.getContent().get(0).getId();
        Mono<BeerDto> beerDtoMono = beerClient.getBeerById(id, false);
        BeerDto beerDto = beerDtoMono.block();

        assertThat(beerDto.getId()).isEqualTo(id);
        assertThat(beerDto.getQuantityOnHand()).isNull();
    }

    @Test
    void listBeers() {
        Mono<BeerPageList> beerPageListMono = beerClient.listBeers(null, null, null,
                null, null);
        BeerPageList pageList = beerPageListMono.block();

        assertThat(pageList).isNotNull();
        assertThat(pageList.getContent().size()).isGreaterThan(0);
        System.out.println(pageList.toList());
    }

    @Test
    void listBeersPageSize10() {
        Mono<BeerPageList> beerPageListMono = beerClient.listBeers(1, 10, null,
                null, null);
        BeerPageList pageList = beerPageListMono.block();

        assertThat(pageList).isNotNull();
        assertThat(pageList.getContent().size()).isEqualTo(10);
    }

    @Test
    void listBeersNoRecord() {
        Mono<BeerPageList> beerPageListMono = beerClient.listBeers(10, 20, null,
                null, null);
        BeerPageList pageList = beerPageListMono.block();

        assertThat(pageList).isNotNull();
        assertThat(pageList.getContent().size()).isEqualTo(0);
    }

    @Test
    void createBeer() {
        BeerDto beerDto = BeerDto.builder()
                .beerName("Star")
                .beerStyle("IPA")
                .upc("1212412414141")
                .price(new BigDecimal("10.99"))
                .build();

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.createBeer(beerDto);
        ResponseEntity responseEntity = responseEntityMono.block();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void updateBeer() {
        Mono<BeerPageList> beerPageListMono = beerClient.listBeers(null, null, null,
                null, null);
        BeerPageList pageList = beerPageListMono.block();
        BeerDto beerDto = pageList.getContent().get(0);


        BeerDto updateBeer = BeerDto.builder()
                .beerName("Really Nice Beer")
                .beerStyle(beerDto.getBeerStyle())
                .price(beerDto.getPrice())
                .upc(beerDto.getUpc())
                .build();

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.updateBeer(beerDto.getId(), updateBeer);

        ResponseEntity<Void> responseEntity = responseEntityMono.block();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteById() {
        Mono<BeerPageList> beerPageListMono = beerClient.listBeers(null, null, null,
                null, null);
        BeerPageList pageList = beerPageListMono.block();

        UUID id = pageList.getContent().get(0).getId();

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteById(id);

        ResponseEntity<Void> responseEntity = responseEntityMono.block();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteByIdNotFound() {
        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteById(UUID.randomUUID());

        ResponseEntity<Void> responseEntity = responseEntityMono.onErrorResume(throwable -> {
            if (throwable instanceof WebClientResponseException){
                WebClientResponseException webClientException = (WebClientResponseException) throwable;
                return Mono.just(ResponseEntity.status(webClientException.getStatusCode()).build());
            }
            else throw new RuntimeException(throwable);
        }).block();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getBeerByUPC() {
        Mono<BeerPageList> beerPageListMono = beerClient.listBeers(null, null, null,
                null, null);
        BeerPageList pageList = beerPageListMono.block();

        String upc = pageList.getContent().get(0).getUpc();

        Mono<BeerDto> beerDtoMono = beerClient.getBeerByUPC(upc);
        BeerDto beerDto = beerDtoMono.block();

        assertThat(beerDto.getUpc()).isEqualTo(upc);
    }
     @Test
    void functionalTestGetBeerById() throws InterruptedException {

         AtomicReference<String> beerName = new AtomicReference<>();
         CountDownLatch countDownLatch = new CountDownLatch(1);
         beerClient.listBeers(null, null, null,
                 null, null)
                 .map(beerPageList -> beerPageList.getContent().get(0).getId())
                 .map(beerId -> beerClient.getBeerById(beerId,false))
                 .flatMap(beerDtoMono -> beerDtoMono)
                 .subscribe(beerDto -> {
                     System.out.println(beerDto.getBeerName());
                     beerName.set(beerDto.getBeerName());
                     countDownLatch.countDown();
                 });
         countDownLatch.await();
         assertThat(beerName.get()).isEqualTo("Mango Bobs");
     }



}