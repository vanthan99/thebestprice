package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.ProductConverter;
import dtu.thebestprice.entities.Product;
import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.response.PageCustom;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.services.SearchService;
import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.TermTermination;
import org.springframework.beans.factory.annotation.Autowired;
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

        BooleanJunction<?> boolForWholeQuery = queryBuilder.bool();
        TermTermination termTermination;

        try {
            if (filterRequest.getKeyword() != "") {

                termTermination = queryBuilder.keyword()
                        .fuzzy().withEditDistanceUpTo(1)
                    .onFields("title","category.title","shortDescription","longDescription")
                    .matching(filterRequest.getKeyword());
                boolForWholeQuery.must(termTermination.createQuery());
            } else {
                boolForWholeQuery.must(queryBuilder.all().createQuery());
            }

        } catch (Exception e) {
            page.setContent(new ArrayList<>());
            return page;
        }


        if (filterRequest.getCatId() != null) {
            BooleanJunction<?> boolForCategoryIds = queryBuilder.bool();
            boolForCategoryIds
                    .should(queryBuilder.keyword()
                            .onField("category.id")
                            .matching(filterRequest.getCatId()).createQuery());
            boolForWholeQuery.must(boolForCategoryIds.createQuery());
        }

        if (filterRequest.getRetailerIds() != null && filterRequest.getRetailerIds().size() != 0) {
            BooleanJunction<?> boolForRetailerIds = queryBuilder.bool();
            for (String id : filterRequest.getRetailerIds()) {
                boolForRetailerIds
                        .should(queryBuilder.keyword()
                                .onField("productRetailers.retailer.id")
                                .matching(id).createQuery());

            }
            boolForWholeQuery.must(boolForRetailerIds.createQuery());
        }


        FullTextQuery fullTextQuery = fullTextEntityManager
                .createFullTextQuery(boolForWholeQuery.createQuery(), Product.class);

        int totalElements = fullTextQuery.getResultList().size();

        fullTextQuery.setMaxResults(pageable.getPageSize());
        fullTextQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

        List<Product> productList = fullTextQuery
                .getResultList();


        page.setContent(productList.stream().map(product -> productConverter.toLongProductResponse(product)).collect(Collectors.toList()));
        page.setTotalElements(totalElements);
        page.setSize(pageable.getPageSize());
        page.setCurrentPage(pageable.getPageNumber());
        page.setTotalPages((int) Math.ceil((double) totalElements / page.getSize()));
        page.setFirst(page.getCurrentPage() == 0);
        page.setLast(page.getTotalPages() - 1 == page.getCurrentPage());

        return page;
    }
}
