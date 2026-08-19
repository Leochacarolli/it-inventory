package br.com.posjava.leochacarolli.it_inventory.controller;

import br.com.posjava.leochacarolli.it_inventory.dto.AssetResponseDTO;
import br.com.posjava.leochacarolli.it_inventory.model.Asset;
import br.com.posjava.leochacarolli.it_inventory.service.AssetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public List<AssetResponseDTO> getAllAssets(){
        List<Asset> assets = assetService.getAllAssets();
        List<AssetResponseDTO> response = new ArrayList<>();

        for (Asset asset : assets) {
            response.add(new AssetResponseDTO(asset));
        }

        return response;
    }

    @GetMapping("/{id}")
    public AssetResponseDTO getAssetById(@PathVariable Long id){
        Asset asset = assetService.getAssetById(id);
        return new AssetResponseDTO(asset);
    }

}
