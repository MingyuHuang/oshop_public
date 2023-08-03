package com.melon.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shipping")
@JsonIdentityInfo(scope = Shipping.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String name;

    @OneToOne(mappedBy = "shipping")
    private Order order;
}
