package com.iiht.estock.stock.eventsourcing.events;

import com.iiht.estock.stock.model.Stock;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DeleteStockEvent {

    private UUID uuid;
    private Stock stock;
}