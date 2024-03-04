package bytebrewers.bitpod.utils.swagger.user;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
    description = "get User By id",
    summary = "get User By id",
    responses = {
        @ApiResponse(
            description = "User Found",
            responseCode = "200",
            content = {
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                        // implementation = ControllerResponse.class,
                        type = "object",
                        example = """
                                {
                                    "status": "OK",
                                    "message": "User found",
                                    "data": {
                                        "id": "7f253dad-0f9f-42df-b4af-9d664a686e95",
                                        "name": "baim",
                                        "username": "woiii2@gmail.com",
                                        "address": "Jakarta",
                                        "birthDate": null,
                                        "profilePicture": null,
                                        "balance": 0.00
                                    }
                                }
                                """
                    )
                )
            }
            
        ),
        @ApiResponse(
            description = "User not found",
            responseCode = "400",
            content = {
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                        // implementation = ControllerResponse.class,
                        type = "object",
                        example = """
                                {
                                    
                                    "status": "Bad Request",
                                    "message": "User Not Found",
                                    "data": null
                                      
                                }
                                """
                    )
                )
            }
        )
    }
)
public @interface SwaggerUserGetUserById {
}
