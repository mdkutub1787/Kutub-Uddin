package com.kutub.ecommerce.ecommerce_api.mapper;

import com.kutub.ecommerce.ecommerce_api.dto.ProductDTO;
import com.kutub.ecommerce.ecommerce_api.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductDTO toDTO(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductDTO productDTO);

    List<ProductDTO> toDTOList(List<Product> products);
}
