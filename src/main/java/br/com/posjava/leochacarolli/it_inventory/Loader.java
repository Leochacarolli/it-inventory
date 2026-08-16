package br.com.posjava.leochacarolli.it_inventory;

import br.com.posjava.leochacarolli.it_inventory.exception.AssetNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.exception.DuplicateAssetException;
import br.com.posjava.leochacarolli.it_inventory.exception.InvalidAssetDataException;
import br.com.posjava.leochacarolli.it_inventory.model.*;
import br.com.posjava.leochacarolli.it_inventory.service.AssetService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Loader implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        //Listas criadas
        List<AssetModel> notebookModels = new ArrayList<>();
        List<AssetModel> desktopModels = new ArrayList<>();
        List<AssetModel> dellModels = new ArrayList<>();
        List<AssetModel> lenovoModels = new ArrayList<>();
        List<Asset> latitude5440Assets = new ArrayList<>();
        List<Asset> thinkPadE14Assets = new ArrayList<>();
        List<Asset> humanResourcesAssets = new ArrayList<>();
        List<Asset> nocAssets = new ArrayList<>();
        List<Asset> comercialAssets = new ArrayList<>();

        //Objetos criados
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

        //Chamada dos Métodos
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

        //Exibição dos dados
        System.out.println("Modelos de Notebooks:");
        notebookModels.forEach(System.out::println);
        System.out.println();
        System.out.println("Modelos Dell:");
        dellModels.forEach(System.out::println);
        System.out.println();
        System.out.println("Ativos Latitude 5440:");
        latitude5440Assets.forEach(System.out::println);
        System.out.println();
        System.out.println("Ativos NOC:");
        nocAssets.forEach(System.out::println);
        System.out.println();
        System.out.println("Ativos ThinkPad E14:");
        thinkPadE14Assets.forEach(System.out::println);
        System.out.println();


        //Testes Parte 2

        AssetService assetService = new AssetService();

        Asset hrnt01updated = new Asset(
                1L,
                true,
                "HRNT01 - Carolini",
                "4IJ18H",
                3500,
                thinkPadE14,
                humanResources
        );

        System.out.println("Teste adicionando e exibindo um ativo");
        assetService.addAsset(hrnt01);
        System.out.println(assetService.getAssetById(hrnt01.getId()));
        System.out.println();

        System.out.println("Teste adicionando novos ativos e exibindo todos");
        assetService.addAsset(nocnt01);
        assetService.addAsset(comercialnt01);
        assetService.getAllAssets().forEach(System.out::println);
        System.out.println();

        System.out.println("Teste atualizando um ativo");
        assetService.updateAsset(1L, hrnt01updated);
        System.out.println(assetService.getAssetById(1L));
        System.out.println();

        System.out.println("Teste removendo um ativo");
        assetService.removeAsset(2L);
        assetService.getAllAssets().forEach(System.out::println);
        System.out.println();

        System.out.println("Testando Exceptions");

        System.out.println("Teste Localizando por ID inexistente");
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
        } catch (InvalidAssetDataException e){
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
        } catch (InvalidAssetDataException e){
            System.out.println(e.getMessage());
            System.out.println();
        }

        System.out.println("Teste adicionando nome nulo");
        Asset assetComNomeNulo = new Asset(
                1L,
                true,
                null,
                "4IJ18H",
                3500,
                thinkPadE14,
                humanResources
        );

        try {
            assetService.addAsset(assetComNomeNulo);
        } catch (InvalidAssetDataException e){
            System.out.println(e.getMessage());
            System.out.println();
        }

        System.out.println("Teste adicionando com valor negativo");
        Asset assetComValorNegativo = new Asset(
                1L,
                true,
                "Teste",
                "4IJ18H",
                -2000,
                thinkPadE14,
                humanResources
        );

        try {
            assetService.addAsset(assetComValorNegativo);
        } catch (InvalidAssetDataException e){
            System.out.println(e.getMessage());
            System.out.println();
        }

        System.out.println("Teste adicionando ativo duplicado");
        try {
            assetService.addAsset(hrnt01);
        } catch (DuplicateAssetException e){
            System.out.println(e.getMessage());
            System.out.println();
        }
    }
}