package kz.medet.idrish_medet_project.repositories;

import kz.medet.idrish_medet_project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryImpl extends JpaRepository<Product, Long> {
}
