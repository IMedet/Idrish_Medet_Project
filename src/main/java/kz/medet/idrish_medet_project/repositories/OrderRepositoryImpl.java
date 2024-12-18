package kz.medet.idrish_medet_project.repositories;

import kz.medet.idrish_medet_project.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepositoryImpl extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomerId(Long customerId);

    Optional<Order> findByProductsId(Long productId);
}
