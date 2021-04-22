package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.ProductConverter;
import dtu.thebestprice.entities.Product;
import dtu.thebestprice.payload.response.ProductItem;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.services.IndexingService;
import dtu.thebestprice.services.SearchSuggestionService;
import dtu.thebestprice.specifications.ProductSpecification;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchSuggestionServiceImpl implements SearchSuggestionService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductConverter productConverter;

    @Autowired
    EntityManager entityManager;

    @Autowired
    IndexingService indexingService;

    @Override
    public Page<ProductItem> findByKeyword(String keyword, Pageable pageable) {
        Specification condition = Specification.where(
                ProductSpecification.titleContaining(keyword)
        );
        Page<Product> productPage = productRepository.findAll(condition, pageable);
        return productPage.map(product -> productConverter.toProductItem(product));
    }

    @Override
    @Transactional
    public List<ProductItem> findByKeywordV2(String keyword) {
        FullTextEntityManager fullTextEntityManager =
                Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Product.class)
                .get();

        Query query = queryBuilder.keyword()
                .onField("title")
                .matching(keyword)
                .createQuery();

        FullTextQuery fullTextQuery = fullTextEntityManager
                .createFullTextQuery(query, Product.class)
                .setMaxResults(5);

        List<Product> productList = fullTextQuery.getResultList();
        return productList.stream().map(product -> productConverter.toProductItem(product)).collect(Collectors.toList());
    }
}
