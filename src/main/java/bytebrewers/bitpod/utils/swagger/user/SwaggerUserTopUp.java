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
        description = "Top Up User's Balance",
        summary = "Top up without midtrans",
        responses = {
                @ApiResponse(
                        description = "Success",
                        responseCode = "200",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                {
                                    "status": "OK",
                                    "message": "topup success",
                                    "data": {
                                        "amount": 100000
                                    }
                                }
                                """
                                        )
                                )
                        }
                ),
                @ApiResponse(
                        description = "Topup failed",
                        responseCode = "400",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                {
                                    
                                    "status": "Bad Request",
                                    "data": "null"
                                }
                                """
                                        )
                                )
                        }
                )
        }
)
public @interface SwaggerUserTopUp {
}
