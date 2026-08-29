package br.com.posjava.leochacarolli.it_inventory.service;

import br.com.posjava.leochacarolli.it_inventory.exception.AssetNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.exception.DuplicateAssetException;
import br.com.posjava.leochacarolli.it_inventory.exception.InvalidAssetDataException;
import br.com.posjava.leochacarolli.it_inventory.model.Asset;
import br.com.posjava.leochacarolli.it_inventory.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final Map<Long, Asset> assets = new HashMap<>();

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

        if (assets.containsKey(asset.getId())) {
            throw new DuplicateAssetException("Já existe um ativo com ID: " + asset.getId());
        }

        assetRepository.save(asset);
    }

    public Asset getAssetById(Long id) {
        if (assets.containsKey(id)) {
            return assets.get(id);
        } else {
            throw new AssetNotFoundException("Ativo não encontrado para o ID: " + id);
        }
    }

    public List<Asset> getAllAssets(){
        return assetRepository.findAll();
    }

    public void removeAsset(Long id){
        if (assets.containsKey(id)) {
            assets.remove(id);
        } else {
            throw new AssetNotFoundException("Não foi possível remover, ativo não encontrado para o ID: " + id);
        }
    }

    public void updateAsset(Long id, Asset asset){
        if(assets.containsKey(id)){
            assets.put(id, asset);
        } else {
            throw new AssetNotFoundException("Não foi possível localizar e alterar o ID: " + id);
        }
    }

    public List<Asset> getActiveAssets(){
        return assets.values()
                .stream()
                .filter(asset -> asset.isActive())
                .toList();
    }

    public List<Asset> getInactiveAssets(){
        return assets.values()
                .stream()
                .filter(asset -> asset.isActive() == false)
                .toList();
    }

    public List<Asset> getOrderedAssetsByName(){
        return assets.values()
                .stream()
                .sorted(Comparator.comparing(asset -> asset.getName()))
                .toList();
    }

    public Asset getAssetByName(String name) {
        return assets.values()
                .stream()
                .filter(asset -> asset.getName().toLowerCase().contains(name.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new AssetNotFoundException("Ativo não encontrado para o nome: " + name));
    }

    public List<String> getAllAssetNames() {
        return assets.values()
                .stream()
                .map(asset -> asset.getName())
                .toList();
    }

    private Long generateNextId(){
        if (assets.isEmpty()) {
            return 1L;
        }

        return Collections.max(assets.keySet()) + 1;
    }
}
