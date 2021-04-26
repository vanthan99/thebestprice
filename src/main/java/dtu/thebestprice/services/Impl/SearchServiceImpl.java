package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.ProductConverter;
import dtu.thebestprice.entities.Product;
import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.response.PageCustom;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.services.SearchService;
import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
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
        Session session = entityManager.unwrap(Session.class);

        FullTextSession fullTextSession = org.hibernate.search.Search.getFullTextSession(session);

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


        Criteria criteria = fullTextSession.createCriteria(Product.class)
                .createAlias("productRetailers.retailer", "retailer");


        if (filterRequest.getCatId() != null) {
            criteria.add(
                    Restrictions.in("category.id", Arrays.asList(Long.parseLong(filterRequest.getCatId())))
            );
        }

        if (filterRequest.getRetailerIds() != null && filterRequest.getRetailerIds().size() != 0) {
            List<Long> retailerIds = filterRequest.getRetailerIds().stream().map((s) -> Long.parseLong(s)).collect(Collectors.toList());
            criteria.add(
                    Restrictions.in("retailer.id", retailerIds)
            );
        }

        FullTextQuery fullTextQuery = fullTextEntityManager
                .createFullTextQuery(query, Product.class).setCriteriaQuery(criteria);




        List<Product> productList = fullTextQuery
//                .setMaxResults(pageable.getPageSize())
//                .setFirstResult(((pageable.getPageNumber() - 1) * pageable.getPageSize()) + 1)
                .getResultList();

        int size = fullTextQuery.getResultList().size();

        page.setContent(productList
                .subList(((pageable.getPageNumber() - 1) * pageable.getPageSize()), pageable.getPageNumber()*size - 1)
                .stream()
                .map(product -> productConverter.toLongProductResponse(product))
                .collect(Collectors.toList()));
        page.setTotalElements(size);
        page.setSize(pageable.getPageSize());
        page.setCurrentPage(pageable.getPageNumber());
        page.setTotalPages((int) Math.ceil((double) size / page.getSize()));

        return page;
    }
}
