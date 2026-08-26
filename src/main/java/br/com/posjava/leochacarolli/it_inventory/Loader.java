package br.com.posjava.leochacarolli.it_inventory;

import br.com.posjava.leochacarolli.it_inventory.exception.AssetNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.exception.DuplicateAssetException;
import br.com.posjava.leochacarolli.it_inventory.exception.InvalidAssetDataException;
import br.com.posjava.leochacarolli.it_inventory.model.*;
import br.com.posjava.leochacarolli.it_inventory.service.AssetModelService;
import br.com.posjava.leochacarolli.it_inventory.service.AssetService;
import br.com.posjava.leochacarolli.it_inventory.service.LocationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Loader implements CommandLineRunner {

    private final AssetService assetService;
    private final AssetModelService assetModelService;
    private final LocationService locationService;

    public Loader(AssetService assetService, AssetModelService assetModelService, LocationService locationService) {
        this.assetService = assetService;
        this.assetModelService = assetModelService;
        this.locationService = locationService;
    }

    @Override
    public void run(String... args) throws Exception {


        // Listas utilizadas nos relacionamentos
        List<AssetModel> notebookModels = new ArrayList<>();
        List<AssetModel> desktopModels = new ArrayList<>();
        List<AssetModel> dellModels = new ArrayList<>();
        List<AssetModel> lenovoModels = new ArrayList<>();
        List<Asset> latitude5440Assets = new ArrayList<>();
        List<Asset> thinkPadE14Assets = new ArrayList<>();
        List<Asset> humanResourcesAssets = new ArrayList<>();
        List<Asset> nocAssets = new ArrayList<>();
        List<Asset> comercialAssets = new ArrayList<>();


        // Criação dos objetos
        Category notebook = new Category(
                1L,
                true,
                "Notebook",
                "",
                notebookModels
        );

        Category desktop = new Category(
                2L,
                true,
                "Desktop",
                "",
                desktopModels
        );

        Manufacturer dell = new Manufacturer(
                1L,
                true,
                "Dell",
                "USA",
                dellModels
        );

        Manufacturer lenovo = new Manufacturer(
                2L,
                true,
                "Lenovo",
                "China",
                lenovoModels
        );

        AssetModel latitude5440 = new AssetModel(
                1L,
                true,
                "Latitude 5440",
                notebook,
                dell,
                latitude5440Assets
        );

        AssetModel thinkPadE14 = new AssetModel(
                2L,
                true,
                "ThinkPad E14",
                notebook,
                lenovo,
                thinkPadE14Assets
        );

        Location humanResources = new Location(
                1L,
                true,
                "Human Resources",
                13,
                "",
                humanResourcesAssets
        );

        Location noc = new Location(
                2L,
                true,
                "NOC",
                1,
                "",
                nocAssets
        );

        Location comercial = new Location(
                3L,
                true,
                "Comercial",
                13,
                "",
                comercialAssets
        );

        Asset hrnt01 = new Asset(
                1L,
                true,
                "HRNT01",
                "4IJ18H",
                3000,
                thinkPadE14,
                humanResources
        );

        Asset nocnt01 = new Asset(
                2L,
                true,
                "NOCNT01",
                "9YTR4O",
                5000,
                latitude5440,
                noc
        );

        Asset comercialnt01 = new Asset(
                3L,
                true,
                "COMERCIALNT01",
                "42JLRW",
                3000,
                thinkPadE14,
                comercial
        );


        // Montagem dos relacionamentos
        notebookModels.add(latitude5440);
        notebookModels.add(thinkPadE14);

        dellModels.add(latitude5440);
        lenovoModels.add(thinkPadE14);

        thinkPadE14Assets.add(hrnt01);
        thinkPadE14Assets.add(comercialnt01);
        latitude5440Assets.add(nocnt01);

        humanResourcesAssets.add(hrnt01);
        nocAssets.add(nocnt01);
        comercialAssets.add(comercialnt01);


        // Cadastro inicial dos modelos
        assetModelService.addAssetModel(latitude5440);
        assetModelService.addAssetModel(thinkPadE14);


        // Cadastro inicial dos ativos
        assetService.addAsset(hrnt01);
        assetService.addAsset(nocnt01);
        assetService.addAsset(comercialnt01);


        // Cadastro inicial das localizações
        locationService.addLocation(noc);
        locationService.addLocation(comercial);
        locationService.addLocation(humanResources);


        // CRUD

        System.out.println("Listando todos os ativos cadastrados");
        assetService.getAllAssets().forEach(System.out::println);
        System.out.println();


        System.out.println("Buscando ativo pelo ID");
        System.out.println(assetService.getAssetById(1L));
        System.out.println();


        Asset hrnt01updated = new Asset(
                1L,
                false,
                "HRNT01 - Carolini",
                "4IJ18H",
                3500,
                thinkPadE14,
                humanResources
        );

        System.out.println("Atualizando ativo");
        assetService.updateAsset(1L, hrnt01updated);
        System.out.println(assetService.getAssetById(1L));
        System.out.println();


        // Streams e Lambdas

        System.out.println("Listando todos os ativos com status ativo");
        assetService.getActiveAssets().forEach(System.out::println);
        System.out.println();


        System.out.println("Listando todos os ativos com status inativo");
        assetService.getInactiveAssets().forEach(System.out::println);
        System.out.println();


        System.out.println("Listando ativos ordenados por nome");
        assetService.getOrderedAssetsByName().forEach(System.out::println);
        System.out.println();


        System.out.println("Fazendo uma busca de ativo por nome");
        System.out.println(assetService.getAssetByName("HRNT01"));
        System.out.println();


        System.out.println("Listando somente os nomes dos ativos");
        assetService.getAllAssetNames().forEach(System.out::println);
        System.out.println();


        // Remoção após os testes com Streams
        System.out.println("Removendo um ativo");
        assetService.removeAsset(2L);
        assetService.getAllAssets().forEach(System.out::println);
        System.out.println();


        // Testes de Exceptions

        System.out.println("Testando Exceptions");
        System.out.println();


        System.out.println("Teste localizando por ID inexistente");
        try {
            assetService.getAssetById(99L);
        } catch (AssetNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println();
        }


        System.out.println("Teste removendo ID inexistente");
        try {
            assetService.removeAsset(99L);
        } catch (AssetNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println();
        }


        System.out.println("Teste atualizando ID inexistente");
        try {
            assetService.updateAsset(99L, hrnt01updated);
        } catch (AssetNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println();
        }


        System.out.println("Teste adicionando ativo nulo");
        try {
            assetService.addAsset(null);
        } catch (InvalidAssetDataException e) {
            System.out.println(e.getMessage());
            System.out.println();
        }


        System.out.println("Teste adicionando ID nulo");

        Asset assetComIdNulo = new Asset(
                null,
                true,
                "Teste",
                "4IJ18H",
                3500,
                thinkPadE14,
                humanResources
        );

        try {
            assetService.addAsset(assetComIdNulo);
        } catch (InvalidAssetDataException e) {
            System.out.println(e.getMessage());
            System.out.println();
        }


        System.out.println("Teste adicionando nome nulo");

        Asset assetComNomeNulo = new Asset(
                4L,
                true,
                null,
                "4IJ18H",
                3500,
                thinkPadE14,
                humanResources
        );

        try {
            assetService.addAsset(assetComNomeNulo);
        } catch (InvalidAssetDataException e) {
            System.out.println(e.getMessage());
            System.out.println();
        }


        System.out.println("Teste adicionando nome vazio");

        Asset assetComNomeVazio = new Asset(
                5L,
                true,
                "   ",
                "4IJ18H",
                3500,
                thinkPadE14,
                humanResources
        );

        try {
            assetService.addAsset(assetComNomeVazio);
        } catch (InvalidAssetDataException e) {
            System.out.println(e.getMessage());
            System.out.println();
        }


        System.out.println("Teste adicionando com valor negativo");

        Asset assetComValorNegativo = new Asset(
                6L,
                true,
                "Teste",
                "4IJ18H",
                -2000,
                thinkPadE14,
                humanResources
        );

        try {
            assetService.addAsset(assetComValorNegativo);
        } catch (InvalidAssetDataException e) {
            System.out.println(e.getMessage());
            System.out.println();
        }


        System.out.println("Teste adicionando ativo duplicado");

        try {
            assetService.addAsset(hrnt01updated);
        } catch (DuplicateAssetException e) {
            System.out.println(e.getMessage());
            System.out.println();
        }


        System.out.println("Teste método getAssetModelById");
        System.out.println(assetModelService.getAssetModelById(2L));
    }
}