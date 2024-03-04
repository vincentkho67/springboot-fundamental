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
        description = "Get All User",
        summary = "Get All User",
        responses = {
                @ApiResponse(
                        description = "User Found",
                        responseCode = "200",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                {
                                     "status": "OK",
                                     "message": "User found",
                                     "data": {
                                         "content": [
                                             {
                                                 "name": "baim",
                                                 "username": "woiii2@gmail.com",
                                                 "address": "Jakarta",
                                                 "birthDate": null,
                                                 "profilePicture": null,
                                                 "balance": 100000.00
                                             },
                                             {
                                                 "name": null,
                                                 "username": "superadmin@gmail.com",
                                                 "address": null,
                                                 "birthDate": null,
                                                 "profilePicture": null,
                                                 "balance": null
                                             }
                                         ],
                                         "totalElements": 2,
                                         "totalPages": 1,
                                         "page": 0,
                                         "size": 10
                                     }
                                 }
                                """
                                        )
                                )
                        }
                ),
                @ApiResponse(
                        description = "Not Authorized",
                        responseCode = "403",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                {
                                    
                                    "status": "Bad Request",
                                    "message": "Not Authorized",
                                    "data": null
                                      
                                }
                                """
                                        )
                                )
                        }
                )
        }
)
public @interface SwaggerUserIndex {
}
