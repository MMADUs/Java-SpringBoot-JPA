package com.domain.restful.usecase.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domain.restful.model.entity.MerchantEntity;
import com.domain.restful.model.repository.MerchantRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MerchantService {
    @Autowired
    private MerchantRepository merchantRepository;

    public List<MerchantEntity> getAllMerchant() {
        return merchantRepository.findAll();
    }

    public MerchantEntity getMerchantById(Long id) {
        return merchantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Merchant not found with id: " + id));
    }

    public MerchantEntity createMerchant(MerchantEntity merchant) {
        return merchantRepository.save(merchant);
    }

    public MerchantEntity updateMerchant(Long id, MerchantEntity merchantDetails) {
        MerchantEntity Merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Merchant not found with id: " + id));

        Merchant.setName(merchantDetails.getName());
        Merchant.setLocation(merchantDetails.getLocation());

        return merchantRepository.save(Merchant);
    }

    public void deleteMerchant(Long id) {
        if (!merchantRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot delete. Merchant not found with id: " + id);
        }
        merchantRepository.deleteById(id);
    }
}
