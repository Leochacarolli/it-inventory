package br.com.posjava.leochacarolli.it_inventory.controller;

import br.com.posjava.leochacarolli.it_inventory.dto.AssetRequestDTO;
import br.com.posjava.leochacarolli.it_inventory.dto.AssetResponseDTO;
import br.com.posjava.leochacarolli.it_inventory.model.Asset;
import br.com.posjava.leochacarolli.it_inventory.model.AssetModel;
import br.com.posjava.leochacarolli.it_inventory.model.Location;
import br.com.posjava.leochacarolli.it_inventory.service.AssetModelService;
import br.com.posjava.leochacarolli.it_inventory.service.AssetService;
import br.com.posjava.leochacarolli.it_inventory.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/assets")
public class AssetController {

    private final AssetService assetService;
    private final AssetModelService assetModelService;
    private final LocationService locationService;

    public AssetController(AssetService assetService, AssetModelService assetModelService, LocationService locationService) {
        this.assetService = assetService;
        this.assetModelService = assetModelService;
        this.locationService = locationService;
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


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssetResponseDTO createAsset(@RequestBody AssetRequestDTO request){
        AssetModel model = assetModelService.getAssetModelById(request.getAssetModelId());
        Location location = locationService.getLocationById(request.getLocationId());

        Asset asset = new Asset(
                null,
                request.isActive(),
                request.getName(),
                request.getSerialNumber(),
                request.getPurchaseValue(),
                model,
                location
        );

        assetService.addAsset(asset);

        return new AssetResponseDTO(asset);
    }

    @PutMapping("/{id}")
    public AssetResponseDTO updateAsset(@PathVariable Long id, @RequestBody AssetRequestDTO request) {
        AssetModel model = assetModelService.getAssetModelById(request.getAssetModelId());
        Location location = locationService.getLocationById(request.getLocationId());

        Asset updatedAsset = new Asset(
                id,
                request.isActive(),
                request.getName(),
                request.getSerialNumber(),
                request.getPurchaseValue(),
                model,
                location
        );

        assetService.updateAsset(id, updatedAsset);

        return new AssetResponseDTO(updatedAsset);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAsset(@PathVariable Long id) {
        assetService.removeAsset(id);
    }

}
