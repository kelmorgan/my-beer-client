package com.kelmorgan.mybeerclient.client;

import com.kelmorgan.mybeerclient.config.WebClientConfig;
import com.kelmorgan.mybeerclient.model.BeerPageList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

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
    }

    @Test
    void listBeers() {
        Mono<BeerPageList> beerPageListMono = beerClient.listBeers(null,null,null,
                null,null);
        BeerPageList pageList = beerPageListMono.block();

        assertThat(pageList).isNotNull();
        assertThat(pageList.getContent().size()).isGreaterThan(0);
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
    }
}