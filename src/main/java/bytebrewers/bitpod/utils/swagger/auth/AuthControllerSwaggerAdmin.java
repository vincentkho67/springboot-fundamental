package bytebrewers.bitpod.utils.swagger.auth;

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
        description = "Register Admin",
        summary = "Register Admin (ADMIN ONLY)",
        responses = {
                @ApiResponse(
                        description = "Register success",
                        responseCode = "202",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                {
                                    "status": "Created",
                                    "message": "register success",
                                    "data": {
                                      "email": "admin9@gmail.com",
                                      "roles": [
                                        "ROLE_MEMBER",
                                        "ROLE_ADMIN"
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
public @interface AuthControllerSwaggerAdmin {
}
