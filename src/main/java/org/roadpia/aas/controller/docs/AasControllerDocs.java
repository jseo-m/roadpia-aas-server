package org.roadpia.aas.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.roadpia.aas.domain.aas.AasAssetDetailResponse;
import org.roadpia.aas.domain.aas.AasAssetSummaryResponse;
import org.roadpia.aas.domain.aas.AasAssetTreeResponse;
import org.roadpia.aas.domain.common.ErrorResponse;

import java.util.List;

@Tag(name = "AAS 자산", description = "BaSyx AAS Environment의 AAS를 외부 서비스용 자산 API로 조회합니다.")
public interface AasControllerDocs {

    @Operation(summary = "AAS 자산 목록 조회", description = "BaSyx /shells API에서 로딩된 전체 AAS 목록을 조회하고 자산 ID 기준으로 변환합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "AAS 자산 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AasAssetSummaryResponse.class)),
                            examples = @ExampleObject(value = AasOpenApiExamples.ASSET_LIST)
                    )
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "BaSyx AAS Environment 연동 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    List<AasAssetSummaryResponse> getAssets();

    @Operation(summary = "AAS 자산 트리 조회", description = "AAS ID 또는 idShort를 파싱해 factory > area > line > assetType > assets 구조로 그룹화합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "AAS 자산 트리 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AasAssetTreeResponse.class),
                            examples = @ExampleObject(value = AasOpenApiExamples.ASSET_TREE)
                    )
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "BaSyx AAS Environment 연동 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    AasAssetTreeResponse getAssetTree();

    @Operation(summary = "특정 자산 AAS 통합 조회", description = "자산 정보와 하위 Submodel인 Nameplate, TechnicalData, 현재 OperationData를 함께 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "특정 자산 AAS 통합 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AasAssetDetailResponse.class),
                            examples = @ExampleObject(value = AasOpenApiExamples.ASSET_DETAIL)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "자산을 찾을 수 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "BaSyx AAS Environment 연동 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    AasAssetDetailResponse getAsset(
            @Parameter(description = "자산 ID", example = "MTK01_F1_NI1_REC01", required = true)
            String assetCode
    );

    @Operation(summary = "특정 자산 Nameplate 조회", description = "특정 자산 AAS의 하위 Nameplate Submodel을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Nameplate Submodel 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AasAssetDetailResponse.class),
                            examples = @ExampleObject(value = AasOpenApiExamples.NAMEPLATE)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "자산을 찾을 수 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "BaSyx AAS Environment 연동 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    AasAssetDetailResponse getNameplate(
            @Parameter(description = "자산 ID", example = "MTK01_F1_NI1_REC01", required = true)
            String assetCode
    );

    @Operation(summary = "특정 자산 TechnicalData 조회", description = "특정 자산 AAS의 하위 TechnicalData Submodel을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "TechnicalData Submodel 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AasAssetDetailResponse.class),
                            examples = @ExampleObject(value = AasOpenApiExamples.TECHNICAL_DATA)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "자산을 찾을 수 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "BaSyx AAS Environment 연동 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    AasAssetDetailResponse getTechnicalData(
            @Parameter(description = "자산 ID", example = "MTK01_F1_NI1_REC01", required = true)
            String assetCode
    );
}
