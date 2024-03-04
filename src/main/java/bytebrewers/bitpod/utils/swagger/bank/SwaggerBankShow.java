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
        description = "Register Member",
        summary = "Register Member",
        responses = {
                @ApiResponse(
                        description = "Bank found",
                        responseCode = "200",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                                        {
                                                            "status": "OK",
                                                            "message": "Bank found",
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
                                    "message": "failed to find",
                                    "data": null
                                      
                                }
                                """
                                        )
                                )
                        }
                )
        }
)
public @interface SwaggerBankShow {
}
