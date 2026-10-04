package br.com.posjava.leochacarolli.it_inventory.asset.service;

import br.com.posjava.leochacarolli.it_inventory.asset.exception.AssetNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.asset.exception.InvalidAssetDataException;
import br.com.posjava.leochacarolli.it_inventory.asset.model.Asset;
import br.com.posjava.leochacarolli.it_inventory.asset.repository.AssetRepository;
import br.com.posjava.leochacarolli.it_inventory.asset.dto.AssetRequestDTO;
import br.com.posjava.leochacarolli.it_inventory.catalog.model.AssetModel;
import br.com.posjava.leochacarolli.it_inventory.catalog.service.AssetModelService;
import org.springframework.stereotype.Service;
import br.com.posjava.leochacarolli.it_inventory.asset.dto.AssetResponseDTO;
import br.com.posjava.leochacarolli.it_inventory.location.client.LocationClient;
import br.com.posjava.leochacarolli.it_inventory.location.client.LocationClientResponseDTO;
import br.com.posjava.leochacarolli.it_inventory.location.client.exception.LocationServiceUnavailableException;
import br.com.posjava.leochacarolli.it_inventory.location.client.exception.LocationNotFoundException;
import feign.FeignException;
import feign.RetryableException;

import java.util.*;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetModelService assetModelService;
    private final LocationClient locationClient;

    public AssetService(AssetRepository assetRepository, AssetModelService assetModelService, LocationClient locationClient) {
        this.assetRepository = assetRepository;
        this.assetModelService = assetModelService;
        this.locationClient = locationClient;
    }

    public Asset createAsset(AssetRequestDTO request) {

        AssetModel model =
                assetModelService.getAssetModelById(
                        request.getAssetModelId()
                );

        LocationClientResponseDTO location =
                getLocationFromService(
                        request.getLocationId()
                );

        Asset asset = new Asset(
                null,
                request.isActive(),
                request.getName(),
                request.getSerialNumber(),
                request.getPurchaseValue(),
                model,
                location.getId()
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

        LocationClientResponseDTO location =
                getLocationFromService(
                        request.getLocationId()
                );

        asset.setActive(request.isActive());
        asset.setName(request.getName());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setPurchaseValue(request.getPurchaseValue());
        asset.setModel(model);
        asset.setLocationId(location.getId());

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

    public AssetResponseDTO toResponseDTO(Asset asset) {

        LocationClientResponseDTO location =
                getLocationFromService(
                        asset.getLocationId()
                );

        return new AssetResponseDTO(
                asset,
                location.getName()
        );
    }

    private LocationClientResponseDTO getLocationFromService(Long locationId) {
        try {
            return locationClient.getLocationById(locationId);

        } catch (FeignException.NotFound exception) {

            throw new LocationNotFoundException(
                    "Localização não encontrada para o ID: " + locationId
            );

        } catch (RetryableException exception) {

            throw new LocationServiceUnavailableException(
                    "O serviço de localizações está temporariamente indisponível"
            );
        }
    }
}
