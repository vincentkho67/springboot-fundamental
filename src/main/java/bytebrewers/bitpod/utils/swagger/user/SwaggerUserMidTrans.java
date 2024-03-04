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
        description = "Top Up via Midtrans",
        summary = "Payment gateway integration",
        responses = {
                @ApiResponse(
                        description = "Payment Token Generated",
                        responseCode = "200",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                                        {
                                                            "status": "OK",
                                                            "message": "Proceed to payment",
                                                            "data": {
                                                                "requestBody": {
                                                                    "credit_card": {
                                                                        "secure": "true"
                                                                    },
                                                                    "transaction_details": {
                                                                        "gross_amount": "100000",
                                                                        "order_id": "bf59d924-1e70-4ade-97a2-d220153faeff"
                                                                    }
                                                                },
                                                                "clientKey": "SB-Mid-client-mmalMqC0Sqqv81WL",
                                                                "token": "https://app.sandbox.midtrans.com/snap/v3/redirection/68619c60-13e1-44a0-8ec3-fe3d4872a99a"
                                                            }
                                                        }
                                """
                                        )
                                )
                        }
                ),
                @ApiResponse(
                        description = "Bad Request",
                        responseCode = "400",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "object",
                                                example = """
                                {
                                    "timestamp": "2024-03-04T10:50:58.148+00:00",
                                    "status": 400,
                                    "error": "Bad Request",
                                    "path": "/api/users/topup/midtrans"
                                }
                                """
                                        )
                                )
                        }
                )
        }
)
public @interface SwaggerUserMidTrans {
}
