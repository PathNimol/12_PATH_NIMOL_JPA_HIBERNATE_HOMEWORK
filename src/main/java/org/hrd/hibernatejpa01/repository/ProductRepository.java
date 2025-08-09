package org.hrd.hibernatejpa01.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hrd.hibernatejpa01.model.dto.request.ProductRequest;
import org.hrd.hibernatejpa01.model.entity.Product;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class ProductRepository {

    @PersistenceContext
    private EntityManager em;

    public void createProducts(Product product) {
        em.persist(product);
    }

    public Product updateProduct(Long id, ProductRequest request) {
        Product product = em.find(Product.class, id);
        product.setName(request.getName().trim());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        return product;
    }

    public void deleteProduct(Long id) {
        Product product = em.find(Product.class, id);
        em.remove(product);
    }

    public Product getProductById(Long id) {
        return em.find(Product.class, id);
    }


    public int countProducts() {
        Long count = em.createQuery("select count(p) from Product p", Long.class)
                .getSingleResult();
        return count.intValue();
    }

    public List<Product> getAllProducts(Integer page, Integer size) {
        int offset = (page - 1) * size;
        return em.createQuery("select p from Product p", Product.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    public List<Product> findProductBySearchName(String name) {
        String searchName = "%" + name.toLowerCase() + "%";
        return em.createQuery("select p from Product p where LOWER(p.name) like :name", Product.class)
                .setParameter("name", searchName)
                .getResultList();
    }

    public List<Product> getLowStockProducts(Integer quantity) {
        return em.createQuery("select p from Product p where p.quantity < :quantity", Product.class)
                .setParameter("quantity", quantity)
                .getResultList();
    }

}
