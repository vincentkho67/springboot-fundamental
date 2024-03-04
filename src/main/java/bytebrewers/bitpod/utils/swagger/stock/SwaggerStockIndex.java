package bytebrewers.bitpod.utils.swagger.stock;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
        description = "Register Member",
        summary = "Register Member",
        responses = {
                @ApiResponse(
                        description = "Stock found",
                        responseCode = "200",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                                        {
                                                            "status": "OK",
                                                            "message": "Stock found",
                                                            "data": {
                                                                "content": [
                                                                    {
                                                                        "discardedAt": null,
                                                                        "id": "b3921dd2-d9c6-4a19-acf4-278a0ec21f76",
                                                                        "name": "ACES",
                                                                        "company": "Astra International Tbk.",
                                                                        "price": 55.0,
                                                                        "lot": 1949314
                                                                    },
                                                                    {
                                                                        "discardedAt": null,
                                                                        "id": "4d71ca4c-9347-4239-baa3-3169d18feea0",
                                                                        "name": "ADRO",
                                                                        "company": "Adaro Energy Tbk.",
                                                                        "price": 93.0,
                                                                        "lot": 1410954
                                                                    },
                                                                    {
                                                                        "discardedAt": null,
                                                                        "id": "0fe09bff-55eb-4887-8830-6b4a25f2443c",
                                                                        "name": "INDY",
                                                                        "company": "Indika Energy Tbk.",
                                                                        "price": 100.0,
                                                                        "lot": 1932064
                                                                    }
                                                                ],
                                                                "totalElements": 16,
                                                                "totalPages": 2,
                                                                "page": 0,
                                                                "size": 10
                                                            }
                                                        }
                                """
                                        )
                                )
                        }
                ),
                @ApiResponse(
                        description = "fetch failed",
                        responseCode = "400",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                {
                                    
                                    "status": "Bad Request",
                                    "message": "failed to fetch",
                                    "data": null
                                      
                                }
                                """
                                        )
                                )
                        }
                )
        }
)
public @interface SwaggerStockIndex {
}
