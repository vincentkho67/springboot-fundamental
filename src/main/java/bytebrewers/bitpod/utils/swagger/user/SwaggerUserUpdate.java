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
                                                            "message": "User updated",
                                                            "data": {
                                                                "name": "Panji simatupang",
                                                                "username": "superadmin@gmail.com",
                                                                "address": "jalan rambutan",
                                                                "birthDate": "1999-07-27",
                                                                "profilePicture": "http://res.cloudinary.com/de0yidcs5/image/upload/v1709549585/vgiwgfog1d3g8eexry70.png",
                                                                "balance": null
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
                                    "message": "failed to update",
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
