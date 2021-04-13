package dtu.thebestprice.converters;

import dtu.thebestprice.entities.Category;
import dtu.thebestprice.payload.response.ShortCategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {
    public ShortCategoryResponse toShortCategoryResponse(Category category){
        if (category == null)
            return null;
        return new ShortCategoryResponse(category.getId(), category.getTitle());
    }
}
