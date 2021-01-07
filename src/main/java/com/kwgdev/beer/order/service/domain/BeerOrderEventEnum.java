package com.kwgdev.beer.order.service.domain;

/**
 * created by kw on 1/7/2021 @ 11:21 AM
 */
public enum BeerOrderEventEnum {
    VALIDATE_ORDER, VALIDATION_PASSED, VALIDATION_FAILED,
    ALLOCATION_SUCCESS, ALLOCATION_NO_INVENTORY, ALLOCATION_FAILED,
    BEER_ORDER_PICKED_UP
}