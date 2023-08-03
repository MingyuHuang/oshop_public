//package com.melon.entity;
//
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.util.Date;
//import java.util.List;
//
//@Entity
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "shopping_cart")
//@JsonIdentityInfo(scope = ShoppingCart.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//public class ShoppingCart {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//    @Column(name = "created_date")
//    private Date createdDate;
//    @Column(name = "items")
//    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
//    private List<Item> items;
//}
