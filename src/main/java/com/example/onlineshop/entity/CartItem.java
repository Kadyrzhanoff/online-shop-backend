package com.example.onlineshop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "cart_items",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"cart_id", "stock_id"}
        )
)
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name = "cart_id",   nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name = "stock_id",  nullable = false)
    private VariantStock stock;

    @Column(nullable = false)
    private Integer quantity;
}
