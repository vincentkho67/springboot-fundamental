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
                                      "discardedAt": null,
                                      "id": "52aa27d3-0742-4c50-ac5c-eb4629942287",
                                      "name": "admin123",
                                      "balance": 432000,
                                      "username": "admin@gmail.com",
                                      "email": "admin@gmail.com",
                                      "password": "$2a$10$w2M60rHtAwgDKClR9bTblOSOxDNb42hQxZgqwI1ej4CI03fmk57FC",
                                      "address": "jakarta",
                                      "birthDate": "1999-12-31",
                                      "profilePicture": "http://res.cloudinary.com/de0yidcs5/image/upload/v1709373744/yzpr3mb7ae0xdjjt4ps0.jpg",
                                      "roles": [
                                        {
                                          "id": "6119a9a2-be19-40fb-907f-956a69e0850c",
                                          "role": "ROLE_MEMBER"
                                        },
                                        {
                                          "id": "5146315d-df56-42e3-9f34-82ac8e8e0d10",
                                          "role": "ROLE_ADMIN"
                                        }
                                      ],
                                      "portfolio": null,
                                      "enabled": true,
                                      "authorities": [
                                        {
                                          "authority": "ROLE_MEMBER"
                                        },
                                        {
                                          "authority": "ROLE_ADMIN"
                                        }
                                      ],
                                      "accountNonLocked": true,
                                      "accountNonExpired": true,
                                      "credentialsNonExpired": true
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
