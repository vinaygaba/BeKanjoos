package bekanjoos.vinaygaba.com.bekanjoos;

/**
 * Created by vinaygaba on 12/5/15.
 */
public class Product {
    String productName;
    String price;
    String website;
    String imageUrl;
    String productId;

    public Product(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;

    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Product(String productName, String price, String website, String imageUrl) {
        this.productName = productName;
        this.price = price;
        this.website = website;
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
