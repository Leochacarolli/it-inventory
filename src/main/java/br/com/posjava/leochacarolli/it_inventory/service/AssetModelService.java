package br.com.posjava.leochacarolli.it_inventory.service;

import br.com.posjava.leochacarolli.it_inventory.exception.AssetModelNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.model.AssetModel;
import br.com.posjava.leochacarolli.it_inventory.repository.AssetModelRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AssetModelService {

    private final Map<Long, AssetModel> models = new HashMap<>();
    private final AssetModelRepository assetModelRepository;

    public AssetModelService(AssetModelRepository assetModelRepository) {
        this.assetModelRepository = assetModelRepository;
    }

    public AssetModel getAssetModelById(Long id){
        if (models.containsKey(id)) {
            return models.get(id);
        } else {
            throw new AssetModelNotFoundException("Asset Model não encontrado para o ID: " + id);
        }
    }

    public void addAssetModel(AssetModel model) {
        assetModelRepository.save(model);
    }
}
