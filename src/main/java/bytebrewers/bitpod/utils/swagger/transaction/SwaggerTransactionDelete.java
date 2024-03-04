package bytebrewers.bitpod.utils.swagger.transaction;
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
        description = "Transaction deleted",
        summary = "Transaction deleted",
        responses = {
                @ApiResponse(
                        description = "Transaction deleted",
                        responseCode = "200",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                                        {
                                                            "status": "OK",
                                                            "message": "Transaction deleted",
                                                            "data": null
                                                        }
                                """
                                        )
                                )
                        }
                )
        }
)
public @interface SwaggerTransactionDelete {
}
