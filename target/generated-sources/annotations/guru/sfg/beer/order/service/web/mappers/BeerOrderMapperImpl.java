package guru.sfg.beer.order.service.web.mappers;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrder.BeerOrderBuilder;
import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.web.model.BeerOrderDto;
import guru.sfg.beer.order.service.web.model.BeerOrderDto.BeerOrderDtoBuilder;
import guru.sfg.beer.order.service.web.model.BeerOrderLineDto;
import guru.sfg.beer.order.service.web.model.OrderStatusEnum;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-29T01:13:58-0600",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 13.0.2 (Oracle Corporation)"
)
@Component
public class BeerOrderMapperImpl implements BeerOrderMapper {

    @Autowired
    private DateMapper dateMapper;
    @Autowired
    private BeerOrderLineMapper beerOrderLineMapper;

    @Override
    public BeerOrderDto beerOrderToDto(BeerOrder beerOrder) {
        if ( beerOrder == null ) {
            return null;
        }

        BeerOrderDtoBuilder beerOrderDto = BeerOrderDto.builder();

        beerOrderDto.customerId( beerOrderCustomerId( beerOrder ) );
        beerOrderDto.id( beerOrder.getId() );
        if ( beerOrder.getVersion() != null ) {
            beerOrderDto.version( beerOrder.getVersion().intValue() );
        }
        beerOrderDto.createdDate( dateMapper.asOffsetDateTime( beerOrder.getCreatedDate() ) );
        beerOrderDto.lastModifiedDate( dateMapper.asOffsetDateTime( beerOrder.getLastModifiedDate() ) );
        beerOrderDto.beerOrderLines( beerOrderLineSetToBeerOrderLineDtoList( beerOrder.getBeerOrderLines() ) );
        beerOrderDto.orderStatus( orderStatusEnumToOrderStatusEnum( beerOrder.getOrderStatus() ) );
        beerOrderDto.orderStatusCallbackUrl( beerOrder.getOrderStatusCallbackUrl() );
        beerOrderDto.customerRef( beerOrder.getCustomerRef() );

        return beerOrderDto.build();
    }

    @Override
    public BeerOrder dtoToBeerOrder(BeerOrderDto dto) {
        if ( dto == null ) {
            return null;
        }

        BeerOrderBuilder beerOrder = BeerOrder.builder();

        beerOrder.id( dto.getId() );
        if ( dto.getVersion() != null ) {
            beerOrder.version( dto.getVersion().longValue() );
        }
        beerOrder.createdDate( dateMapper.asTimestamp( dto.getCreatedDate() ) );
        beerOrder.lastModifiedDate( dateMapper.asTimestamp( dto.getLastModifiedDate() ) );
        beerOrder.customerRef( dto.getCustomerRef() );
        beerOrder.beerOrderLines( beerOrderLineDtoListToBeerOrderLineSet( dto.getBeerOrderLines() ) );
        beerOrder.orderStatus( orderStatusEnumToOrderStatusEnum1( dto.getOrderStatus() ) );
        beerOrder.orderStatusCallbackUrl( dto.getOrderStatusCallbackUrl() );

        return beerOrder.build();
    }

    private UUID beerOrderCustomerId(BeerOrder beerOrder) {
        if ( beerOrder == null ) {
            return null;
        }
        Customer customer = beerOrder.getCustomer();
        if ( customer == null ) {
            return null;
        }
        UUID id = customer.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected List<BeerOrderLineDto> beerOrderLineSetToBeerOrderLineDtoList(Set<BeerOrderLine> set) {
        if ( set == null ) {
            return null;
        }

        List<BeerOrderLineDto> list = new ArrayList<BeerOrderLineDto>( set.size() );
        for ( BeerOrderLine beerOrderLine : set ) {
            list.add( beerOrderLineMapper.beerOrderLineToDto( beerOrderLine ) );
        }

        return list;
    }

    protected OrderStatusEnum orderStatusEnumToOrderStatusEnum(BeerOrderStatusEnum beerOrderStatusEnum) {
        if ( beerOrderStatusEnum == null ) {
            return null;
        }

        OrderStatusEnum orderStatusEnum1;

        switch (beerOrderStatusEnum) {
            case NEW: orderStatusEnum1 = OrderStatusEnum.NEW;
            break;
            case READY: orderStatusEnum1 = OrderStatusEnum.READY;
            break;
            case PICKED_UP: orderStatusEnum1 = OrderStatusEnum.PICKED_UP;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + beerOrderStatusEnum);
        }

        return orderStatusEnum1;
    }

    protected Set<BeerOrderLine> beerOrderLineDtoListToBeerOrderLineSet(List<BeerOrderLineDto> list) {
        if ( list == null ) {
            return null;
        }

        Set<BeerOrderLine> set = new HashSet<BeerOrderLine>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( BeerOrderLineDto beerOrderLineDto : list ) {
            set.add( beerOrderLineMapper.dtoToBeerOrderLine( beerOrderLineDto ) );
        }

        return set;
    }

    protected BeerOrderStatusEnum orderStatusEnumToOrderStatusEnum1(OrderStatusEnum orderStatusEnum) {
        if ( orderStatusEnum == null ) {
            return null;
        }

        BeerOrderStatusEnum beerOrderStatusEnum1;

        switch ( orderStatusEnum ) {
            case NEW: beerOrderStatusEnum1 = BeerOrderStatusEnum.NEW;
            break;
            case READY: beerOrderStatusEnum1 = BeerOrderStatusEnum.READY;
            break;
            case PICKED_UP: beerOrderStatusEnum1 = BeerOrderStatusEnum.PICKED_UP;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + orderStatusEnum );
        }

        return beerOrderStatusEnum1;
    }
}
