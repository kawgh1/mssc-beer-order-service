package com.kwgdev.beer.order.service.services;

import com.kwgdev.beer.order.service.domain.BeerOrder;
import com.kwgdev.beer.order.service.domain.BeerOrderEventEnum;
import com.kwgdev.beer.order.service.domain.BeerOrderStatusEnum;
import com.kwgdev.beer.order.service.repositories.BeerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * created by kw on 1/10/2021 @ 9:15 AM
 */
@RequiredArgsConstructor
@Service
public class BeerOrderManagerImpl implements BeerOrderManager {

    public static final String ORDER_ID_HEADER = "ORDER_ID_HEADER";

    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachineFactory;
    private final BeerOrderRepository beerOrderRepository;

    // 1. new beer order
    @Transactional
    @Override
    public BeerOrder newBeerOrder(BeerOrder beerOrder) {
        // defensive coding - if someone sends in a beer order, by setting id to null, we're guaranteeing that we control
        // the beerOrder Id
        beerOrder.setId(null);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);

        // once persisted to DB by Hibernate, Hibernate will set the ID value
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        // then we tell the State Machine to validate the order
        sendBeerOrderEvent(savedBeerOrder, BeerOrderEventEnum.VALIDATE_ORDER);

        return savedBeerOrder;
    }

    @Override
    public void processValidationResult(UUID beerOrderId, Boolean isValid) {

        // what's going to happen in terms of Hibernate, when we do sendBeerOrderEvent,
        // the interceptors are going to save it so beerOrder becomes a stale object and will have an
        // older version number on there so Hibernate will not be happy unless we go get the current version

        BeerOrder beerOrder = beerOrderRepository.getOne(beerOrderId);

        if(isValid) {
            sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.VALIDATION_PASSED);

            // get a fresh object
            BeerOrder validatedOrder = beerOrderRepository.findOneById(beerOrderId);

            // So we're going to send a ALLOCATE_ORDER event telling the State Machine that we want to allocate that order
            sendBeerOrderEvent(validatedOrder, BeerOrderEventEnum.ALLOCATE_ORDER);
        } else {
            sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.VALIDATION_FAILED);
        }
    }

    // State Machine
    // - here we don't need to rehydrate it, but because it is a brand new beer order
    // - we want to send the event
    private void sendBeerOrderEvent(BeerOrder beerOrder, BeerOrderEventEnum eventEnum) {

        // build new State Machine method below with the Order Status from the Database
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> sm = build(beerOrder);


        // Then we build a message using the "VALIDATE_ORDER" EventEnum and send that message to the StateMachine
        Message msg = MessageBuilder.withPayload(eventEnum)
                .setHeader(ORDER_ID_HEADER, beerOrder.getId().toString())
                .build();

        sm.sendEvent(msg);

    }

    private StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> build(BeerOrder beerOrder) {

        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> sm = stateMachineFactory.getStateMachine(beerOrder.getId());

        sm.stop();

        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.resetStateMachine(new DefaultStateMachineContext<>(beerOrder.getOrderStatus(), null, null, null));
                });

        sm.start();

        return sm;
    }
}
