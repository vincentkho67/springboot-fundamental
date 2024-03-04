package bytebrewers.bitpod.utils.swagger.bank;
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
        description = "Create Bank",
        summary = "Create Bank with SuperAdmin or Admin account",
        responses = {
                @ApiResponse(
                        description = "Bank created",
                        responseCode = "201",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                                        {
                                                            "status": "Created",
                                                            "message": "Bank created",
                                                            "data": {
                                                                "discardedAt": null,
                                                                "id": 1,
                                                                "name": "Permata",
                                                                "address": "Jakarta"
                                                            }
                                                        }
                                """
                                        )
                                )
                        }
                ),
                @ApiResponse(
                        description = "Bad Request",
                        responseCode = "400",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                {
                                    
                                    "status": "Bad Request",
                                    "message": "failed to create",
                                    "data": null
                                      
                                }
                                """
                                        )
                                )
                        }
                )
        }
)
public @interface SwaggerBankCreate {
}
