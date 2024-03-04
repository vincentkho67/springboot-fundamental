package bytebrewers.bitpod.utils.swagger.user;
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
        description = "Update User",
        summary = "Update user and handle profile upload on Cloudinary",
        responses = {
                @ApiResponse(
                        description = "User Updated",
                        responseCode = "200",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                {
                                    "status": "OK",
                                    "message": "User Updated",
                                    "data": {
                                      
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
public @interface SwaggerUserUpdate {
}
