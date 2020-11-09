package com.springframework.spring5webfluxrest.controllers;

import com.springframework.spring5webfluxrest.domain.Vendor;
import com.springframework.spring5webfluxrest.repositories.VendorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class VendorControllerTest {

    WebTestClient webTestClient;

    VendorRepository vendorRepository;

    VendorController vendorController;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void list() {
        BDDMockito.given(vendorRepository.findAll()).willReturn(
                Flux.just(Vendor.builder().firstName("Bob").lastName("Fill").build())
        );

        webTestClient.get()
                .uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(1);
    }

    @Test
    void getById() {
        BDDMockito.given(vendorRepository.findById("someId")).willReturn(
                Mono.just(Vendor.builder().firstName("Bob").lastName("Fill").build())
        );

        webTestClient.get()
                .uri("/api/v1/vendors/someId")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    void create(){
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class))).willReturn(
                Flux.just(Vendor.builder().firstName("Bob").lastName("Fill").build())
        );
        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(Mono.just(Vendor.builder().firstName("Bob").lastName("Fill").build()),Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void update() {
        BDDMockito.given(vendorRepository.save(any(Vendor.class))).willReturn(
                Mono.just(Vendor.builder().firstName("Bob").lastName("Fill").build())
        );
        webTestClient.put()
                .uri("/api/v1/vendors/someId")
                .body(Mono.just(Vendor.builder().firstName("Bob").lastName("Fill").build()), Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void patch(){
        BDDMockito.given(vendorRepository.findById(anyString())).willReturn(
                Mono.just(Vendor.builder().firstName("Bob").lastName("McGregory").build())
        );
        BDDMockito.given(vendorRepository.save(any(Vendor.class))).willReturn(
                Mono.just(Vendor.builder().firstName("Bob").lastName("Fill").build())
        );
        webTestClient.patch()
                .uri("/api/v1/vendors/someId")
                .body(Mono.just(Vendor.builder().firstName("Bob").lastName("Fill").build()), Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
        Mockito.verify(vendorRepository,Mockito.times(1)).save(any());
    }

    @Test
    void patchNoChanges(){
        BDDMockito.given(vendorRepository.findById(anyString())).willReturn(
                Mono.just(Vendor.builder().firstName("Bob").lastName("Fill").build())
        );
        webTestClient.patch()
                .uri("/api/v1/vendors/someId")
                .body(Mono.just(Vendor.builder().firstName("Bob").lastName("Fill").build()), Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
        Mockito.verify(vendorRepository,Mockito.times(0)).save(any());
    }

}