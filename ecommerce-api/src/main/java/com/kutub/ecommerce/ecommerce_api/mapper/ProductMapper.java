package com.kutub.ecommerce.ecommerce_api.mapper;

import com.kutub.ecommerce.ecommerce_api.dto.ProductDTO;
import com.kutub.ecommerce.ecommerce_api.entity.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "imageUrl", ignore = true) // আমরা নিচে এটি ম্যানুয়ালি সেট করব
    ProductDTO toDTO(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductDTO productDTO);

    List<ProductDTO> toDTOList(List<Product> products);

    // ডিটিও তৈরির পর ছবির ফুল ইউআরএল বসিয়ে দিচ্ছি
    @AfterMapping
    default void setImageUrl(Product product, @MappingTarget ProductDTO dto) {
        if (product.getImageName() != null) {
            dto.setImageUrl("http://localhost:8080/api/v1/images/" + product.getImageName());
        }
    }
}
