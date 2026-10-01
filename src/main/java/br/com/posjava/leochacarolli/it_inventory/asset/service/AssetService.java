package br.com.posjava.leochacarolli.it_inventory.asset.service;

import br.com.posjava.leochacarolli.it_inventory.asset.exception.AssetNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.asset.exception.InvalidAssetDataException;
import br.com.posjava.leochacarolli.it_inventory.asset.model.Asset;
import br.com.posjava.leochacarolli.it_inventory.asset.repository.AssetRepository;
import br.com.posjava.leochacarolli.it_inventory.asset.dto.AssetRequestDTO;
import br.com.posjava.leochacarolli.it_inventory.catalog.model.AssetModel;
import br.com.posjava.leochacarolli.it_inventory.catalog.service.AssetModelService;
import br.com.posjava.leochacarolli.it_inventory.location.model.Location;
import br.com.posjava.leochacarolli.it_inventory.location.service.LocationService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetModelService assetModelService;
    private final LocationService locationService;

    public AssetService(AssetRepository assetRepository, AssetModelService assetModelService, LocationService locationService) {
        this.assetRepository = assetRepository;
        this.assetModelService = assetModelService;
        this.locationService = locationService;
    }

    public Asset createAsset(AssetRequestDTO request) {
        AssetModel model =
                assetModelService.getAssetModelById(
                        request.getAssetModelId()
                );

        Location location =
                locationService.getLocationById(
                        request.getLocationId()
                );

        Asset asset = new Asset(
                null,
                request.isActive(),
                request.getName(),
                request.getSerialNumber(),
                request.getPurchaseValue(),
                model,
                location
        );

        addAsset(asset);

        return asset;
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

    public Asset updateAsset(
            Long id,
            AssetRequestDTO request) {

        Asset asset = getAssetById(id);

        AssetModel model =
                assetModelService.getAssetModelById(
                        request.getAssetModelId()
                );

        Location location =
                locationService.getLocationById(
                        request.getLocationId()
                );

        asset.setActive(request.isActive());
        asset.setName(request.getName());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setPurchaseValue(request.getPurchaseValue());
        asset.setModel(model);
        asset.setLocation(location);

        return assetRepository.save(asset);
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
