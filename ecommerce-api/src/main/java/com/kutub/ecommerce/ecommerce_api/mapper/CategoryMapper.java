package com.kutub.ecommerce.ecommerce_api.mapper;

import com.kutub.ecommerce.ecommerce_api.dto.CategoryDTO;
import com.kutub.ecommerce.ecommerce_api.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category category);
    
    Category toEntity(CategoryDTO categoryDTO);
    
    List<CategoryDTO> toDTOList(List<Category> categories);
}
