package br.com.posjava.leochacarolli.it_inventory.service;

import br.com.posjava.leochacarolli.it_inventory.exception.AssetNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.exception.InvalidAssetDataException;
import br.com.posjava.leochacarolli.it_inventory.model.Asset;
import br.com.posjava.leochacarolli.it_inventory.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public void addAsset(Asset asset) {
        if (asset == null){
            throw new InvalidAssetDataException("O ativo não pode ser nulo");
        }

        if (asset.getName() == null || asset.getName().isBlank()) {
            throw new InvalidAssetDataException("O nome do ativo não pode ser nulo, vazio ou conter apenas espaços");
        }

        if (asset.getPurchaseValue() < 0) {
            throw new InvalidAssetDataException("O valor de compra não pode ser negativo");
        }

        assetRepository.save(asset);
    }

    public Asset getAssetById(Long id) {
        return assetRepository.findById(id).orElseThrow(() -> new AssetNotFoundException("Ativo não encontrado para o ID: " + id));
    }

    public List<Asset> getAllAssets(){
        return assetRepository.findAll();
    }

    public void removeAsset(Long id) {
        if (!assetRepository.existsById(id)) {
            throw new AssetNotFoundException("Não foi possível remover, ativo não encontrado para o ID: " + id);
        }

        assetRepository.deleteById(id);
    }

    public void updateAsset(Long id, Asset asset) {
        if (!assetRepository.existsById(id)) {
            throw new AssetNotFoundException("Não foi possível localizar e alterar o ID: " + id);
        }

        asset.setId(id);
        assetRepository.save(asset);
    }

    public List<Asset> getActiveAssets(){
        return assetRepository.findByActive(true);
    }

    public List<Asset> getInactiveAssets(){
        return assetRepository.findByActive(false);
    }

    public List<Asset> getOrderedAssetsByName(){
        return assetRepository.findAllByOrderByNameAsc();
    }

    public Asset getAssetByName(String name) {
        return assetRepository.findFirstByNameContainingIgnoreCase(name)
                .orElseThrow(() -> new AssetNotFoundException("Ativo não encontrado para o nome: " + name));
    }

    public List<String> getAllAssetNames() {
        return assetRepository.findAll()
                .stream()
                .map(asset -> asset.getName())
                .toList();
    }
}
