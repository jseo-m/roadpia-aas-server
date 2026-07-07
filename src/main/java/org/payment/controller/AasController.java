package org.payment.controller;

import org.payment.controller.docs.AasControllerDocs;
import org.payment.domain.aas.AasAssetDetailResponse;
import org.payment.domain.aas.AasAssetSummaryResponse;
import org.payment.domain.aas.AasAssetTreeResponse;
import org.payment.service.aas.AasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/aas/assets")
public class AasController implements AasControllerDocs {

    private final AasService aasService;

    public AasController(AasService aasService) {
        this.aasService = aasService;
    }

    @Override
    @GetMapping
    public List<AasAssetSummaryResponse> getAssets() {
        return aasService.getAssets();
    }

    @Override
    @GetMapping("/tree")
    public AasAssetTreeResponse getAssetTree() {
        return aasService.getAssetTree();
    }

    @Override
    @GetMapping("/{assetCode}")
    public AasAssetDetailResponse getAsset(@PathVariable("assetCode") String assetCode) {
        return aasService.getAsset(assetCode);
    }

    @Override
    @GetMapping("/{assetCode}/submodels/nameplate")
    public AasAssetDetailResponse getNameplate(@PathVariable("assetCode") String assetCode) {
        return aasService.getNameplate(assetCode);
    }

    @Override
    @GetMapping("/{assetCode}/submodels/technical-data")
    public AasAssetDetailResponse getTechnicalData(@PathVariable("assetCode") String assetCode) {
        return aasService.getTechnicalData(assetCode);
    }
}
