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
                        description = "Bank deleted",
                        responseCode = "200",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                                        {
                                                            "status": "OK",
                                                            "message": "Bank deleted",
                                                            "data": null
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
                                    "message": "Bank Not Found",
                                    "data": null
                                      
                                }
                                """
                                        )
                                )
                        }
                )
        }
)
public @interface SwaggerBankDelete {
}
