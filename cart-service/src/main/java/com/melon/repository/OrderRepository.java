package com.melon.repository;

import com.melon.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<List<Order>> findByUserId(String userId);

    @Modifying
    @Query("update Order o set o.orderStatus = :orderstatus where o.id = :orderid")
    void updateOrderStatus(@Param("orderid") Long orderId, @Param("orderstatus") String orderStatus);

    @Modifying
    @Query("update Order o set o.paymentMethod = :paymentmethod where o.id =:orderid")
    void updateOrderPaymentMethod(@Param("orderid") Long orderId, @Param("paymentmethod") String paymentMethod);
}
