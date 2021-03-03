/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naturemorning.parallax.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cashiers")
public class Cashier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)

    private long id;

    private String cashierName;

    private String cashierAge;

    private String amount;

    private String paymentType;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getCashierAge() {
        return cashierAge;
    }

    public void setCashierAge(String cashierAge) {
        this.cashierAge = cashierAge;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String type) {
        this.paymentType = type;
    }

    @Override
    public String toString() {
        return "Cashier [id=" + id + ", cashierName=" + cashierName + ",cashierAge=" + cashierAge + ", deliveryAmount=" + amount
                + ",paymentType=" + paymentType + "]";

    }

}
