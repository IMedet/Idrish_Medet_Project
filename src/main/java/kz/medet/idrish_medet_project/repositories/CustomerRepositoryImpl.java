package kz.medet.idrish_medet_project.repositories;

import kz.medet.idrish_medet_project.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepositoryImpl extends JpaRepository<Customer, Long> {
    Optional<Customer> findByOrdersId(Long orderId);
}
