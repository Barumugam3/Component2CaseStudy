package com.iiht.estock.stock.exception;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ExceptionResponseMessage {

    private String message;
}
