package dtu.thebestprice.entities.interceptor;

import dtu.thebestprice.entities.Price;
import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.repositories.PriceRepository;
import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductIndexingInterceptor implements EntityIndexingInterceptor<Product> {

    @Autowired
    PriceRepository priceRepository;

    @Override
    public IndexingOverride onAdd(Product product) {

        if (!checkState(product)) {
            return IndexingOverride.SKIP;
        }
        return IndexingOverride.APPLY_DEFAULT;
    }

    @Override
    public IndexingOverride onUpdate(Product product) {
        if (!checkState(product)) {
            return IndexingOverride.REMOVE;
        }
        return IndexingOverride.UPDATE;
    }

    @Override
    public IndexingOverride onDelete(Product product) {
        return IndexingOverride.APPLY_DEFAULT;
    }

    @Override
    public IndexingOverride onCollectionUpdate(Product product) {
        if (!checkState(product)) {
            return IndexingOverride.REMOVE;
        }
        return onUpdate(product);
    }

    private boolean checkState(Product product) {
        if (!product.isApprove() || !product.isEnable() || product.isDeleteFlg())
            return false;

        if (product.getProductRetailers() == null || product.getProductRetailers().size() == 0)
            return false;

        int count = 0;

//        for (ProductRetailer productRetailer : product.getProductRetailers()) {
//            if (productRetailer != null) {
//                Price price = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer);
//                if (price == null) {
//                    return false;
//                } else {
//                    if (price.getPrice() > 0)
//                        count++;
//                }
//            }
//        }

        int productRetialerSize = product.getProductRetailers().size();

        for (ProductRetailer productRetailer : product.getProductRetailers()) {
            if (productRetailer.isDeleteFlg() || !productRetailer.isApprove() || !productRetailer.isEnable())
                count++;
        }

        if (count == productRetialerSize)
            return false;


        return true;
    }
}
