package kz.medet.idrish_medet_project.services;

import kz.medet.idrish_medet_project.model.Customer;
import kz.medet.idrish_medet_project.model.Order;
import kz.medet.idrish_medet_project.model.Product;
import kz.medet.idrish_medet_project.repositories.CustomerRepositoryImpl;
import kz.medet.idrish_medet_project.repositories.OrderRepositoryImpl;
import kz.medet.idrish_medet_project.repositories.ProductRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AllServices {
    public static final Logger LOG = LoggerFactory.getLogger(AllServices.class);
    private final OrderRepositoryImpl orderRepository;
    private final CustomerRepositoryImpl customerRepository;
    private final ProductRepositoryImpl productRepository;

    public AllServices(OrderRepositoryImpl orderRepository, CustomerRepositoryImpl customerRepository, ProductRepositoryImpl productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public void addCustomer(String firstName, String lastName){
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastname(lastName);
        customerRepository.save(customer);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addOrderToCustomer(Long customerId){
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();

            Order order = new Order();
            order.setTimeCreated(new Timestamp(System.currentTimeMillis()));
            order.setCustomer(customer);
            orderRepository.save(order);

            customer.getOrders().add(order);
            customerRepository.save(customer);
        }else{
            LOG.info("Could not find Customer, try with valid one");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addProductToOrder(Long orderId, String product_name, double product_price) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (product_name == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            Product product = new Product();
            product.setName(product_name);
            product.setPrice(product_price);

            productRepository.save(product);

            order.getProducts().add(product);
            orderRepository.save(order);
        } else {
            try {
                throw new IllegalArgumentException();
            } finally {
                LOG.info("Could not find Order, try with valid orderId )");
            }
        }
    }

    public List<Customer> showALlCustomers() {
        return customerRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Order> getOrdersOfCustomer(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent()) {
            return orderRepository.findAllByCustomerId(customerId);
        }

        return new ArrayList<>();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Product> getProductsOfOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            return order.getProducts();
        }

        return new ArrayList<>();
    }

    @Transactional
    public boolean deleteProduct(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Optional<Order> optionalOrder = orderRepository.findByProductsId(productId);

        if (optionalProduct.isPresent() && optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            Product product = optionalProduct.get();

            order.getProducts().remove(product);
            productRepository.deleteById(productId);

            orderRepository.save(order);
            return true;
        }
        return false;
    }


}
