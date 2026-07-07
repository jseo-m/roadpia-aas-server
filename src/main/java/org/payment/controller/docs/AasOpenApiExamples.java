package org.payment.controller.docs;

public final class AasOpenApiExamples {

    public static final String ASSET_LIST = """
            [
              {
                "aasId": "urn:aas:kr:mtk01:MTK01_F1_NI1_REC01",
                "idShort": "MTK01_F1_NI1_REC01",
                "factoryCode": "MTK01",
                "areaCode": "F1",
                "lineCode": "NI1",
                "assetType": "REC",
                "assetCode": "MTK01_F1_NI1_REC01",
                "name": "MTK01_F1_NI1_REC01",
                "warnings": []
              },
              {
                "aasId": "urn:aas:kr:mtk01:MTK01_F1_NI1_REC02",
                "idShort": "MTK01_F1_NI1_REC02",
                "factoryCode": "MTK01",
                "areaCode": "F1",
                "lineCode": "NI1",
                "assetType": "REC",
                "assetCode": "MTK01_F1_NI1_REC02",
                "name": "MTK01_F1_NI1_REC02",
                "warnings": []
              }
            ]
            """;

    public static final String ASSET_TREE = """
            {
              "factories": [
                {
                  "code": "MTK01",
                  "name": "MTK01",
                  "areas": [
                    {
                      "code": "F1",
                      "name": "F1",
                      "lines": [
                        {
                          "code": "NI1",
                          "name": "NI1",
                          "assetTypes": [
                            {
                              "code": "REC",
                              "name": "REC",
                              "assets": [
                                {
                                  "aasId": "urn:aas:kr:mtk01:MTK01_F1_NI1_REC01",
                                  "idShort": "MTK01_F1_NI1_REC01",
                                  "assetCode": "MTK01_F1_NI1_REC01",
                                  "name": "MTK01_F1_NI1_REC01",
                                  "warnings": []
                                },
                                {
                                  "aasId": "urn:aas:kr:mtk01:MTK01_F1_NI1_REC02",
                                  "idShort": "MTK01_F1_NI1_REC02",
                                  "assetCode": "MTK01_F1_NI1_REC02",
                                  "name": "MTK01_F1_NI1_REC02",
                                  "warnings": []
                                }
                              ]
                            }
                          ]
                        }
                      ]
                    }
                  ]
                }
              ],
              "warnings": []
            }
            """;

    public static final String ASSET_DETAIL = """
            {
              "asset": {
                "aasId": "urn:aas:kr:mtk01:MTK01_F1_NI1_REC01",
                "idShort": "MTK01_F1_NI1_REC01",
                "factoryCode": "MTK01",
                "areaCode": "F1",
                "lineCode": "NI1",
                "assetType": "REC",
                "assetCode": "MTK01_F1_NI1_REC01",
                "name": "MTK01_F1_NI1_REC01",
                "warnings": [],
                "submodels": {
                  "nameplate": {
                    "id": "MTK01_F1_NI1_REC01:nameplate",
                    "idShort": "nameplate",
                    "name": "nameplate",
                    "values": {
                      "manufacturerName": "Roadpia",
                      "manufacturerProductDesignation": "MTK01_F1_NI1_REC01",
                      "serialNumber": "SN-MTK01-F1-NI1-REC01"
                    },
                    "raw": {}
                  },
                  "technicalData": {
                    "id": "MTK01_F1_NI1_REC01:technicalData",
                    "idShort": "technicalData",
                    "name": "technicalData",
                    "values": {
                      "ratedPower": "3.7kW",
                      "ratedSpeed": "1750rpm"
                    },
                    "raw": {}
                  },
                  "operationData": {
                    "id": "MTK01_F1_NI1_REC01:operationData",
                    "idShort": "operationData",
                    "modelType": "Submodel",
                    "kind": "Instance",
                    "assetCode": "MTK01_F1_NI1_REC01",
                    "submodelElements": [
                      {
                        "modelType": "Property",
                        "idShort": "temperature",
                        "valueType": "xs:double",
                        "value": 42.5,
                        "qualifiers": [
                          {
                            "type": "unit",
                            "valueType": "xs:string",
                            "value": "C"
                          },
                          {
                            "type": "measuredAt",
                            "valueType": "xs:dateTime",
                            "value": "2026-06-25T10:30:00+09:00"
                          }
                        ]
                      }
                    ]
                  }
                }
              }
            }
            """;

    public static final String NAMEPLATE = """
            {
              "asset": {
                "aasId": "urn:aas:kr:mtk01:MTK01_F1_NI1_REC01",
                "idShort": "MTK01_F1_NI1_REC01",
                "factoryCode": "MTK01",
                "areaCode": "F1",
                "lineCode": "NI1",
                "assetType": "REC",
                "assetCode": "MTK01_F1_NI1_REC01",
                "name": "MTK01_F1_NI1_REC01",
                "warnings": [],
                "submodels": {
                  "nameplate": {
                    "id": "MTK01_F1_NI1_REC01:nameplate",
                    "idShort": "nameplate",
                    "name": "nameplate",
                    "values": {
                      "manufacturerName": "Roadpia",
                      "manufacturerProductDesignation": "MTK01_F1_NI1_REC01",
                      "serialNumber": "SN-MTK01-F1-NI1-REC01"
                    },
                    "raw": {}
                  }
                }
              }
            }
            """;

    public static final String TECHNICAL_DATA = """
            {
              "asset": {
                "aasId": "urn:aas:kr:mtk01:MTK01_F1_NI1_REC01",
                "idShort": "MTK01_F1_NI1_REC01",
                "factoryCode": "MTK01",
                "areaCode": "F1",
                "lineCode": "NI1",
                "assetType": "REC",
                "assetCode": "MTK01_F1_NI1_REC01",
                "name": "MTK01_F1_NI1_REC01",
                "warnings": [],
                "submodels": {
                  "technicalData": {
                    "id": "MTK01_F1_NI1_REC01:technicalData",
                    "idShort": "technicalData",
                    "name": "technicalData",
                    "values": {
                      "ratedPower": "3.7kW",
                      "ratedSpeed": "1750rpm",
                      "ratedVoltage": "380V"
                    },
                    "raw": {}
                  }
                }
              }
            }
            """;

    private AasOpenApiExamples() {
    }
}
