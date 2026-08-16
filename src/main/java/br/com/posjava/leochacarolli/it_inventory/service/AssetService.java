package br.com.posjava.leochacarolli.it_inventory.service;

import br.com.posjava.leochacarolli.it_inventory.exception.AssetNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.exception.DuplicateAssetException;
import br.com.posjava.leochacarolli.it_inventory.exception.InvalidAssetDataException;
import br.com.posjava.leochacarolli.it_inventory.model.Asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetService {

    private final Map<Long, Asset> assets = new HashMap<>();

    public void addAsset(Asset asset) {
        if (asset == null) {
            throw new InvalidAssetDataException("O ativo não pode ser nulo");
        }

        if (asset.getId() == null) {
            throw new InvalidAssetDataException("O ID do ativo não pode ser nulo");
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

        assets.put(asset.getId(), asset);
    }

    public Asset getAssetById(Long id) {
        if (assets.containsKey(id)) {
            return assets.get(id);
        } else {
            throw new AssetNotFoundException("Ativo não encontrado para o ID: " + id);
        }
    }

//    public Asset getAssetByName(String name) {
//        return assets.get(name);
//    }

    public List<Asset> getAllAssets(){
        return new ArrayList<>(assets.values());
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
}
