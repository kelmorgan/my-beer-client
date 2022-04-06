package com.kelmorgan.mybeerclient.client;

import com.kelmorgan.mybeerclient.config.WebClientConfig;
import com.kelmorgan.mybeerclient.model.BeerDto;
import com.kelmorgan.mybeerclient.model.BeerPageList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.UUID;

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
        Mono<BeerPageList> beerPageListMono = beerClient.listBeers(null,null,null,
                null,null);
        BeerPageList pageList = beerPageListMono.block();

        UUID id = pageList.getContent().get(0).getId();
        Mono<BeerDto> beerDtoMono = beerClient.getBeerById(id,false);
        BeerDto beerDto = beerDtoMono.block();

        assertThat(beerDto.getId()).isEqualTo(id);
        assertThat(beerDto.getQuantityOnHand()).isNull();
    }

    @Test
    void listBeers() {
        Mono<BeerPageList> beerPageListMono = beerClient.listBeers(null,null,null,
                null,null);
        BeerPageList pageList = beerPageListMono.block();

        assertThat(pageList).isNotNull();
        assertThat(pageList.getContent().size()).isGreaterThan(0);
        System.out.println(pageList.toList());
    }

    @Test
    void listBeersPageSize10() {
        Mono<BeerPageList> beerPageListMono = beerClient.listBeers(1,10,null,
                null,null);
        BeerPageList pageList = beerPageListMono.block();

        assertThat(pageList).isNotNull();
        assertThat(pageList.getContent().size()).isEqualTo(10);
    }

    @Test
    void listBeersNoRecord() {
        Mono<BeerPageList> beerPageListMono = beerClient.listBeers(10,20,null,
                null,null);
        BeerPageList pageList = beerPageListMono.block();

        assertThat(pageList).isNotNull();
        assertThat(pageList.getContent().size()).isEqualTo(0);
    }

    @Test
    void createBeer() {

    }

    @Test
    void updateBeer() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void getBeerByUPC() {
        Mono<BeerPageList> beerPageListMono = beerClient.listBeers(null,null,null,
                null,null);
        BeerPageList pageList = beerPageListMono.block();

        String upc = pageList.getContent().get(0).getUpc();

        Mono<BeerDto> beerDtoMono = beerClient.getBeerByUPC(upc);
        BeerDto beerDto = beerDtoMono.block();

        assertThat(beerDto.getUpc()).isEqualTo(upc);
    }
}