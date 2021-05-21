package dtu.thebestprice.entities.interceptor;

import dtu.thebestprice.entities.Product;
import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;

public class ProductIndexingInterceptor implements EntityIndexingInterceptor<Product> {

    @Override
    public IndexingOverride onAdd(Product product) {
        if (!product.isApprove() && !product.isEnable()) {
            return IndexingOverride.SKIP;
        }
        return IndexingOverride.APPLY_DEFAULT;
    }

    @Override
    public IndexingOverride onUpdate(Product product) {
        if (!product.isApprove() || !product.isEnable() || product.isDeleteFlg()) {
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
        if (!product.isApprove() && !product.isEnable()) {
            return IndexingOverride.SKIP;
        }
        return onUpdate(product);
    }
}
