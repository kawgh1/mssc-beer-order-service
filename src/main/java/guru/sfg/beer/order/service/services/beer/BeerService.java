package guru.sfg.beer.order.service.services.beer;

/**
 * created by kw on 12/28/2020 @ 10:49 PM
 */
import java.util.Optional;
import java.util.UUID;

import guru.sfg.beer.order.service.web.model.BeerDto;

public interface BeerService {

    Optional<BeerDto> getBeerById(UUID uuid);

    Optional<BeerDto> getBeerByUpc(String upc);
}