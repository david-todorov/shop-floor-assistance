package com.shopfloor.backend.database.mappers;

import com.shopfloor.backend.api.transferobjects.editors.EditorProductTO;
import com.shopfloor.backend.database.objects.ProductDBO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class ProductDBOMapper {

    public ProductDBO initializeProductDBO(EditorProductTO productTO, Long creatorId) {
        ProductDBO productDBO = new ProductDBO();

        productDBO.setProductNumber(productTO.getProductNumber());
        productDBO.setName(productTO.getName());
        productDBO.setType(productTO.getType());
        productDBO.setCountry(productTO.getCountry());
        productDBO.setPackageSize(productTO.getPackageSize());
        productDBO.setPackageType(productTO.getPackageType());
        productDBO.setLanguage(productTO.getLanguage());
        productDBO.setDescription(productTO.getDescription());

        productDBO.setCreatedBy(creatorId);
        productDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        return productDBO;
    }

    public ProductDBO updateProductDboFrom(ProductDBO target, EditorProductTO source, Long updaterId) {
        target.setProductNumber(source.getProductNumber());
        target.setName(source.getName());
        target.setType(source.getType());
        target.setCountry(source.getCountry());
        target.setPackageSize(source.getPackageSize());
        target.setPackageType(source.getPackageType());
        target.setLanguage(source.getLanguage());
        target.setDescription(source.getDescription());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        return target;
    }
}
