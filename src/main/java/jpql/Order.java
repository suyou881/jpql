package jpql;

import javax.persistence.*;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id @GeneratedValue
    private Long id;
    private int orderAmount;
@Embedded
    private Address address;

@ManyToOne
@JoinColumn(name = "PRODUCT_ID")
private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
