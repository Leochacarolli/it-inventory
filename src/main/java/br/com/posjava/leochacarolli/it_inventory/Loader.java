package br.com.posjava.leochacarolli.it_inventory;

import br.com.posjava.leochacarolli.it_inventory.exception.AssetNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.exception.DuplicateAssetException;
import br.com.posjava.leochacarolli.it_inventory.exception.InvalidAssetDataException;
import br.com.posjava.leochacarolli.it_inventory.model.*;
import br.com.posjava.leochacarolli.it_inventory.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Loader implements CommandLineRunner {

    private final AssetService assetService;
    private final AssetModelService assetModelService;
    private final LocationService locationService;
    private final CategoryService categoryService;
    private final ManufacturerService manufacturerService;

    public Loader(AssetService assetService, AssetModelService assetModelService, LocationService locationService, CategoryService categoryService, ManufacturerService manufacturerService) {
        this.assetService = assetService;
        this.assetModelService = assetModelService;
        this.locationService = locationService;
        this.categoryService = categoryService;
        this.manufacturerService = manufacturerService;
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

        Location humanResources = new Location(
                null,
                true,
                "Human Resources",
                13,
                "",
                humanResourcesAssets
        );

        Location noc = new Location(
                null,
                true,
                "NOC",
                1,
                "",
                nocAssets
        );

        Location comercial = new Location(
                null,
                true,
                "Comercial",
                13,
                "",
                comercialAssets
        );

        Asset hrnt01 = new Asset(
                null,
                true,
                "HRNT01",
                "4IJ18H",
                3000,
                thinkPadE14,
                humanResources
        );

        Asset nocnt01 = new Asset(
                null,
                true,
                "NOCNT01",
                "9YTR4O",
                5000,
                latitude5440,
                noc
        );

        Asset comercialnt01 = new Asset(
                null,
                true,
                "COMERCIALNT01",
                "42JLRW",
                3000,
                thinkPadE14,
                comercial
        );


        // Montagem dos relacionamentos

        categoryService.addCategory(notebook);
        categoryService.addCategory(desktop);

        manufacturerService.addManufacturer(dell);
        manufacturerService.addManufacturer(lenovo);

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


        // Cadastro inicial das localizações
        locationService.addLocation(humanResources);
        locationService.addLocation(noc);
        locationService.addLocation(comercial);


        // Cadastro inicial dos ativos
        assetService.addAsset(hrnt01);
        assetService.addAsset(nocnt01);
        assetService.addAsset(comercialnt01);


        System.out.println("Ativos cadastrados no banco:");
        assetService.getAllAssets().forEach(System.out::println);
    }
}