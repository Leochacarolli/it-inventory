package br.com.posjava.leochacarolli.it_inventory.config;

import br.com.posjava.leochacarolli.it_inventory.asset.model.Asset;
import br.com.posjava.leochacarolli.it_inventory.asset.service.AssetService;
import br.com.posjava.leochacarolli.it_inventory.catalog.model.AssetModel;
import br.com.posjava.leochacarolli.it_inventory.catalog.model.Category;
import br.com.posjava.leochacarolli.it_inventory.catalog.model.Manufacturer;
import br.com.posjava.leochacarolli.it_inventory.catalog.service.AssetModelService;
import br.com.posjava.leochacarolli.it_inventory.catalog.service.CategoryService;
import br.com.posjava.leochacarolli.it_inventory.catalog.service.ManufacturerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Loader implements CommandLineRunner {

    private final AssetService assetService;
    private final AssetModelService assetModelService;
    private final CategoryService categoryService;
    private final ManufacturerService manufacturerService;

    public Loader(
            AssetService assetService,
            AssetModelService assetModelService,
            CategoryService categoryService,
            ManufacturerService manufacturerService) {

        this.assetService = assetService;
        this.assetModelService = assetModelService;
        this.categoryService = categoryService;
        this.manufacturerService = manufacturerService;
    }

    @Override
    public void run(String... args) {

        // Listas utilizadas nos relacionamentos locais
        List<AssetModel> notebookModels = new ArrayList<>();
        List<AssetModel> desktopModels = new ArrayList<>();

        List<AssetModel> dellModels = new ArrayList<>();
        List<AssetModel> lenovoModels = new ArrayList<>();

        List<Asset> latitude5440Assets = new ArrayList<>();
        List<Asset> thinkPadE14Assets = new ArrayList<>();


        // IDs das localizações pertencentes ao location-service
        Long humanResourcesLocationId = 1L;
        Long nocLocationId = 2L;
        Long comercialLocationId = 3L;


        // Criação das categorias
        Category notebook = new Category(
                null,
                true,
                "Notebook",
                "",
                notebookModels
        );

        Category desktop = new Category(
                null,
                true,
                "Desktop",
                "",
                desktopModels
        );


        // Criação dos fabricantes
        Manufacturer dell = new Manufacturer(
                null,
                true,
                "Dell",
                "USA",
                dellModels
        );

        Manufacturer lenovo = new Manufacturer(
                null,
                true,
                "Lenovo",
                "China",
                lenovoModels
        );


        // Criação dos modelos
        AssetModel latitude5440 = new AssetModel(
                null,
                true,
                "Latitude 5440",
                notebook,
                dell,
                latitude5440Assets
        );

        AssetModel thinkPadE14 = new AssetModel(
                null,
                true,
                "ThinkPad E14",
                notebook,
                lenovo,
                thinkPadE14Assets
        );


        // Criação dos ativos
        // A aplicação principal não possui mais objetos Location.
        // Ela armazena somente o ID pertencente ao location-service.

        Asset hrnt01 = new Asset(
                null,
                true,
                "HRNT01",
                "4IJ18H",
                3000,
                thinkPadE14,
                humanResourcesLocationId
        );

        Asset nocnt01 = new Asset(
                null,
                true,
                "NOCNT01",
                "9YTR4O",
                5000,
                latitude5440,
                nocLocationId
        );

        Asset comercialnt01 = new Asset(
                null,
                true,
                "COMERCIALNT01",
                "42JLRW",
                3000,
                thinkPadE14,
                comercialLocationId
        );


        // Cadastro inicial das categorias
        categoryService.addCategory(notebook);
        categoryService.addCategory(desktop);


        // Cadastro inicial dos fabricantes
        manufacturerService.addManufacturer(dell);
        manufacturerService.addManufacturer(lenovo);


        // Montagem dos relacionamentos locais

        notebookModels.add(latitude5440);
        notebookModels.add(thinkPadE14);

        dellModels.add(latitude5440);
        lenovoModels.add(thinkPadE14);

        thinkPadE14Assets.add(hrnt01);
        thinkPadE14Assets.add(comercialnt01);

        latitude5440Assets.add(nocnt01);


        // Cadastro inicial dos modelos
        assetModelService.addAssetModel(latitude5440);
        assetModelService.addAssetModel(thinkPadE14);


        // Cadastro inicial dos ativos
        assetService.addAsset(hrnt01);
        assetService.addAsset(nocnt01);
        assetService.addAsset(comercialnt01);


        System.out.println("Ativos cadastrados no banco:");
        assetService.getAllAssets()
                .forEach(System.out::println);
    }
}