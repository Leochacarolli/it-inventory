package br.com.posjava.leochacarolli.it_inventory.asset.controller;

import br.com.posjava.leochacarolli.it_inventory.asset.dto.AssetRequestDTO;
import br.com.posjava.leochacarolli.it_inventory.asset.dto.AssetResponseDTO;
import br.com.posjava.leochacarolli.it_inventory.asset.model.Asset;
import br.com.posjava.leochacarolli.it_inventory.asset.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(
        name = "Ativos",
        description = "Endpoints responsáveis pelo gerenciamento dos ativos"
)
@RestController
@RequestMapping("/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @Operation(
            summary = "Listar todos os ativos",
            description = "Retorna todos os ativos cadastrados no inventário"
    )
    @GetMapping
    public List<AssetResponseDTO> getAllAssets() {
        return assetService.getAllAssets()
                .stream()
                .map(assetService::toResponseDTO)
                .toList();
    }


    @Operation(
            summary = "Buscar ativo por ID",
            description = "Retorna um ativo a partir do seu identificador"
    )
    @GetMapping("/{id}")
    public AssetResponseDTO getAssetById(
            @Parameter(description = "ID do ativo", example = "1")
            @PathVariable Long id) {

        Asset asset = assetService.getAssetById(id);

        return assetService.toResponseDTO(asset);
    }


    @Operation(
            summary = "Listar ativos com status ativo",
            description = "Retorna somente os ativos que estão com status ativo"
    )
    @GetMapping("/active")
    public List<AssetResponseDTO> getActiveAssets() {
        return assetService.getActiveAssets()
                .stream()
                .map(assetService::toResponseDTO)
                .toList();
    }


    @Operation(
            summary = "Listar ativos com status inativo",
            description = "Retorna somente os ativos que estão com status inativo"
    )
    @GetMapping("/inactive")
    public List<AssetResponseDTO> getInactiveAssets() {
        return assetService.getInactiveAssets()
                .stream()
                .map(assetService::toResponseDTO)
                .toList();
    }


    @Operation(
            summary = "Listar ativos ordenados por nome",
            description = "Retorna os ativos cadastrados em ordem alfabética pelo nome"
    )
    @GetMapping("/ordered")
    public List<AssetResponseDTO> getOrderedAssetsByName() {
        return assetService.getOrderedAssetsByName()
                .stream()
                .map(assetService::toResponseDTO)
                .toList();
    }


    @Operation(
            summary = "Buscar ativo por nome",
            description = "Busca um ativo utilizando parte ou o nome completo"
    )
    @GetMapping("/search")
    public AssetResponseDTO getAssetByName(
            @Parameter(
                    description = "Nome ou parte do nome do ativo",
                    example = "TESTENT01"
            )
            @RequestParam String name) {

        Asset asset = assetService.getAssetByName(name);

        return assetService.toResponseDTO(asset);
    }


    @Operation(
            summary = "Criar ativo",
            description = "Cadastra um novo ativo no inventário"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssetResponseDTO createAsset(
            @Valid @RequestBody AssetRequestDTO request) {

        Asset asset = assetService.createAsset(request);

        return assetService.toResponseDTO(asset);
    }


    @Operation(
            summary = "Atualizar ativo",
            description = "Atualiza os dados de um ativo existente"
    )
    @PutMapping("/{id}")
    public AssetResponseDTO updateAsset(
            @Parameter(description = "ID do ativo", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody AssetRequestDTO request) {

        Asset updatedAsset = assetService.updateAsset(id, request);

        return assetService.toResponseDTO(updatedAsset);
    }


    @Operation(
            summary = "Excluir ativo",
            description = "Remove um ativo do inventário a partir do seu identificador"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAsset(
            @Parameter(description = "ID do ativo", example = "1")
            @PathVariable Long id) {

        assetService.removeAsset(id);
    }
}
