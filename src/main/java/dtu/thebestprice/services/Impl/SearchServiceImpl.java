package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.ProductConverter;
import dtu.thebestprice.entities.Category;
import dtu.thebestprice.entities.Product;
import dtu.thebestprice.payload.request.FilterRequest;
import dtu.thebestprice.payload.response.PageCustom;
import dtu.thebestprice.repositories.CategoryRepository;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.services.SearchService;
import lombok.SneakyThrows;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.TermTermination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.hibernate5.HibernateTemplate;
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

    @Autowired
    CategoryRepository categoryRepository;

    @SneakyThrows
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
            if (filterRequest.getKeyword() != null && filterRequest.getKeyword() != "") {

                termTermination = queryBuilder.keyword()
                        .fuzzy().withEditDistanceUpTo(1)
                        .onFields("title", "category.title", "longDescription")
                        .matching(filterRequest.getKeyword());
                boolForWholeQuery.must(termTermination.createQuery());
            } else {
                boolForWholeQuery.must(queryBuilder.all().createQuery());
            }

        } catch (Exception e) {
            page.setContent(new ArrayList<>());
            return page;
        }

//        BooleanJunction<?> boolForEnable = queryBuilder.bool();
//        boolForEnable.must(queryBuilder.keyword().onField("enable").matching(1).createQuery());
//        boolForWholeQuery.must(boolForEnable.createQuery());
//
//        BooleanJunction<?> boolForApprove = queryBuilder.bool();
//        boolForApprove.must(queryBuilder.keyword().onField("approve").matching(1).createQuery());
//        boolForWholeQuery.must(boolForApprove.createQuery());

//        BooleanJunction<?> boolForDelete = queryBuilder.bool();
//        boolForDelete.must(queryBuilder.keyword().onField("deleteFlg").matching(0).createQuery());
//        boolForWholeQuery.must(boolForDelete.createQuery());

        if (filterRequest.getCatId() != null) {
            BooleanJunction<?> boolForCategoryIds = queryBuilder.bool();

            List<String> catIds = getListCatId(filterRequest.getCatId());

            for (String id : catIds) {
                boolForCategoryIds
                        .should(queryBuilder.keyword()
                                .onField("category.id")
                                .matching(id).createQuery());
            }
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
        page.setNumber(pageable.getPageNumber());
        page.setTotalPages((int) Math.ceil((double) totalElements / page.getSize()));
        page.setFirst(page.getNumber() == 0);
        page.setLast(page.getTotalPages() - 1 == page.getNumber());
        page.setEmpty(page.getContent().size() == 0);
        page.setNumberOfElements(page.getContent().size());

        return page;
    }

    private List<String> getListCatId(String categoryId) throws Exception {
        if (categoryId == null) return null;
        List<String> longSet = new ArrayList<>();
        Category category;
        try {
            category = categoryRepository.findById(Long.parseLong(categoryId)).orElse(null);
        } catch (Exception e) {
            throw new Exception("Mã danh mục không đúng định dạng");
        }

        if (category != null) {
            longSet.add(category.getId().toString());
            if (category.getCategory() == null) {
                longSet.addAll(categoryRepository.findAllCatIdOfParent(category.getId()).stream().map(aLong -> aLong.toString()).collect(Collectors.toList()));
            }
        } else throw new Exception("Không tồn tại id danh mục");
        return longSet;
    }
}
