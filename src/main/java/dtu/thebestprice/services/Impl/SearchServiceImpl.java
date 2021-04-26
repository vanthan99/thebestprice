package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.ProductConverter;
import dtu.thebestprice.entities.Product;
import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.response.LongProductResponse;
import dtu.thebestprice.payload.response.PageCustom;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.services.SearchService;
import dtu.thebestprice.specifications.ProductSpecification;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductConverter productConverter;

    @Autowired
    EntityManager entityManager;

    @Override
    @Transactional
    public PageCustom filter(FilterRequest filterRequest, Pageable pageable) {
        PageCustom page = new PageCustom();

        FullTextEntityManager fullTextEntityManager =
                Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Product.class)
                .get();

        Query query;

        try {
            query = queryBuilder.keyword()
                    .onFields("title","category.title","shortDescription","longDescription")
                    .matching(filterRequest.getKeyword())
                    .createQuery();
        } catch (Exception e) {
            page.setContent(new ArrayList<>());
            return page;
        }



        FullTextQuery fullTextQuery = fullTextEntityManager
                .createFullTextQuery(query, Product.class);


        List<Product> productList = fullTextQuery
                .setMaxResults(pageable.getPageSize())
                .setFirstResult(((pageable.getPageNumber() - 1) * pageable.getPageSize()) + 1)
                .getResultList();

        page.setContent(productList.stream().map(product -> productConverter.toLongProductResponse(product)).collect(Collectors.toList()));
        page.setTotalElements(fullTextQuery.getResultSize());
        page.setSize(pageable.getPageSize());
        page.setCurrentPage(pageable.getPageNumber());
        page.setTotalPages((int) Math.ceil((double) fullTextQuery.getResultSize() / page.getSize()));

        return page;
    }
}
