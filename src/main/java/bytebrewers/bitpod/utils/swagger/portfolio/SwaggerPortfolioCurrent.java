package bytebrewers.bitpod.utils.swagger.portfolio;
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
        description = "Portfolio found",
        summary = "Portfolio found",
        responses = {
                @ApiResponse(
                        description = "Portfolio found",
                        responseCode = "200",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                                        {
                                                            "status": "OK",
                                                            "message": "Portfolio found",
                                                            "data": {
                                                                "discardedAt": null,
                                                                "id": "0ef44108-a2cc-4c7c-88e5-53e9547e7784",
                                                                "avgBuy": 8700.00,
                                                                "returns": "Gain 1.00%",
                                                                "transactions": [
                                                                    {
                                                                        "discardedAt": null,
                                                                        "id": "5e2e6ad4-c33b-4889-94a5-1a31be35bc11",
                                                                        "price": 8700.0,
                                                                        "lot": 1,
                                                                        "transactionType": "BUY",
                                                                        "stock": {
                                                                            "discardedAt": null,
                                                                            "id": "03bcdfd9-e7e6-4f2b-8eb7-886dcf048a65",
                                                                            "name": "ACES",
                                                                            "company": "Astra International Tbk.",
                                                                            "price": 88.0,
                                                                            "lot": 1874969
                                                                        }
                                                                    }
                                                                ]
                                                            }
                                                        }
                                """
                                        )
                                )
                        }
                ),
                @ApiResponse(
                        description = "Login failed",
                        responseCode = "400",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                {
                                    
                                    "status": "Bad Request",
                                    "message": "failed to register",
                                    "data": null
                                      
                                }
                                """
                                        )
                                )
                        }
                )
        }
)
public @interface SwaggerPortfolioCurrent {
}
