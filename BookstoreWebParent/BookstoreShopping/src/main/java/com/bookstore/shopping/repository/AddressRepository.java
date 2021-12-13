package com.bookstore.shopping.repository;

import com.bookstore.common.entity.Address;
import com.bookstore.common.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface AddressRepository extends CrudRepository<Address, Integer> {

    public List<Address> findByCustomer(Customer customer);

    @Query("SELECT a FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
    public Address findByIdAndCustomer(Integer addressId, Integer customerId);

    @Query("DELETE FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
    @Modifying
    public void deleteByIdAndCustomer(Integer addressId, Integer customerId);

    @Query("UPDATE Address a SET a.defaultForShipping = true WHERE a.id = ?1")
    @Modifying
    public void setDefaultAddress(Integer id);

    @Query("UPDATE Address a SET a.defaultForShipping = false " + "WHERE a.id != ?1 AND a.customer.id = ?2")
    @Modifying
    public void setNonDefaultForOther(Integer defaultAddressId, Integer customerId);

}
