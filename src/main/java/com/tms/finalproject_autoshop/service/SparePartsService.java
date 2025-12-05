package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.model.*;
import com.tms.finalproject_autoshop.model.dto.PartDto;
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

    public Boolean updateSparePart(PartDto partDto) {
        try {
            SpareParts newSparePart = new SpareParts();
            newSparePart.setName(partDto.getName());
            newSparePart.setBrand(partDto.getBrand());
            newSparePart.setPrice(partDto.getPrice());
            newSparePart.setImage(partDto.getImage());
            newSparePart.setArticle(partDto.getArticle());
            newSparePart.setCarBrand(partDto.getCarBrand());
            sparePartsRepository.save(newSparePart);
        } catch (Exception ex) {
            System.out.println("Error in saving user: " + ex.getMessage());
        }
        return true;
    }

    public Boolean updateServicePart(PartDto partDto) {
        try {
            ServiceParts newServicePart = new ServiceParts();
            newServicePart.setName(partDto.getName());
            newServicePart.setCarBrand(partDto.getCarBrand());
            newServicePart.setPrice(partDto.getPrice());
            newServicePart.setImage(partDto.getImage());
            newServicePart.setArticle(partDto.getArticle());
            newServicePart.setBrand(partDto.getBrand());
            servicePartsRepository.save(newServicePart);
        } catch (Exception ex) {
            System.out.println("Error in saving user: " + ex.getMessage());
        }
        return true;
    }

    public Boolean updateOilPart(PartDto partDto) {
        try {
            OilParts newOilPart = new OilParts();
            newOilPart.setName(partDto.getName());
            newOilPart.setBrand(partDto.getBrand());
            newOilPart.setPrice(partDto.getPrice());
            newOilPart.setImage(partDto.getImage());
            newOilPart.setArticle(partDto.getArticle());
            oilPartsRepository.save(newOilPart);
        } catch (Exception ex) {
            System.out.println("Error in saving user: " + ex.getMessage());
        }
        return true;
    }


    public Object getProductById(String type, Long id) {
        if (ProductTypes.SPARE_PART.equals(type)) {
            Optional<SpareParts> spareParts = sparePartsRepository.findById(id);
        } else if (ProductTypes.OIL_PART.equals(type)) {
            Optional<OilParts> oilParts = oilPartsRepository.findById(id);
        } else if (ProductTypes.SERVICE_PART.equals(type)) {
            Optional<ServiceParts> serviceParts = servicePartsRepository.findById(id);
        } else {
            return Optional.empty();
        }
    return null;
    }


    public Object createProduct(String type, SpareParts spareParts, OilParts oilParts, ServiceParts serviceParts) {
        if (ProductTypes.SPARE_PART.equals(type)) {
            return sparePartsRepository.save(spareParts);
        } else if (ProductTypes.OIL_PART.equals(type)) {
            return oilPartsRepository.save(oilParts);
        } else if (ProductTypes.SERVICE_PART.equals(type)) {
            return servicePartsRepository.save(serviceParts);
        } else {
            return Optional.empty();
        }

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

    public void deleteProductById(String type, Long id) {
        if (ProductTypes.SPARE_PART.equals(type)) {
            sparePartsRepository.deleteById(id);
        } else if (ProductTypes.OIL_PART.equals(type)) {
            oilPartsRepository.deleteById(id);
        } else if (ProductTypes.SERVICE_PART.equals(type)) {
            servicePartsRepository.deleteById(id);
        } else {
            throw new NullPointerException();
        }
    }

    public List<Catalog> getAllCatalog() {
        return catalogRepository.findAll();
    }

    public Optional<Catalog> getCatalogById(Long id) {
        return catalogRepository.findById(id);
    }

    public Catalog createCatalog(Catalog catalog) {
        catalogRepository.save(catalog);
        return catalog;
    }

    public Optional<Catalog> deleteCatalogById(Long id) {
        catalogRepository.deleteById(id);
        return Optional.empty();
    }

    public Catalog updateCatalog(Catalog catalog, Long id) {
        Catalog newCatalog = new Catalog();
        if(catalogRepository.findById(id).isPresent()) {
            newCatalog.setName(catalog.getName());
            newCatalog.setDescription(catalog.getDescription());
            newCatalog.setImage(catalog.getImage());
        } else{
            throw new RuntimeException("Catalog id not found");
        }
        return catalogRepository.save(newCatalog);
    }


}
