package bytebrewers.bitpod.utils.swagger.transaction;
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
                        description = "Transaction created",
                        responseCode = "201",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                                        {
                                                            "status": "Created",
                                                            "message": "Transaction created",
                                                            "data": {
                                                                "discardedAt": null,
                                                                "id": "a3e4bbd4-1956-4c8a-acf0-4a6d143fc1d9",
                                                                "price": 8600.0,
                                                                "lot": 1,
                                                                "transactionType": "BUY",
                                                                "stock": {
                                                                    "discardedAt": null,
                                                                    "id": "03bcdfd9-e7e6-4f2b-8eb7-886dcf048a65",
                                                                    "name": "ACES",
                                                                    "company": "Astra International Tbk.",
                                                                    "price": 86.0,
                                                                    "lot": 1464647
                                                                }
                                                            }
                                                        }
                                """
                                        )
                                )
                        }
                )
        }
)
public @interface SwaggerTransactionCreate {
}
