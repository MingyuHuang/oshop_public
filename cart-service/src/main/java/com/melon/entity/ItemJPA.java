//package com.melon.entity;
//
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//import lombok.*;
//import org.hibernate.annotations.Cascade;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//
//import javax.persistence.*;
//
//@Table(name = "items")
//@Getter
//@Setter
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonIdentityInfo(scope = Item.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//public class Item {
//
//    @Id
//    private Long id;
//    @Column(name = "quantity")
//    private Integer quantity;
//    @ManyToOne()
//    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
//    @JoinColumn(name = "cart")
//    private ShoppingCart cart;
//
//    @Column(name = "price")
//    private Double price;
//    @Column(name = "title")
//    private String title;
//
//}
