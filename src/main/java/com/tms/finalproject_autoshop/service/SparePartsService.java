package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.model.*;
import com.tms.finalproject_autoshop.model.dto.ProductUpdateDto;
import com.tms.finalproject_autoshop.repository.CatalogRepository;
import com.tms.finalproject_autoshop.repository.OilPartsRepository;
import com.tms.finalproject_autoshop.repository.SparePartsRepository;
import com.tms.finalproject_autoshop.repository.ServicePartsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SparePartsService {
    private final SparePartsRepository sparePartsRepository;
    private final ServicePartsRepository servicePartsRepository;
    private final OilPartsRepository oilPartsRepository;
    private final CatalogRepository catalogRepository;


    public SparePartsService(SparePartsRepository sparePartsRepository,
                             ServicePartsRepository servicePartsRepository,
                             OilPartsRepository oilPartsRepository,
                             CatalogRepository catalogRepository) {
        this.sparePartsRepository = sparePartsRepository;
        this.servicePartsRepository = servicePartsRepository;
        this.oilPartsRepository = oilPartsRepository;
        this.catalogRepository = catalogRepository;
    }


    public Optional<?> updateProduct(String type, Long id, ProductAbstract product) {
        Optional<?> productFromDB = getProductById(type, id);
        if (ProductTypes.SERVICE_PART.equals(type)) {
            ServiceParts servicePartsSave = (ServiceParts) product;
            return Optional.of(servicePartsRepository.saveAndFlush(servicePartsSave));
        } else if (ProductTypes.OIL_PART.equals(type)) {
            OilParts oilPartsSave = (OilParts) product;
            return Optional.of(oilPartsRepository.saveAndFlush(oilPartsSave));
        } else if (ProductTypes.SPARE_PART.equals(type)) {
            SpareParts sparePartsSave = (SpareParts) product;
            return Optional.of(sparePartsRepository.saveAndFlush(sparePartsSave));
        }
        return Optional.empty();
    }


    public Optional<?> getProductById(String type, Long id) {
        if (ProductTypes.SPARE_PART.equals(type)) {
            return sparePartsRepository.findById(id);
        } else if (ProductTypes.OIL_PART.equals(type)) {
            return oilPartsRepository.findById(id);
        } else if (ProductTypes.SERVICE_PART.equals(type)) {
            return servicePartsRepository.findById(id);
        }
        return Optional.empty();
    }


    public Optional<?> createSparePart(String type, ProductAbstract product) {
        if (ProductTypes.SPARE_PART.equals(type)) {
            return Optional.of(sparePartsRepository.save((SpareParts) product));
        } else if (ProductTypes.OIL_PART.equals(type)) {
            return Optional.of(oilPartsRepository.save((OilParts) product));
        } else if (ProductTypes.SERVICE_PART.equals(type)) {
            return Optional.of(servicePartsRepository.save((ServiceParts) product));
        }
        return Optional.empty();
    }


    public List<?> getAllProducts(String type) {
        if (ProductTypes.SPARE_PART.equals(type)) {
            return sparePartsRepository.findAll();
        } else if (ProductTypes.OIL_PART.equals(type)) {
            return oilPartsRepository.findAll();
        } else if (ProductTypes.SERVICE_PART.equals(type)) {
            return servicePartsRepository.findAll();
        } else {
            throw new NullPointerException();
        }
    }

    public Boolean deleteProductById(String type, Long id) {
        if (ProductTypes.SPARE_PART.equals(type)) {
            sparePartsRepository.deleteById(id);
        } else if (ProductTypes.OIL_PART.equals(type)) {
            oilPartsRepository.deleteById(id);
        } else if (ProductTypes.SERVICE_PART.equals(type)) {
            servicePartsRepository.deleteById(id);
        }
        throw new NullPointerException();
    }
}

