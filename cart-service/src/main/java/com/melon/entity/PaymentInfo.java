package com.melon.entity;

import lombok.*;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "t_payment_info")
public class PaymentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_id")
    private String orderId;
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "trade_type")
    private String tradeType;
    @Column(name = "trade_status")
    private String tradeStatus;
    @Column(name = "payer_total")
    private Double payerTotal;
    @Column(name = "content")
    private String content;
    @Column(name = "creation_time")
    private Date creationTime;
    @Column(name = "modification_time")
    private Date modificationTime;
}
