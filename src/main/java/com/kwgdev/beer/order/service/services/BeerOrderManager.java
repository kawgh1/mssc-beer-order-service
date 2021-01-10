package com.kwgdev.beer.order.service.services;

import com.kwgdev.beer.order.service.domain.BeerOrder;

/**
 * created by kw on 1/10/2021 @ 9:14 AM
 */
public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);
}
