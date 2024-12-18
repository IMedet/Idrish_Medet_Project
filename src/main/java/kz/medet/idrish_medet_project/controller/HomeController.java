package kz.medet.idrish_medet_project.controller;

import kz.medet.idrish_medet_project.model.Customer;
import kz.medet.idrish_medet_project.model.Order;
import kz.medet.idrish_medet_project.model.Product;
import kz.medet.idrish_medet_project.services.AllServices;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service")
public class HomeController {

    private final AllServices services;

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return new ResponseEntity<>(services.showALlCustomers(), HttpStatus.OK);
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<String> createCustomer(@RequestParam String firstName,
                                                          @RequestParam String lastName) {
        services.addCustomer(firstName, lastName);

        return new ResponseEntity<>("Customer created", HttpStatus.CREATED);
    }

    @PostMapping("/addOrderToCustomer/{customerId}")
    public ResponseEntity<String> addOrderToCustomer(@PathVariable Long customerId) {
        services.addOrderToCustomer(customerId);

        return new ResponseEntity<>("Order created for Customer" + customerId, HttpStatus.CREATED);
    }

    @GetMapping("/showOrdersOfCustomer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersOfCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(services.getOrdersOfCustomer(customerId));
    }

    @PostMapping("/addProductToOrder/{orderId}")
    public ResponseEntity<String> addProductToOrder(@PathVariable Long orderId,
                                                             @RequestParam String productName,
                                                             @RequestParam double productPrice) {
        services.addProductToOrder(orderId, productName, productPrice);

        return new ResponseEntity<>("Product added to Order " + orderId, HttpStatus.CREATED);
    }

    @GetMapping("/showProductsOfOrder/{orderId}")
    public ResponseEntity<List<Product>> getProductsOfOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(services.getProductsOfOrder(orderId));
    }

    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long productId) {
        boolean isDeleted = services.deleteProduct(productId);

        if (isDeleted) {
            return new ResponseEntity<>("product: " + productId + " deleted", HttpStatus.OK);
        }

        return new ResponseEntity<>("Could not find product (", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId,
                                                @RequestParam String productName,
                                                @RequestParam double productPrice){
        boolean isUpdated = services.updateProduct(productId, productName, productPrice);
        System.out.println(isUpdated);
        if (isUpdated){
            return new ResponseEntity<>("Product updated!", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Product could not be updated (", HttpStatus.NOT_FOUND);
        }
    }
}
