package com.melon.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "items")
@JsonIdentityInfo(scope = OrderItem.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;
    @Column(name = "imageUrl")
    private String imageUrl;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "totalPrice")
    private Double totalPrice;
    @ManyToOne()
//    @JoinTable(name = "item_order", joinColumns = {@JoinColumn(name = "item_id")}, inverseJoinColumns = {@JoinColumn(name = "order_id")})
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
}
